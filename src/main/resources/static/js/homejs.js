// вивід меню логіна і реєстрації
document.addEventListener('DOMContentLoaded', function() {
    const registerLink = document.querySelector('.nav_link[href="#register_Section"]');
    const registerForm = document.getElementById('registerSection');
    const swap_to_login = document.getElementById('swap_to_login');
    const loginLink = document.querySelector('.nav_link[href="#login_Section"]');
    const loginForm = document.getElementById('loginSection');
    const swap_to_register = document.getElementById('swap_to_register');
    const overlay = document.getElementById('overlayEnter');
    const addNewsLink = document.querySelector('.nav_link[href="#add_News"]')
    const addNewsForm = document.getElementById('addNewsSection');

    registerLink.addEventListener('click', function(event) {
        overlay.classList.remove('hidden');
        showElement(registerForm);
    });

    swap_to_login.addEventListener('click', function(event) {
        hideElement(registerForm);
        showElement(loginForm);
    });


    loginLink.addEventListener('click', function(event) {
        overlay.classList.remove('hidden');
        showElement(loginForm);
    });

    swap_to_register.addEventListener('click', function(event) {
        hideElement(loginForm);
        showElement(registerForm);
    });

    if(addNewsLink !== null) {
        addNewsLink.addEventListener('click', function (event) {
            overlay.classList.remove('hidden');
            showElement(addNewsForm);
        })
    }

    overlay.addEventListener('click', function() {
        history.replaceState({}, document.title, window.location.pathname);
        overlay.classList.add('hidden');
        hideElement(registerForm);
        hideElement(loginForm);
        hideElement(addNewsForm);
    });
});

function showElement(formElement) {
    formElement.classList.remove('hidden');
}

function hideElement(formElement) {
    formElement.classList.add('hidden');
}



// зміна теми
const logoImg = document.querySelector('#theme-toggle');
const imgElement = document.querySelector('.full-screen-image');
const logooverlay = document.querySelector('.overlay');
const loginSection = document.querySelector("#loginSection");
const newDataSection = document.querySelector('.newsDataSection');
const slideContent = document.querySelectorAll('.slide');
const newLink = document.querySelectorAll('.news_link');
const registerSection = document.querySelector("#registerSection");
const addNewsSection = document.querySelector("#addNewsSection");

// Перевірка збереженого стану теми при завантаженні сторінки
document.addEventListener('DOMContentLoaded', function() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-theme');
        applyDarkTheme();
    } else {
        applyLightTheme();
    }
});

