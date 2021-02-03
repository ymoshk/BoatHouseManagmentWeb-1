import ("./utils/helpers.js").then((helpers) => {

    const emailInputEl = document.getElementById('email');
    const passwordInputEl = document.getElementById('password');
    const loginFormEl = document.getElementById('loginForm');
    const emailErrorEl = document.getElementById('emailError');
    const passwordErrorEl = document.getElementById('passwordError');


    async function processLogin() {
        let email = emailInputEl.value
        let password = passwordInputEl.value
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
            } else {
                passwordErrorEl.innerText = '';
                passwordErrorEl.style.display = 'none';
                window.location = '/home'
            }
        });
    }


    function validateEmail() {

        if (emailInputEl.value.length === 0) {
            emailErrorEl.innerText = "Email can't be empty.";
            emailErrorEl.style.display = 'block';
            return false;
        } else if (!helpers.validateEmailAddress(emailInputEl.value)) {
            emailErrorEl.innerText = "Email isn't valid.";
            emailErrorEl.style.display = 'block';
        } else {
            emailErrorEl.innerText = '';
            emailErrorEl.style.display = 'none';
            return true;
        }
    }

    function validatePassword() {
        if (passwordInputEl.value.length === 0) {
            passwordErrorEl.innerText = "Password can't be empty.";
            passwordErrorEl.style.display = 'block';
            return false;
        } else {
            passwordErrorEl.innerText = '';
            passwordErrorEl.style.display = 'none';
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
});