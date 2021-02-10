const serialNumberEl = document.getElementById("serialNumber");
const nameEl = document.getElementById("name");
const ageEl = document.getElementById("age");
const phoneEl = document.getElementById("phone");
const emailEl = document.getElementById("email");
const passwordOneEl = document.getElementById("password1");
const passwordTwoEl = document.getElementById("password2");
const levelEl = document.getElementById("level");
const formEl = document.getElementById("createRowerForm");
const errorListEl = document.getElementById("errors");
const isAdminEl = document.getElementById("isAdmin");
const notesEl = document.getElementById("notes");

document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', validateForm);
});


async function sendForm() {
    let data = JSON.stringify({
        serialNumber: serialNumberEl.value,
        name: nameEl.value,
        age: ageEl.value,
        phone: phoneEl.value,
        email: emailEl.value,
        password: passwordOneEl.value,
        level: levelEl.selectedIndex.toString(),
        isAdmin: isAdminEl.checked.toString(),
        notes: notesEl.value
    });

    fetch('/rowers/create', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()

        if (json.result === false) {
            showErrorsInCreateForm(json.error, errorListEl);
        } else {
            showSuccess("Rower successfully added!");
            setTimeout(function () {
                window.location = '/rowers/index';
            }, timeOutTime);
        }
    });

}

async function validateForm(event) {
    event.preventDefault();
    let errors = [];


    if (!validatePhone(phoneEl.value)) {
        errors.push("Invalid phone number received.");
    }

    if (passwordOneEl.value !== passwordTwoEl.value) {
        errors.push("Password aren't match");
    }

    if (levelEl.selectedIndex.valueOf() === 0) {
        errors.push("You must select a rower level");
    }

    if (errors.length > 0) {
        showErrorsInCreateForm(errors, errorListEl);
    } else {
        await sendForm();
    }
}




