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

document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', validateForm);
});


async function sendForm() {
    let data = new URLSearchParams();
    data.append("serialNumber", serialNumberEl.value);
    data.append("name", nameEl.value);
    data.append("age", ageEl.value);
    data.append("phone", phoneEl.value);
    data.append("email", emailEl.value);
    data.append("password", passwordOneEl.value);
    data.append("level", levelEl.selectedIndex);
    data.append("isAdmin", isAdminEl.checked);

    fetch('/rowers/create', {
        method: 'post',
        body: data
    }).then(async function (response) {
        let json = await response.json()
        console.log(json);

        if (json.result === false) {
            console.log(json);
            showErrors(json.error);
        } else {
            showSuccess("Rower successfully added!");
            setTimeout(function () {
                window.location = '/rowers/index';
            }, 2000);
        }
    }).catch(() => showError("Error", "Adding rower failed for unknown reason."));

}

async function validateForm(event) {
    event.preventDefault();
    let errors = [];

    import ("/public/scripts/utils/helpers.js").then((helpers) => {

        if (!helpers.validatePhone(phoneEl.value)) {
            errors.push("Invalid phone number received.");
        }

        if (passwordOneEl.value !== passwordTwoEl.value) {
            errors.push("Password aren't match");
        }

        if (levelEl.selectedIndex.valueOf() === 0) {
            errors.push("You must select a rower level");
        }

        if (errors.length > 0) {
            showErrors(errors);
        } else {
            sendForm();
        }
    });
}


function showErrors(errorsList) {
    errorListEl.innerHTML = "";
    errorsList.forEach((error) => {
        let domEl = document.createElement("li");
        domEl.textContent = error;
        domEl.style.fontFamily = "Arial";
        errorListEl.appendChild(domEl);
    });
}




