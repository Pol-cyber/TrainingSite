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
            showPopUpMessage(null,'Потрібно вибрати щонайменше один елемент',"ERROR");
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
        fetch('/api/user/getAdminsUserName/' + authorizedUser)
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
                    showPopUpMessage(null, 'Немає доступних адміністраторів',"ERROR");
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
    document.getElementById('adminList').style.display = 'none';
    document.getElementById('mainOverlay').style.display = 'none';
    document.getElementById('socialMediaMenu').style.display = 'none';
    document.getElementById('scheduleModal').style.display = 'none';
    document.getElementById('userSettingsMenu').style.display = 'none';
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
                showPopUpMessage(null,"Успішно збережено. Будь ласка оновіть сторінку","NON_ERROR");
            } else {
                // Обробляємо відповідь
                response.text().then(errorMessage => {
                    showPopUpMessage(null, errorMessage, "ERROR");
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

    // Показуємо екран завантаження
    document.getElementById('loadingScreen').style.display = 'flex';

    fetch('/api/workout/create', {
        method: 'POST', // Метод запиту
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': dataObject["_csrf"]
        },
        body: JSON.stringify(dataObject) // Перетворюємо дані у формат JSON
    })
        .then(response =>  {
            document.getElementById('loadingScreen').style.display = 'none';
            if(response.ok){
                // Обробляємо відповідь
                showPopUpMessage(null,"Успішно збережено","NON_ERROR");
            } else {
                // Обробляємо відповідь
                response.text().then(errorMessage => {
                    showPopUpMessage(null, errorMessage, "ERROR");
                });
            }
        })
        .catch(error => {
            document.getElementById('loadingScreen').style.display = 'none';
            console.error('Помилка запиту:', error);
        });
});

// список графіків
let selectedPlan = null;

function toggleModal() {
    const modal = document.getElementById('scheduleModal');
    const overlay = document.getElementById('mainOverlay');
    const isVisible = modal.style.display === 'flex';

    modal.style.display = isVisible ? 'none' : 'flex';
    overlay.style.display = isVisible ? 'none' : 'block';
    // Вимкнути кнопки, якщо план не вибрано
    if (selectedPlan == null) {
        document.getElementById('scheduleListButton').style.display = "none";
    } else {
        document.getElementById('scheduleListButton').style.display = "flex";
    }
}

function selectPlan(div) {
    // Знімаємо виділення з інших елементів
    document.querySelectorAll('.training-plan-item').forEach(item => {
        item.classList.remove('selected');
        item.querySelector('.details').style.display = 'none';
    });

    // Встановлюємо вибір для поточного елемента
    const planName = div.querySelector('#selectedPlanName').textContent;
    if(planName === selectedPlan){
          selectedPlan = null;
          document.getElementById('scheduleListButton').style.display = "none";
    } else {
        div.classList.add('selected');
        div.querySelector('.details').style.display = 'block';
        if(selectedPlan == null){
            selectedPlan = planName;
            document.getElementById('scheduleListButton').style.display = "flex";
        } else {
            selectedPlan = planName;
        }
    }
}

function deletePlan() {
    if (confirm("Ви дійсно хочете видалити план: " + selectedPlan + "?")) {
        if(selectedPlan != null) {

            const csrfToken = document.querySelector('input[name="_csrf"]').value;

            fetch('/api/workout/delete/' + selectedPlan, {
                method: 'DELETE',
                headers: {
                    'X-CSRF-TOKEN': csrfToken
                }
            })
                .then(response => {
                    if (response.ok) {
                        // Обробляємо відповідь
                        const trainingPlans = document.querySelectorAll(`.training-plan-item`);
                        trainingPlans.forEach(planItem => {
                            // Знаходимо span з ID selectedPlanName всередині поточного planItem
                            const planNameSpan = planItem.querySelector('#selectedPlanName');

                            // Перевіряємо, чи планNameSpan існує та чи містить потрібний текст
                            if (planNameSpan && planNameSpan.textContent.trim() === selectedPlan) {
                                planItem.remove(); // Видаляємо план
                                selectedPlan = null;
                                document.getElementById('scheduleListButton').style.display = "none";
                            }
                        });
                        showPopUpMessage(null, "Успішно видалено", "NON_ERROR");
                    } else {
                        // Обробляємо відповідь
                        response.text().then(errorMessage => {
                            showPopUpMessage(null, errorMessage, "ERROR");
                        });
                    }
                })
                .catch(error => console.error('Помилка запиту:', error));
        } else {
            showPopUpMessage(null,"Для видалення не обрано план тренування","ERROR")
        }
    }
}

