importScripts('/js/sockjs-stomp.bundle.js');

let socket = null;
let stompClient = null;
let ports = [];
let changeStatus = false;

function connectWebSocket(authorizedUser) {
    if (!socket) {
        socket = new SockStompLib.SockJS('/ws');
        stompClient = SockStompLib.Stomp.over(socket);

        stompClient.heartbeat.outgoing = 20000;
        stompClient.heartbeat.incoming = 20000;

        stompClient.debug = function (str) {
            if (str.startsWith('>>> PING') || str.startsWith('<<< PONG')) {
                return;
            }
        };

        stompClient.connect({}, function (frame) {
            // Підписуємося на загальний канал
            stompClient.subscribe('/topic/chat.general', function (message) {
                let data = JSON.parse(message.body);
                sendMessageToAllPorts({type: 'receiveMessageFromGeneral', data: data})
            });

            stompClient.subscribe('/topic/chat.'+authorizedUser, function (message) {
                let data = JSON.parse(message.body);
                sendMessageToAllPorts({type: 'receiveMessageFromUser', data: data})
            });


        }, function (error) {
            console.error('Error connecting to WebSocket:', error);
            showPopUpMessage(null,'Could not connect to WebSocket. Please try again.',"ERROR");
        });

        socket.onclose = function() {
        };

    }
}

// Подія підключення нової вкладки
self.onconnect = function(e) {
    const port = e.ports[0];
    ports.push(port);


    // Обробка вхідних повідомлень від вкладок
    port.onmessage = function(event) {
        const { type, data } = event.data;

        if(type === "connectSocket"){
            // Надсилаємо команду для підключення WebSocket при першому підключенні
            if (!socket) {
                connectWebSocket(data.authorizedUser);
                port.postMessage({ type: 'connected', data: 'Worker is connected!' });
            }
        } else if (type === 'sendMessageToGeneral') {
            stompClient.send('/send.general', {}, JSON.stringify({ 'senderUsername': data.senderUsername,
                'text': data.text }));
        } else if(type === 'sendMessageToUser'){
            // port.postMessage({ type: 'connected', data: data });
            stompClient.send('/send.to.user', {}, JSON.stringify({ 'senderId': data.senderId,
                'text':data.text,'receiverId': data.receiverId}));
        } else if(type === 'receiveSpecificMessage'){
            stompClient.send('/receive.specific', {}, JSON.stringify({ 'id': data.id}));
        } else if (type === 'disconnect') {
            // Відключаємо порт
            removePort(port); // Видаляємо порт із списку
            port.close();     // Закриваємо порт
            checkAndCloseWorker(); // Перевіряємо, чи є інші підключення
        }
    };


};

function removePort(port) {
    ports = ports.filter(p => p !== port);
}

function sendMessageToAllPorts(event){
    ports.forEach(port => {
        try {
            port.postMessage(event); // Розсилаємо повідомлення всім активним вкладкам
        } catch (error) {
            console.error('Error sending message to port:', error);
            removePort(port); // Видаляємо порт, якщо виникає помилка
        }
    });
}

function checkAndCloseWorker() {
    if (ports.length === 0) {
        console.log("No active connections. Closing the worker.");
        if(changeStatus === true){
            stompClient.send('/change.user.status', {}, 'OFFLINE');
        }
        self.close(); // Закриваємо воркера, якщо більше немає підключених портів
    }
}

// Періодична перевірка активності портів
setTimeout(() => {
    if(changeStatus === false) {
        stompClient.send('/change.user.status', {}, 'ONLINE');
        changeStatus = true;
    }
}, 5000);

