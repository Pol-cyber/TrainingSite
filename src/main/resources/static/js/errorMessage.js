// Повідомлення про помилку
function showPopUpMessage(errorMessageElement, textError, type) {
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