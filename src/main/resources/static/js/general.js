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
                    let sender = checkWithWhoConversation();
                    if(sender === null){
                        return null;
                    }
                    if(data.senderId === sender) {
                        receiveMessageFromUser(text);
                        stompClient.send('/receive.specific', {}, JSON.stringify({ 'id': data.id}));
                    } else {
                        showErrorMessage(null,"Вам надійшло повідомлення від іншого користувача.","NON_ERROR");
                    }
                }
            });

            localStorage.setItem('beConnection', JSON.stringify({
                connected: true,
                lastConnectedTime: new Date().getTime()
            }));
        }, function (error) {
            console.error('Error connecting to WebSocket: ' + error);
            showErrorMessage(null,'Could not connect to WebSocket. Please try again.',"ERROR");
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
    // console.log(localStorage.getItem("habuma"));
});


/// функцію при логауті з -1 ДОБАВИТИ?????!?!?!??!?!???!
// Chat
// -----------------------------------------------------------

function showChat() {
    document.getElementById('generalChatBox').style.display = 'flex';
    document.getElementById('userList').style.display = 'none';
    document.getElementById('chatBox').style.display = 'none';
    setActiveTab('General');

    fetch('/api/chat/general/last10')
        .then(response => response.json())
        .then(messages => {
            let chatContent = document.getElementById('generalChatContent');
            chatContent.innerHTML = ''; // Очищення попередніх повідомлень
            messages.reverse().forEach(message => {
                receiveMessageToGeneral(message.senderUsername, message.text);
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
}

function showUserList() {
    document.getElementById('userList').style.display = 'flex';
    document.getElementById('chatBox').style.display = 'none';
    document.getElementById('generalChatBox').style.display = 'none';
    setActiveTab('User to User');
    let userListContent = document.getElementById('userListContent');
    let onlineUserListContent = document.getElementById('onlineUserListContent');
    userListContent.innerHTML = '';
    onlineUserListContent.innerHTML = '';
    fetch('/api/user/allUserWithFirstActive')
        .then(response => response.json())
        .then(users => {
            let unreadMessage= {};

            users.forEach(user => {
                if (user.username === authorizedUser) {
                    unreadMessage = user.unreadMessage;
                }
            });

            users.reverse().forEach(user => {
                if (user.username !== authorizedUser) {
                    let li = document.createElement('li');
                    li.className = 'user-item';
                    li.id = user.username;
                    li.textContent = user.username;

                    if (unreadMessage !== undefined && unreadMessage[user.username] !== undefined) {
                        let badge = document.createElement('span');
                        badge.className = 'unread-badge';
                        badge.textContent = unreadMessage[user.username];
                        li.appendChild(badge);
                    }

                    li.onclick = () => openChatWithUser(user.username);
                    if (user.status === 'ONLINE') {
                        onlineUserListContent.appendChild(li);
                    } else {
                        userListContent.appendChild(li);
                    }
                }
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
}

function setActiveTab(tabName) {
    let tabs = document.getElementsByClassName('tab-header');
    for (let i = 0; i < tabs.length; i++) {
        tabs[i].classList.remove('active');
        if (tabs[i].textContent === tabName) {
            tabs[i].classList.add('active');
        }
    }
}


function closeChat(chatMenu) {
    document.getElementById(chatMenu).style.display = 'none';
}

function openChatWithUser(userName) {
    document.getElementById('chatWithUser').textContent = `Chat with ${userName}`;
    document.getElementById('chatBox').style.display = 'flex';
    document.getElementById('generalChatBox').style.display = 'none';
    document.getElementById('userList').style.display = 'none';
    let chatContent = document.getElementById('chatContent');
    chatContent.innerHTML = ''; // Очищення попередніх повідомлень

    fetch('/api/chat/toUser/last10?senderId='+authorizedUser+'&receiverId='+userName)
        .then(response => response.json())
        .then(messages => {
            messages.reverse().forEach(message => {
                if(message.senderId === authorizedUser){
                    let chatInput = document.getElementById('chatToUserInput');
                    let text = message.text;
                    if (text.trim() !== "") {
                        let messageElement = document.createElement('div');
                        messageElement.className = 'message my-message';


                        let textElement = document.createElement('div');
                        textElement.className = 'message-text';
                        textElement.textContent = text;

                        messageElement.appendChild(textElement);

                        chatContent.appendChild(messageElement);
                        chatInput.value = "";
                        chatContent.scrollTop = chatContent.scrollHeight;
                    }
                } else {
                    receiveMessageFromUser(message.text);
                }
            });
        })
        .catch(error => console.error('Error fetching messages:', error));
    stompClient.send('/receive.all', {}, JSON.stringify({ 'senderId': userName,
        'receiverId': authorizedUser}));
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
        showErrorMessage(null,"Пусті повідомлення забороняються.","ERROR");
        return;
    }

    stompClient.send('/send.general', {}, JSON.stringify({ 'senderUsername': authorizedUser,
        'text':message }));
}

// Приклад функції для отримання повідомлення від іншого користувача
function receiveMessageFromUser(message) {
    let chatContent = document.getElementById('chatContent');
    let messageElement = document.createElement('div');
    messageElement.className = 'message other-message';


    let textElement = document.createElement('div');
    textElement.className = 'message-text';
    textElement.textContent = message;

    messageElement.appendChild(textElement);

    chatContent.appendChild(messageElement);
    chatContent.scrollTop = chatContent.scrollHeight;
}

function receiveMessageToGeneral(userName, message) {
    let chatContent = document.getElementById('generalChatContent');
    let chatInput = document.getElementById('generalChatInput');
    let messageElement = document.createElement('div');
    let textElement = document.createElement('div');


    if(userName === authorizedUser) {
        if (message.trim() !== "") {
            messageElement.className = 'message my-message';

            textElement.className = 'message-text';
            textElement.textContent = message;

            messageElement.appendChild(textElement);

            chatContent.appendChild(messageElement);
            chatContent.scrollTop = chatContent.scrollHeight;
            chatInput.value = "";
            updateCharCount('generalChatInput','chatGeneralCount');
        }
    } else {

        messageElement.className = 'message other-message';

        let userElement = document.createElement('div');
        userElement.className = 'message-user';
        userElement.textContent = userName;

        textElement.className = 'message-text';
        textElement.textContent = message;

        messageElement.appendChild(userElement);
        messageElement.appendChild(textElement);

        chatContent.appendChild(messageElement);
        chatContent.scrollTop = chatContent.scrollHeight;
    }

}

function checkWithWhoConversation(){
    let text = document.getElementById('chatWithUser').textContent;
    const prefix = "Chat with ";
    if (text.startsWith(prefix)) {
        return text.slice(prefix.length);
    } else {
        return null;
    }
}


function updateCharCount(nameChanIn,nameCharCount) {
    let chatInput = document.getElementById(nameChanIn);
    let charCount = document.getElementById(nameCharCount);
    charCount.textContent = chatInput.value.length + "/255";
}

function toggleList(listId) {
    let list = document.getElementById(listId);
    if (list.style.display === "none" || list.style.display === "") {
        list.style.display = "block";
    } else {
        list.style.display = "none";
    }
}

// Повідомлення про помилку
function showErrorMessage(errorMessageElement,textError,type) {
    if(errorMessageElement === null){
        errorMessageElement = document.querySelector('.popUpMessage');
    }
    setTimeout(function() {
        // Змінюємо прозорість елементу на 0
        if(type === "NON_ERROR"){
            errorMessageElement.style.color = '#26c546';
        } else {
            errorMessageElement.style.color = '#ec3747';
        }
        errorMessageElement.textContent = textError;
        errorMessageElement.style.opacity = '100';
        // Після 0.5 секунд змінюємо висоту елементу на 0 і прибираємо його з потоку документу
        setTimeout(function() {
            errorMessageElement.style.opacity = '0';
        }, 2000);
    }, 500); // 4000 мілісекунд = 4 секунди
}