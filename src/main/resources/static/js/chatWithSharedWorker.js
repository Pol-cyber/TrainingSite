// Пробую нове зєднання з WebSocket за допомогою Sha
let worker = null;
const authorizedUser = document.getElementById("authorizeUserName").textContent;
document.addEventListener('DOMContentLoaded', function () {

    function funcForConnection() {
        if(authorizedUser !== 'undefined') {
            var savedConnection = localStorage.getItem('beConnection');
            if (savedConnection == null || !savedConnection.connected) {
                setTimeout(createSharedWorker,100);
            }
            else {
                savedConnection = JSON.parse(savedConnection);
                var lastConnectedTime = savedConnection.lastConnectedTime;
                var currentTime = new Date().getTime();
                var timeDifference = currentTime - lastConnectedTime;
                if (timeDifference < 10000) {
                    setTimeout(createSharedWorker, 2000);
                } else {
                    setTimeout(createSharedWorker, 100);
                }
            }
        }
    }

    function createSharedWorker() {
        worker = new SharedWorker('/js/sharedWorker.js');

        worker.port.start(); // Запуск порту для обміну повідомленнями

        const authUser = { authorizedUser: authorizedUser};

        worker.port.postMessage({
            type: 'connectSocket',
            data: authUser,
        });

        // Обробка вхідних повідомлень від SharedWorker
        worker.port.onmessage = function (event) {
            const {type, data} = event.data;

            if (type === 'connected') {
                localStorage.setItem('beConnection', JSON.stringify({
                    connected: true,
                    lastConnectedTime: new Date().getTime()
                }));
                console.log(data);
            } else if (type === 'receiveMessageFromGeneral') {
                let text = data.text;
                let senderUsername = data.senderUsername;
                receiveMessageToGeneral(senderUsername,text);
            } else if(type === 'ping') {
                console.log('Ping');
            } else if(type === 'receiveMessageFromUser'){
                receiveMessageLogic(data)
            }
        };
    }

    funcForConnection();

});

window.addEventListener('beforeunload', function (event) {
    if (worker && worker.port) {
        worker.port.postMessage({ type: 'disconnect' }); // Надсилаємо повідомлення про закриття
    }
});

function receiveMessageLogic(data){
    let text = data.text;
    if(document.getElementById('chatBox').style.display === 'none'){
        if(document.getElementById('userList').style.display !== 'none'){
            let onlineList = document.querySelectorAll("#onlineUserListContent li");
            let offlineList = document.querySelectorAll("#userListContent li");
            onlineList.forEach(onlineUser => {
                if(onlineUser.id === data.senderId){
                    let badgeElement = onlineUser.querySelector('.unread-badge');
                    if(badgeElement){
                        badgeElement.textContent = parseInt(badgeElement.textContent) + 1 +'';
                    } else {
                        badgeElement = document.createElement('span');
                        badgeElement.className = 'unread-badge';
                        badgeElement.textContent = '1';
                        onlineUser.appendChild(badgeElement);
                    }
                }
            });
            //  нижній код не сильно потрібен системі, но осткільки на даний момент немає нормальної системи онлайна я залишу це
            offlineList.forEach(offlineUser => {
                if(offlineUser.id === data.senderId){
                    let badgeElement = offlineUser.querySelector('.unread-badge');
                    if(badgeElement){
                        badgeElement.textContent = parseInt(badgeElement.textContent) + 1 +'';
                    } else {
                        badgeElement = document.createElement('span');
                        badgeElement.className = 'unread-badge';
                        badgeElement.textContent = '1';
                        offlineUser.appendChild(badgeElement);
                    }
                }
            });
        }
    } else {
        let sender = checkWithWhoConversation();
        if(sender === null){
            return null;
        }
        if(data.senderId === sender) {
            receiveMessageFromUser(text);
            worker.port.postMessage({ type: 'receiveSpecificMessage', data: { 'id': data.id} });
        } else {
            showErrorMessage(null,"Вам надійшло повідомлення від іншого користувача.","NON_ERROR");
        }
    }
}


function sendMessageToGeneral() {
    let chatInput = document.getElementById('generalChatInput');
    // let chatContent = document.getElementById('generalСhatContent');
    let message = chatInput.value;
    if(message.length < 1){
        showErrorMessage(null,"Пусті повідомлення забороняються.","ERROR");
        return;
    }

    worker.port.postMessage({ type: 'sendMessageToGeneral', data: { 'senderUsername': authorizedUser,
        'text':message } });
}

function sendMessageToUser() {
    let chatInput = document.getElementById('chatToUserInput');
    let chatContent = document.getElementById('chatContent');
    let message = chatInput.value;
    if(message.length < 1){
        showErrorMessage(null,"Пусті повідомлення забороняються.","ERROR");
        return;
    }
    let receiver = checkWithWhoConversation();
    if(receiver === null){
        return null;
    }
    worker.port.postMessage({ type: 'sendMessageToUser', data: { 'senderId': authorizedUser,
            'text':message,'receiverId': receiver} });

    if (message.trim() !== "") {
        let messageElement = document.createElement('div');
        messageElement.className = 'message my-message';


        let textElement = document.createElement('div');
        textElement.className = 'message-text';
        textElement.textContent = message;

        messageElement.appendChild(textElement);

        chatContent.appendChild(messageElement);
        chatInput.value = "";
        chatContent.scrollTop = chatContent.scrollHeight;
    }
    updateCharCount('chatToUserInput','charToUserCount')
}


// Logout logic
const channel = new BroadcastChannel('logout-channel'); // Ініціалізація BroadcastChannel

// Слухач подій від інших вкладок
channel.onmessage = (event) => {
    if (event.data === 'logout') {
        // Якщо отримали повідомлення про логаут, оновлюємо сторінку
        alert('Ви були вилогінені!');
        location.reload();
    }
};

// Перехоплення відправки форми
const logoutForm = document.getElementById('logoutForm');

if (logoutForm) {  // Перевіряємо, чи існує форма
    logoutForm.addEventListener('submit', (event) => {
        // Додаткові дії перед стандартною відправкою форми
        channel.postMessage('logout');
    });
}
