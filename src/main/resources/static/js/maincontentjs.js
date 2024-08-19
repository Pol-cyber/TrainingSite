// зміна теми
const logoImg = document.querySelector('#theme-toggle');
const imgElement = document.querySelector('.full-screen-image');
const logooverlay = document.querySelector('.overlay');
const settingcontent = document.querySelector('.settings-content');
const labelElement = document.querySelectorAll('label');
const menuLink = document.querySelectorAll('.menu-link');
const menuItems = document.querySelector('.menu-items');

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
    settingcontent.style.backgroundImage = 'linear-gradient(to bottom right, #bdd2ee, #4879bb)';
    menuItems.style.backgroundImage = 'linear-gradient(to bottom right, #bdd2ee, #4879bb)';
    labelElement.forEach(label => {
        label.style.color = "black";
    });
    menuLink.forEach(menuLink => {
        if(menuLink.style.color === "black"){
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
    labelElement.forEach(label => {
        label.style.color = "white";
    });
    menuLink.forEach(menuLink => {
        if(menuLink.style.color === "white"){
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
        if(localStorage.getItem('theme') === "dark"){
            lastClickedLink.style.color = "white";
        } else {
            lastClickedLink.style.color = "black";
        }
    }

    clickedLink.style.textDecoration = "underline";
    clickedLink.style.pointerEvents = "none";
    if(localStorage.getItem('theme') === "dark"){
        clickedLink.style.color = "black";
    } else {
        clickedLink.style.color = "white";
    }

    lastClickedLink = clickedLink;

    if (menuItem === 'characteristic') {
        document.querySelector('.characteristic').style.display = 'block';
        document.querySelector('.tschedule').style.display = 'none';
    } else if (menuItem === 'tschedule') {
        document.querySelector('.characteristic').style.display = 'none';
        document.querySelector('.tschedule').style.display = 'block';
    }
}


document.addEventListener("DOMContentLoaded", function() {
    // Отримуємо значення параметра # з URL
    var hash = window.location.hash;

    // Перевіряємо, чи присутній параметр #
    if (hash) {
        // Викликаємо функцію для обробки параметра #
        handleHash(hash);

        // Очищаємо параметр # з URL
        history.replaceState(null, document.title, window.location.pathname);
    } else {
        showForm('characteristic');
    }
});

function handleHash(hash) {
    if (hash === "#tschedule") {
        // Логіка для відображення графіка тренувань
        showForm('tschedule');
    } else {
        showForm('characteristic');
    }
}