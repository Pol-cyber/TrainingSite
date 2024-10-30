// зміна теми
const logoImg = document.querySelector('#theme-toggle');
const imgElement = document.querySelector('.full-screen-image');
const logooverlay = document.querySelector('.overlay');
const settingcontent = document.querySelector('.settings-content');
// const labelElement = document.querySelectorAll('.changeStyle');
const labelElement = document.querySelectorAll('.settings-content label:not(.checkboxes label)');
const menuLink = document.querySelectorAll('.menu-link');
const menuItems = document.querySelector('.menu-items');
const sliderValue = document.querySelector('#sliderValue');

// Перевірка збереженого стану теми при завантаженні сторінки
document.addEventListener('DOMContentLoaded', function () {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-theme');
        applyDarkTheme();
    } else {
        applyLightTheme();
    }
});

logoImg.addEventListener('click', function () {
    document.body.classList.toggle('dark-theme');
    const currentTheme = document.body.classList.contains('dark-theme') ? 'dark' : 'light';
    if (currentTheme === 'dark') {
        applyDarkTheme();
    } else {
        applyLightTheme();
    }
    localStorage.setItem('theme', currentTheme);
});

function applyLightTheme() {
    imgElement.src = '../images/dayStartLogo.jpg';
    logooverlay.style.backgroundImage = 'linear-gradient(45deg, #bdd2ee, #4879bb)';
    settingcontent.style.backgroundImage = 'linear-gradient(to bottom right, #bdd2ee, #4879bb)';
    menuItems.style.backgroundImage = 'linear-gradient(to bottom right, #bdd2ee, #4879bb)';
    sliderValue.style.color = "black";
    labelElement.forEach(label => {
        label.style.color = "black";
    });
    menuLink.forEach(menuLink => {
        if (menuLink.style.color === "black") {
            menuLink.style.color = "white";
        } else {
            menuLink.style.color = "black";
        }
    });
}

function applyDarkTheme() {
    imgElement.src = '../images/nightStartLogo.jpg';
    logooverlay.style.backgroundImage = 'linear-gradient(45deg, #cecccc, #000000)';
    settingcontent.style.backgroundImage = 'linear-gradient(to bottom right, #ccbebe, #000000)';
    menuItems.style.backgroundImage = 'linear-gradient(to bottom right, #ccbebe, #000000)';
    sliderValue.style.color = "white";
    labelElement.forEach(label => {
        label.style.color = "white";
    });
    menuLink.forEach(menuLink => {
        if (menuLink.style.color === "white") {
            menuLink.style.color = "black";
        } else {
            menuLink.style.color = "white";
        }
    });
}


/*Зміна контенту при натисненні*/

var lastClickedLink = null;

function showForm(menuItem) {

    var linkId = menuItem + "-link";
    var clickedLink = document.getElementById(linkId);

    if (lastClickedLink !== null) {
        lastClickedLink.style.textDecoration = "";
        lastClickedLink.style.pointerEvents = "auto";
        if (localStorage.getItem('theme') === "dark") {
            lastClickedLink.style.color = "white";
        } else {
            lastClickedLink.style.color = "black";
        }
    }

    clickedLink.style.textDecoration = "underline";
    clickedLink.style.pointerEvents = "none";
    if (localStorage.getItem('theme') === "dark") {
        clickedLink.style.color = "black";
    } else {
        clickedLink.style.color = "white";
    }

    lastClickedLink = clickedLink;

    if (menuItem === 'characteristics') {
        document.querySelector('.characteristics-section').style.display = 'block';
        document.querySelector('.tSchedule-section').style.display = 'none';
    } else if (menuItem === 'tSchedule') {
        document.querySelector('.characteristics-section').style.display = 'none';
        document.querySelector('.tSchedule-section').style.display = 'block';
    }
}

// перевірка де знаходиться користувач

document.addEventListener("DOMContentLoaded", function () {
    // Отримуємо значення параметра # з URL
    var hash = window.location.hash;

    // Перевіряємо, чи присутній параметр #
    if (hash) {
        // Викликаємо функцію для обробки параметра #
        handleHash(hash);

        // Очищаємо параметр # з URL
        history.replaceState(null, document.title, window.location.pathname);
    } else {
        showForm('characteristics');
    }
});

function handleHash(hash) {
    if (hash === "#tSchedule") {
        // Логіка для відображення графіка тренувань
        showForm('tSchedule');
    } else {
        showForm('characteristics');
    }
}

// Перевірка обраних типів вправ
const checkboxesLabels = document.querySelectorAll('.checkboxesLabel');

checkboxesLabels.forEach(label => {
    label.addEventListener('change', function () {
        // const meditationCheckbox = document.querySelector('input[name="meditation"]');
        // const strengthCheckbox = document.querySelector('input[name="strength"]');
        const tSchedule_Submit = document.getElementById("tSchedule_submitButton");

        // Перевірка, чи обрано жоден з чекбоксів
        if (areAllCheckboxesNoChecked()) {
            showErrorMessage(null,'Потрібно вибрати щонайменше один елемент',"ERROR");
            tSchedule_Submit.disabled = true;
            tSchedule_Submit.style.backgroundColor = '#af4b4b'
            // errorMessage.style.display = 'block'; // Показати повідомлення
         } else {
            // errorMessage.style.display = 'none'; // Сховати повідомлення, якщо хоча б один чекбокс вибрано
            tSchedule_Submit.disabled = false;
            tSchedule_Submit.style.backgroundColor = '#1da332'
        }
    });
});

