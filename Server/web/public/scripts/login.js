const emailInputEl = document.getElementById('email');
const passwordInputEl = document.getElementById('password');
const loginFormEl = document.getElementById('loginForm');
const emailErrorEl = document.getElementById('emailError');
const passwordErrorEl = document.getElementById('passwordError');


async function processLogin() {
    let email = emailInputEl.value;
    let password = passwordInputEl.value;
    let data = new URLSearchParams();
    data.append("email", email);
    data.append("password", password);

    fetch('/login', {
        method: 'post',
        body: data
    }).then(async function (response) {
        let json = await response.json()

        if (json.result === false) {
            passwordErrorEl.innerText = json.error;
            passwordErrorEl.style.display = 'block';
            passwordInputEl.value = '';
            emailInputEl.value = '';
        } else {
            passwordErrorEl.innerText = '';
            passwordErrorEl.style.display = 'none';
            window.location = '/home'
        }
    }).catch(() => showError("Error", "Login failed for unknown reason."));
}

function showError(element, error) {
    element.innerText = error;
    element.style.display = 'block';
    setTimeout(function () {
        element.innerText = '';
        element.style.display = 'none';
    }, 2000);
}

function hideError(element) {
    element.innerText = '';
    element.style.display = 'none';
}


function validateEmail() {
    if (emailInputEl.value.length === 0) {
        showError(emailErrorEl, "Email can't be empty.");
        return false;
    } else if (!validateEmailAddress(emailInputEl.value)) {
        showError(emailErrorEl, "Email isn't valid.")
    } else {
        hideError(emailErrorEl);
        return true;
    }
}

function validatePassword() {
    if (passwordInputEl.value.length === 0) {
        showError(passwordErrorEl, "Password can't be empty.");
        return false;
    } else {
        hideError(passwordErrorEl);
        return true;
    }
}

async function handleLogin(event) {
    event.preventDefault();

    let emailRes = validateEmail();
    let passwordRes = validatePassword();

    if (emailRes && passwordRes) {
        await processLogin();
    }
}


loginFormEl.addEventListener('submit', handleLogin);
