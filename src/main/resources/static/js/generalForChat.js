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

// Функції для отримання повідомлення від іншого користувача/користувачів
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

// зміна к-сті символів в повідомленні
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

// Переключення списку користувачів (Онлайн та офлайн)
function toggleList(listId) {
    let list = document.getElementById(listId);
    if (list.style.display === "none" || list.style.display === "") {
        list.style.display = "block";
    } else {
        list.style.display = "none";
    }
}

// відкриття чату із конкретним користувачем
function openChatWithUser(userName) {
    document.getElementById('chatWithUser').textContent = `Chat with ${userName}`;
    document.getElementById('chatBox').style.display = 'flex';
    document.getElementById('generalChatBox').style.display = 'none';
    document.getElementById('userList').style.display = 'none';
    let chatContent = document.getElementById('chatContent');
    chatContent.innerHTML = ''; // Очищення попередніх повідомлень

    fetch('/api/chat/toUser/receiveAllUnreadAndLast10Read?senderId='+authorizedUser+'&receiverId='+userName)
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
    // stompClient.send('/receive.all', {}, JSON.stringify({ 'senderId': userName,
    //     'receiverId': authorizedUser}));
}