function areAllCheckboxesNoChecked() {
    return Array.from(checkboxesLabels).every(label => {
        const checked = label.checked;
        return !checked;  // Перевіряємо, чи чекбокс існує
    });
}

// Функціонал звернення в тех. підтримку
let allAdminVariable = null;
var lastConnectedTime = null;

function goToChatWithAdmin(){
    const overlay = document.getElementById('mainOverlay');
    let adminListContainer = document.getElementById('adminList');
    if(lastConnectedTime != null && new Date().getTime() - lastConnectedTime > 20000){
        allAdminVariable = null;
    }
    if(allAdminVariable === null) {
        fetch('/api/user/getAdminUserName/' + authorizedUser)
            .then(response => response.json())
            .then(allAdmin => {
                // Очищуємо контейнер для списку адмінів
                adminListContainer.innerHTML = ''; // Очищення попереднього вмісту

                const message = document.createElement('p');
                message.innerText = 'Оберіть адміністратора до якого бажаєте звернутись';
                adminListContainer.appendChild(message);

                // Створюємо кнопку або список для кожного адміна
                if (allAdmin.length === 0) {
                    // Якщо список порожній, показуємо повідомлення
                    showErrorMessage(null, 'Немає доступних адміністраторів',"ERROR");
                } else {
                    allAdmin.forEach(admin => {
                        let adminButton = document.createElement('button');
                        adminButton.innerText = admin.username +" ("+admin.status+")";
                        adminButton.onclick = function () {
                            closeOverlay();
                            openChatWithUser(admin.username); // Відкриття чату з обраним адміном
                        };
                        adminListContainer.appendChild(adminButton);
                    });
                    lastConnectedTime = new Date().getTime();
                    allAdminVariable = allAdmin;
                    adminListContainer.style.display='block';
                    overlay.style.display = 'block';
                }
            })
            .catch(error => console.error('Error fetching messages:', error));
    } else {
        adminListContainer.style.display='block';
        overlay.style.display = 'block';
    }
}

function closeOverlay() {
    let adminListContainer = document.getElementById('adminList');
    const overlay = document.getElementById('mainOverlay');
    overlay.style.display = 'none'; // Сховати оверлей
    adminListContainer.style.display = 'none';
    const socialMediaMenu = document.getElementById('socialMediaMenu');
    socialMediaMenu.style.display = 'none';
}

// Медіа меню
function showAllMedia() {
    // Показати оверлей і меню
    const overlay = document.getElementById('mainOverlay');
    const socialMediaMenu = document.getElementById('socialMediaMenu');

    overlay.style.display = 'block';
    socialMediaMenu.style.display = 'block';
}

// Відправка характеристики за допомогою fetch
document.getElementById('characteristicForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Зупинити стандартну подію відправки форми

    const formData = new FormData(this); // Збираємо дані з форми
    const dataObject = {};

    // Перетворюємо FormData на звичайний об'єкт
    const healthConditions = [];
    formData.forEach((value, key) => {
        if (key === 'healthConditions') {
            healthConditions.push(value); // Додаємо всі вибрані чекбокси до масиву
        } else {
            dataObject[key] = value; // Інші поля
        }
    });

    // Додаємо масив тренувань до dataObject
    dataObject['healthConditions'] = healthConditions;

    fetch('/api/user/characteristic', {
        method: 'POST', // Метод запиту
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': dataObject["_csrf"]
        },
        body: JSON.stringify(dataObject) // Перетворюємо дані у формат JSON
    })
        .then(response =>  {
            if(response.ok){
                // Обробляємо відповідь
                document.getElementById("characteristicsButton").textContent = "Оновити характеристику";
                showErrorMessage(null,"Успішно збережено","NON_ERROR");
            } else {
                // Обробляємо відповідь
                response.text().then(errorMessage => {
                    showErrorMessage(null, errorMessage, "ERROR");
                });
            }
        })
        .catch(error => console.error('Помилка запиту:', error));
});


// Створення графіку тренувань
document.getElementById('tScheduleForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Зупинити стандартну подію відправки форми

    const formData = new FormData(this); // Збираємо дані з форми
    const dataObject = {};

    // Перетворюємо FormData на звичайний об'єкт
    const trainingTypes = [];
    formData.forEach((value, key) => {
        if (key === 'trainingTypes') {
            trainingTypes.push(value); // Додаємо всі вибрані чекбокси до масиву
        } else {
            dataObject[key] = value; // Інші поля
        }
    });

    // Додаємо масив тренувань до dataObject
    dataObject['trainingTypes'] = trainingTypes;

    fetch('/api/workout/create', {
        method: 'POST', // Метод запиту
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': dataObject["_csrf"]
        },
        body: JSON.stringify(dataObject) // Перетворюємо дані у формат JSON
    })
        .then(response =>  {
            if(response.ok){
                // Обробляємо відповідь
                showErrorMessage(null,"Успішно збережено","NON_ERROR");
            } else {
                // Обробляємо відповідь
                response.text().then(errorMessage => {
                    showErrorMessage(null, errorMessage, "ERROR");
                });
            }
        })
        .catch(error => console.error('Помилка запиту:', error));
});