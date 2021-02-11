const changePasswordEl = document.getElementById("changePassword");
const errorListEl = document.getElementById("errors");
const serialNumber = document.getElementById("serialNumber").value;
const formEl = document.getElementById("updateRowerForm");
// form elements
const nameEl = document.getElementById("name");
const phoneEl = document.getElementById("phone");
const ageEl = document.getElementById("age");
const emailEl = document.getElementById("email");
const notesEl = document.getElementById("notes");
const levelEl = document.getElementById("level");
const isAdminEl = document.getElementById("isAdmin");
const subscriptionExp = document.getElementById("expirationDate")


document.addEventListener("DOMContentLoaded", function () {
    changePasswordEl.addEventListener('click', (e) => changePasswordClickEventHandler(e));
    formEl.addEventListener('submit', (e) => updateRower(e));
});


function updateRower(e) {
    e.preventDefault();

    let data = JSON.stringify({
        serialNumber: serialNumber,
        email: emailEl.value,
        name: nameEl.value,
        age: ageEl.value,
        phone: phoneEl.value,
        expirationDate: subscriptionExp.value,
        level: levelEl.selectedIndex.toString(),
        isAdmin: isAdminEl.checked.toString(),
        notes: notesEl.value
    });

    fetch('/rowers/update', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()

        if (json.result === false) {
            showErrorsInUnOrderedListEl(json.error, errorListEl);
        } else {
            showSuccess("Rower successfully updated!");
            setTimeout(function () {
                window.location = '/rowers/index';
            }, 2000);
        }
    });


}

function changePasswordClickEventHandler(e) {
    Swal.fire({
        title: 'Change Password',
        icon: 'info',
        html: getChangePasswordInputs(),
        showCloseButton: true,
        showCancelButton: true,
        focusConfirm: false,
        confirmButtonText:
            '<i class="fa fa-check"></i> Save',
        confirmButtonAriaLabel: 'Change Password',
        cancelButtonText:
            '<i class="fa fa-close"></i> Cancel',
        cancelButtonAriaLabel: 'Cancel'
    }).then(function (result) {
        if (result.isConfirmed) {
            let passwordOneVal = document.getElementById("password1").value;
            let passwordTwoVal = document.getElementById("password2").value;

            if (passwordOneVal.length < passwordMinLen || passwordTwoVal.length < passwordMinLen) {
                showError("Failed", "The new password must be at least " + passwordMinLen + " characters long");
            } else {
                if (passwordOneVal === passwordTwoVal) {
                    updatePassword(passwordOneVal);
                } else {
                    showError("Failed", "Changing password failed because the passwords aren't match.");
                }
            }
        }
    });
}

function updatePassword(value) {
    let data = JSON.stringify({
        serialNumber: serialNumber,
        password: value,
    });

    fetch('/rowers/update/password', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()

        if (json.result === false) {
            showError("Failed!", json.error);
        } else {
            showSuccess("Success!", "Password successfully changed");
        }
    });
}

function getChangePasswordInputs() {

    return '<div class="row">' +
        '<div><ul style="color:red;" id = "changePasswordErrorList"></ul></div>' +
        '<div class="col-md-12">' +
        '<div class="form-group bmd-form-group">' +
        '<input id="password1" name="password1" type="password" class="form-control" ' +
        'minlength="4" required placeholder="Enter Password">' +
        '</div>' +
        '</div>' +
        '<div class="col-md-12">' +
        '<div class="form-group bmd-form-group">' +
        '<input id="password2" name="password2" type="password" class="form-control" ' +
        'minlength="4" required placeholder="Retype Password">' +
        '</div>' +
        '</div>' +
        '</div>';
}