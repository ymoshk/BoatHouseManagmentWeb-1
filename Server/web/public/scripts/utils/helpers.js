const EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
const PHONE_PATTERN = "^05\\d-\\d{7}$|^05\\d{8}$";


const timeOutTime = 2000; //2 sec

function validateEmailAddress(email) {
    let reg = new RegExp(EMAIL_PATTERN);
    return reg.test(email);
}

function validatePhone(phone) {
    let reg = new RegExp(PHONE_PATTERN);
    return reg.test(phone);
}

function getRankFromInt(num) {
    switch (num) {
        case 0:
            return "Beginner";
        case 1:
            return "Avarage";
        case 2:
            return "Professional";
    }

    return "None";
}

function getCheckedIcon() {
    let res = document.createElement("i");
    res.className += " fa fa-check";

    return res;
}

function getUnCheckedIcon() {
    let res = document.createElement("i");
    res.className += " fa fa-times";

    return res;
}

function handleErrors() {
    showError("Error", "Action failed due to an unknown reason");
    setTimeout(() => window.location = "/home", timeOutTime)
}

function getNoDataEl() {
    let res = document.createElement("h3");
    res.innerText = "No data found";

    return res;
}

function getPostHeaders() {
    return new Headers({
        'Content-Type': 'application/json'
    });
}