function toggleEmailText() {
    var emailText = document.getElementById('email_text');
    if (emailText.style.display === 'none') {
        emailText.style.display = 'inline';
    } else {
        emailText.style.display = 'none';
    }
}

function togglePhoneNumber() {
    var phoneNumber = document.getElementById('phone_number');
    if (phoneNumber.style.display === 'none') {
        phoneNumber.style.display = 'inline';
    } else {
        phoneNumber.style.display = 'none';
    }
}

document.addEventListener("DOMContentLoaded", function () {
    const cookieBanner = document.getElementById("cookieBanner");
    const closeBtn = document.getElementById("closeBtn");

    closeBtn.addEventListener("click", function () {
        cookieBanner.classList.remove("show");
    });

    setTimeout(function () {
        cookieBanner.classList.add("show");
    }, 1000); // Показать через 1 секунду после загрузки страницы
});

let isDragging = false;
let startX;
let scrollLeft;

const scrollContainer = document.querySelector('.list_partners');

scrollContainer.addEventListener('mousedown', (e) => {
    isDragging = true;
    startX = e.pageX - scrollContainer.offsetLeft;
    scrollLeft = scrollContainer.scrollLeft;
});

scrollContainer.addEventListener('mouseleave', () => {
    isDragging = false;
});

scrollContainer.addEventListener('mouseup', () => {
    isDragging = false;
});

scrollContainer.addEventListener('mousemove', (e) => {
    if (!isDragging) return;
    e.preventDefault();
    const x = e.pageX - scrollContainer.offsetLeft;
    const walk = (x - startX) * 3; // Adjust the multiplier as needed
    scrollContainer.scrollLeft = scrollLeft - walk;
});

function scrollToPartners(blockId) {
    const block = document.getElementById(blockId);
    if (block) {
        block.scrollIntoView({behavior: 'smooth'});
    }
}


const principles = document.querySelectorAll('.principle');
const firstPrincipleWidth = principles[0].offsetWidth;
const containerWidth = document.querySelector('.principle_container').offsetWidth;
let topPosition = 0;
let totalWidth = 0;

let spacing = (containerWidth - firstPrincipleWidth) / (principles.length - 1);
let leftPosition = 0;

principles.forEach(principle => {
    principle.style.top = `${topPosition}px`;
    principle.style.left = `${leftPosition}px`;

    topPosition += 168;
    leftPosition += spacing;
});

function toggleModal(modalId) {
    var modal = document.getElementById(modalId);
    if (modal) {
        if (modal.style.display === "block") {
            modal.style.display = "none";
        } else {
            modal.style.display = "block";
        }
    }
}

function closeAllModals() {
    var modals = document.querySelectorAll(".modal");
    modals.forEach(function (modal) {
        modal.style.display = "none";
    });
}

/*Открытие модального окна создания аакунта*/
document.getElementById("openReg").addEventListener("click", function (event) {
    event.preventDefault(); // Предотвращаем стандартное действие перехода по ссылке
    closeAllModals();
    toggleModal("registrationModal"); // Открываем модальное окно 1 при клике на ссылку
});

document.getElementById("closeModal1").addEventListener("click", function () {
    toggleModal("registrationModal");
});
/*Открытие модального окна Входа*/
document.getElementById("openWelcome").addEventListener("click", function () {
    closeAllModals();
    toggleModal("welcome_model");
});

document.getElementById("closeModal2").addEventListener("click", function () {
    toggleModal("welcome_model");
});
/*Открытие модального окна Восстановление пароля*/
document.getElementById("open_Step_1").addEventListener("click", function (event) {
    event.preventDefault(); // Предотвращаем стандартное действие перехода по ссылке
    closeAllModals();
    toggleModal("shuufuku_model-step-1"); // Открываем модальное окно 1 при клике на ссылку
});

document.getElementById("closeModal3").addEventListener("click", function () {
    toggleModal("shuufuku_model-step-1");
});

document.querySelectorAll(".openReg").forEach(function (link) {
    link.addEventListener("click", function (event) {
        event.preventDefault();
        closeAllModals();
        toggleModal("registrationModal");
    });
});

document.getElementById('shuufuku_model-step-1').addEventListener('submit', submitStep1Form);

function submitStep1Form(event) {
    event.preventDefault();
    closeAllModals();
    toggleModal("shuufuku_model-step-2");

    let input = document.getElementById('inputEmail').value;

    // Отправка данных на сервер
    fetch('url_для_обработки_формы', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({email: input})
    })
        .then(response => response.json())
        .then(data => {
            document.getElementById('output').innerText = data.message;
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

document.getElementById("closeModal4").addEventListener("click", function () {
    toggleModal("shuufuku_model-step-2");
});
    document.getElementById('shuufuku_model-step-2').addEventListener('submit', submitStep2Form);

    function submitStep2Form(event) {
        event.preventDefault();
        closeAllModals();
        toggleModal("shuufuku_model-step-3");

        let input = document.getElementById('inputEmail').value;

        // Отправка данных на сервер
        fetch('url_для_обработки_формы', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({email: input})
        })
            .then(response => response.json())
            .then(data => {
                document.getElementById('output').innerText = data.message;
            })
            .catch(error => {
                console.error('Ошибка:', error);
            });
    }
document.getElementById("closeModal5").addEventListener("click", function () {
    toggleModal("shuufuku_model-step-3");
});