logoImg.addEventListener('click', function() {
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
    loginSection.style.backgroundImage = 'linear-gradient(to bottom right, #d1ddf3, #a6c2ea)';
    registerSection.style.backgroundImage = 'linear-gradient(to bottom right, #d1ddf3, #a6c2ea)';
    addNewsSection.style.backgroundImage = 'linear-gradient(to bottom right, #d1ddf3, #a6c2ea)';
    newDataSection.style.backgroundImage = 'linear-gradient(to bottom right, #bdd2ee, #4879bb)';
    slideContent.forEach(slide => {
        slide.style.color = '#000000';
    });
    newLink.forEach(link => {
        link.style.backgroundColor = '#000000';
        link.style.color = '#ffffff';
    })
}
function applyDarkTheme() {
    imgElement.src = '../images/nightStartLogo.jpg';
    logooverlay.style.backgroundImage = 'linear-gradient(45deg, #cecccc, #000000)';
    loginSection.style.backgroundImage = 'linear-gradient(to bottom right, #ffffff, #d2d0d0)';
    registerSection.style.backgroundImage = 'linear-gradient(to bottom right, #ffffff, #d2d0d0)';
    addNewsSection.style.backgroundImage = 'linear-gradient(to bottom right, #ffffff, #d2d0d0)';
    newDataSection.style.backgroundImage = 'linear-gradient(to bottom right, #ccbebe, #000000)';
    slideContent.forEach(slide => {
        slide.style.color = '#ffffff';
    });
    newLink.forEach(link => {
        link.style.backgroundColor = '#ffffff';
        link.style.color = '#000000';
    })
}


// Вибираємо елемент з класом error-message
const errorMessageElement = document.querySelector('.error-message');
// Якщо елемент існує
if (errorMessageElement) {
    // Затримка відображення вспливаючого вікна - 4 секунд
    setTimeout(function() {
        // Змінюємо прозорість елементу на 0
        errorMessageElement.style.opacity = '0';
        // Після 0.5 секунд змінюємо висоту елементу на 0 і прибираємо його з потоку документу
        setTimeout(function() {
            errorMessageElement.style.height = '0';
            errorMessageElement.style.overflow = 'hidden';
            // Видаляємо параметр "?error=access-denied" з URL
            history.replaceState({}, document.title, window.location.pathname);
        }, 500);
    }, 4000); // 4000 мілісекунд = 4 секунди
}


//register image
function updateLabel(fileInputText,fileLabelText,maxSizeMB,pixel) {
    const fileInput = document.getElementById(fileInputText);
    const fileLabel = document.getElementById(fileLabelText);

    if (fileInput.files.length > 0) {
        const file = fileInput.files[0];
        if (file.size > maxSizeMB * 1000 * 1024) { // 1 MB (конвертуємо в байти)
            alert('Зображення повинно бути менше '+maxSizeMB+' MB');
            fileInput.value = ''; // Очищаємо значення введення
            fileLabel.textContent = 'Оберіть зображення';
            return;
        }
        const img = new Image();
        img.onload = function() {
            if (this.width > pixel || this.height > pixel) {
                alert('Зображення повинно мати розмір не більше '+pixel+'x'+pixel+' пікселів');
                fileInput.value = ''; // Очищаємо значення введення
                fileLabel.textContent = 'Оберіть зображення';
                return;
            }
            fileLabel.textContent = 'Обрано: '+ file.name;
        };
        img.src = URL.createObjectURL(file);
    } else {
        fileLabel.textContent = 'Оберіть зображення';
    }
}



//
document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Зупинити стандартну подію відправки форми
    // Отримати дані форми
    var formData = new FormData(this);

    // Зробити AJAX-запит
    var xhr = new XMLHttpRequest();
    xhr.open('POST', this.getAttribute('action'), true);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.setRequestHeader('X-CSRF-TOKEN', document.querySelector('input[name="_csrf"]').value); // Додаємо CSRF токен як заголовок

    xhr.onload = function() {
        if (xhr.status === 200) {
            // Обробити результат успішної відправки форми
            var response = JSON.parse(xhr.responseText);
            if (response.success) {
                history.pushState({}, '', '#login_Section');
                document.getElementById('swap_to_login').click();
                alert('Ви успішно зареєстровані!');
            } else {
                alert('Помилка: ' + response.message);
            }
        } else {
            alert('Сталася помилка під час відправки форми.');
        }
    };
    xhr.onerror = function() {
        // Обробити помилку
        alert('Сталася помилка під час відправки форми.');
    };

    xhr.send(formData);
});


document.getElementById('newsForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Зупинити стандартну подію відправки форми
    // Отримати дані форми
    var formData = new FormData(this);



    // Зробити AJAX-запит
    var xhr = new XMLHttpRequest();
    xhr.open('POST', this.getAttribute('action'), true);
    xhr.setRequestHeader('Accept', 'application/json');
    xhr.setRequestHeader('X-CSRF-TOKEN', document.querySelector('input[name="_csrf"]').value); // Додаємо CSRF токен як заголовок

    xhr.onload = function() {
        if (xhr.status === 200) {
            alert('Новину успішно додано - обновіть сторінку.');
        } else {
            alert('Сталася помилка під час відправки форми.');
        }
    };
    xhr.onerror = function() {
        alert('Сталася помилка під час відправки форми.');
    };

    xhr.send(formData);
});

// news section
function confirmDelete(element) {
    const newsId = element.getAttribute('data-id');
    const confirmation = confirm('Ви впевнені, що хочете видалити цю новину?');

    if (confirmation) {
        const csrfToken = document.querySelector('input[name="_csrf"]').value;
        fetch(`/news/delete-news/${newsId}`, {
            method: 'PATCH',
            headers: {
            'X-CSRF-TOKEN': csrfToken
            }
        })
            .then(response => {
                if (response.ok) {
                    // Успішне видалення - перезавантажити сторінку або видалити елемент з DOM
                    // location.reload(); // або видалити елемент з DOM
                    document.querySelector(`.slide [data-id="${newsId}"]`).parentElement.remove();
                } else {
                    alert('Помилка при видаленні новини.');
                }
            });
    }
}
