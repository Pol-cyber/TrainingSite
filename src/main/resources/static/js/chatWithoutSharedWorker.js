// Підлючення сокету та чат
var socket = null;
var stompClient = null;
const authorizedUser = document.getElementById("authorizeUserName").textContent;

document.addEventListener('DOMContentLoaded', function () {
    // sessionStorage.removeItem("PageIsHidden");
    // sessionStorage.removeItem("onBeforeUnloadFlag");

    function connectWebSocket() {
        socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.heartbeat.outgoing = 20000;
        stompClient.heartbeat.incoming = 20000;

        stompClient.debug = function (str) {
            if (str.startsWith('>>> PING') || str.startsWith('<<< PONG')) {
                return;
            }
        };

        stompClient.connect({}, function (frame) {

            stompClient.subscribe('/topic/chat.general', function (message) {
                // alert('Message received: ' + message.body);
                let data = JSON.parse(message.body);
                let text = data.text;
                let senderUsername = data.senderUsername;
                receiveMessageToGeneral(senderUsername,text);

            });

            stompClient.subscribe('/topic/chat.'+authorizedUser, function (message) {
                let data = JSON.parse(message.body);
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
                    let interlocutor = checkWithWhoConversation();
                    if(interlocutor === null){
                        return null;
                    }
                    if(data.senderId === interlocutor) {
                        receiveMessageFromUser(text);
                        stompClient.send('/receive.specific', {}, JSON.stringify({ 'id': data.id}));
                    } else {
                        showPopUpMessage(null,"Вам надійшло повідомлення від іншого користувача.","NON_ERROR");
                    }
                }
            });

            localStorage.setItem('beConnection', JSON.stringify({
                connected: true,
                lastConnectedTime: new Date().getTime()
            }));
        }, function (error) {
            console.error('Error connecting to WebSocket: ' + error);
            showPopUpMessage(null,'Could not connect to WebSocket. Please try again.',"ERROR");
        });

        socket.onclose = function() {
        };
    }

    function reconnectWebSocket() {
        var savedConnection = localStorage.getItem('beConnection');
        if (savedConnection) {
            savedConnection = JSON.parse(savedConnection);
            if (savedConnection.connected) {
                connectWebSocket();
            }
        }
    }

    function funcForConnection() {
        if(authorizedUser !== 'undefined') {
            var savedConnection = localStorage.getItem('beConnection');
            if (savedConnection == null || !savedConnection.connected) {
                setTimeout(connectWebSocket,100);
            }
            else {
                savedConnection = JSON.parse(savedConnection);
                var lastConnectedTime = savedConnection.lastConnectedTime;
                var currentTime = new Date().getTime();
                var timeDifference = currentTime - lastConnectedTime;
                if (timeDifference < 10000) {
                    setTimeout(reconnectWebSocket, 2000);
                } else {
                    setTimeout(reconnectWebSocket, 100);
                }
            }
        }
    }


    funcForConnection();
});


function sendMessageToUser() {
    let chatInput = document.getElementById('chatToUserInput');
    let chatContent = document.getElementById('chatContent');
    let message = chatInput.value;
    if(message.length < 1){
        showPopUpMessage(null,"Пусті повідомлення забороняються.","ERROR");
        return;
    }
    let receiver = checkWithWhoConversation();
    if(receiver === null){
        return null;
    }
    stompClient.send('/send.to.user', {}, JSON.stringify({ 'senderId': authorizedUser,
        'text':message,'receiverId': receiver}));

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


function sendMessageToGeneral() {
    let chatInput = document.getElementById('generalChatInput');
    // let chatContent = document.getElementById('generalСhatContent');
    let message = chatInput.value;
    if(message.length < 1){
        showPopUpMessage(null,"Пусті повідомлення забороняються.","ERROR");
        return;
    }

    stompClient.send('/send.general', {}, JSON.stringify({ 'senderUsername': authorizedUser,
        'text':message }));
}