function toggleDetails(div) {
    const details = div.querySelector('.details');
    details.style.display = details.style.display === 'none' ? 'block' : 'none';
    selectPlan(div);
}


// демонстрація детелей плану
function fetchWorkoutPlanDetails() {
    const planName = document.getElementById("selectedPlanName").innerText;
    fetch(`/api/workout/details/${planName}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-Token': document.querySelector('input[name="_csrf"]').value
        }
    })
        .then(response => response.json())
        .then(data => displayWorkoutDetails(data))
        .catch(error => console.error('Error:', error));
}

function displayWorkoutDetails(workoutDays) {
    // Clear any previous content
    const workoutListModal = document.getElementById("workoutListModal");
    workoutListModal.style.display = 'none';
    const workoutDetailsModal = document.getElementById("workoutDetailsModal");
    workoutDetailsModal.style.display = 'flex';
    const detailsContainer = document.getElementById("workoutDetailsContainer");
    detailsContainer.innerHTML = "";

    workoutDays.forEach(day => {
        // Контейнер для кожного дня
        const dayDiv = document.createElement("div");
        dayDiv.classList.add("accordion");

        // Заголовок дня
        const dayTitle = document.createElement("h3");
        dayTitle.textContent = day.day;
        dayDiv.appendChild(dayTitle);

        // Контейнер для вправ
        const panel = document.createElement("div");
        panel.classList.add("panel");

        day.exercises.forEach(exercise => {
            const exerciseItem = document.createElement("div");
            exerciseItem.classList.add("exercise-item");

            exerciseItem.innerHTML = `
                <p><strong>Назва:</strong> ${exercise.name}</p>
                <p><strong>Тривалість:</strong> ${exercise.duration || "N/A"}</p>
                <p><strong>Повтори:</strong> ${exercise.repetitions || "N/A"}</p>
                <p><strong>Сети:</strong> ${exercise.sets || "N/A"}</p>
                <p><strong>Обладнання:</strong> ${exercise.equipment || "N/A"}</p>
            `;

            panel.appendChild(exerciseItem);
        });

        dayDiv.appendChild(panel);
        detailsContainer.appendChild(dayDiv);

        // Додаємо обробник події для розкриття і приховання панелі
        dayDiv.addEventListener("click", function () {
            const isVisible = panel.style.display === "block";
            panel.style.display = isVisible ? "none" : "block";
        });
    });

    // Show the modal or details section with the list of exercises
    document.getElementById("workoutDetailsModal").style.display = 'block';
}

function displayWorkoutListModal(){
    const workoutListModal = document.getElementById("workoutListModal");
    workoutListModal.style.display = 'block';
    const workoutDetailsModal = document.getElementById("workoutDetailsModal");
    workoutDetailsModal.style.display = 'none';
}


// Налаштування юзера
function showUserSettingsMenu() {
    const overlay = document.getElementById('mainOverlay');
    const settingsMenu = document.getElementById('userSettingsMenu');

    overlay.style.display = 'block';
    settingsMenu.style.display = 'block';
}

// Оновлення зображення
function validateAndPreviewImage(fileInputId, fileNameId, maxSizeMB, maxPixel, previewId, acceptButtonId) {
    const fileInput = document.getElementById(fileInputId);
    const fileName = document.getElementById(fileNameId);
    const previewImage = document.getElementById(previewId);
    const acceptButton = document.getElementById(acceptButtonId);
    const previewContainer = document.getElementById('image-preview-container');
    const file = fileInput.files[0];

    // Скидаємо стан кнопки "Прийняти" та попереднього перегляду
    acceptButton.style.display = 'none';
    previewContainer.style.display = 'none';
    previewImage.src = '';
    fileName.textContent = 'Файл не обрано';

    if (!file) return;

    // Перевірка розміру файлу
    if (file.size > maxSizeMB * 1024 * 1024) {
        showPopUpMessage(null,`Зображення повинно бути менше ${maxSizeMB} MB`,"ERROR");
        fileInput.value = ''; // Очищуємо input
        fileName.classList.remove('selected');
        return;
    }

    // Перевірка розмірів зображення
    const img = new Image();
    img.onload = function () {
        if (this.width > maxPixel || this.height > maxPixel) {
            showPopUpMessage(null,`Зображення повинно мати розмір не більше ${maxPixel}x${maxPixel} пікселів`,"ERROR");
            fileInput.value = ''; // Очищуємо input
            fileName.classList.remove('selected');
            return;
        }

        // Оновлення тексту назви файлу
        fileName.textContent = `Обрано: ${file.name}`;
        fileName.classList.add('selected');

        // Відображення попереднього перегляду
        previewImage.src = URL.createObjectURL(file);
        previewContainer.style.display = 'flex';

        // Відображення кнопки "Прийняти"
        acceptButton.style.display = 'inline-block';
    };

    img.onerror = function () {
        showPopUpMessage(null,'Файл не є коректним зображенням.',"ERROR");
        fileInput.value = ''; // Очищуємо input
        fileName.classList.remove('selected');
    };

    img.src = URL.createObjectURL(file);
}

function acceptImage(fileInputId) {
    const fileInput = document.getElementById(fileInputId);
    const file = fileInput.files[0];

    if (!file) {
        alert('Зображення не вибрано!');
        return;
    }

    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    const formData = new FormData();
    formData.append('account-image', file); // Ім'я параметра повинно збігатися з серверним параметром @RequestParam

    fetch('/api/user/account/changeImage', {
        method: 'PATCH', // Заміна на PATCH
        body: formData,
        headers: {
            'X-CSRF-TOKEN': csrfToken
        }
    })
        .then(response => {
            if (response.ok) {
                response.text().then(message => {
                    showPopUpMessage(null,message,"NON_ERROR")
                });
                fileInput.value = ''; // Очищення інпуту після завантаження
            } else {
                response.text().then(error_message => {
                    showPopUpMessage(null,error_message,"ERROR")
                });
            }
        })
        .catch(error => {
            showPopUpMessage(null,'Не вдалося підключитися до сервера',"ERROR");
        });
}

// Підписка або відписка від розсилки
function changeSubscription(action) {
    const csrfToken = document.querySelector('input[name="_csrf"]').value;
    fetch('/api/user/account/changeSubscription?subscribe=' + action, {
        method: 'PATCH',
        headers: {
            'X-CSRF-TOKEN': csrfToken
        }
    }).then(response => {
        if (response.ok) {
            const message = action ? 'Ви підписалися на розсилку!' : 'Ви відписалися від розсилки!';
            showPopUpMessage(null,message,"NON_ERROR");
            toggleSubscriptionButton(action);
        } else {
            const errorMessage = action ? 'Помилка підписки на розсилку.' : 'Помилка відписки від розсилки.';
            showPopUpMessage(null,errorMessage,"ERROR");
        }
    }).catch(error => {
        console.error('Помилка:', error);
        showPopUpMessage(null,'Не вдалося підключитися до сервера',"ERROR");
    });
}

function toggleSubscriptionButton(subscribe) {
    const subscribeButton = document.getElementById('subscribe-button');
    const unsubscribeButton = document.getElementById('unsubscribe-button');

    // В залежності від дії ховаємо або показуємо кнопки
    if (subscribe) {
        subscribeButton.style.display = 'none';
        unsubscribeButton.style.display = 'block';
    } else {
        subscribeButton.style.display = 'block';
        unsubscribeButton.style.display = 'none';
    }
}


