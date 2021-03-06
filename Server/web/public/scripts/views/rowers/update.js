const changePasswordEl = document.getElementById("changePassword");
const errorListEl = document.getElementById("errors");
let serialNumber;
let redirectUrl = '/rowers/index';
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
const privateBoatsEl = document.getElementById("privateBoats");


document.addEventListener("DOMContentLoaded", function () {
    changePasswordEl.addEventListener('click', (e) => changePasswordClickEventHandler(e));
    serialNumber = document.getElementById("serialNumber").value;
    formEl.addEventListener('submit', (e) => updateRower(e));
    insertOptionalBoats();
    insertPrivateBoats();
});

function updateSerialNumber(sn){
    serialNumber = sn;
}

function updateRedirectUrl(newUrl){
    redirectUrl = newUrl;
}


function insertOptionalBoats() {
    getBoatsFromServer(boat => boat.owner === undefined).then(function (boats) {
        return insertBoatOptions(boats, false);
    })
}

function insertPrivateBoats() {
    let serial = serialNumber;
    getBoatsFromServer(boat => boat.owner !== undefined && boat.owner.serialNumber === serial).then(function (boats) {
        return insertBoatOptions(boats, true);
    })
}

function insertBoatOptions(boats, selected) {
    if (boats !== false && boats.length > 0) {
        boats.forEach(function (boat) {
            privateBoatsEl.appendChild(buildBoatOptionEl(boat, selected));
        });
    }
}


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
        notes: notesEl.value,
        boatsId: selectExtractor('privateBoats')
    });

    fetch('/rowers/update', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()

        if (json.isSuccess) {
            showSuccess("Rower successfully updated!");
            setTimeout(function () {
                window.location = redirectUrl;
            }, 2000);
        } else {
            showErrorsInUnOrderedListEl(json.data, errorListEl);
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
                showError("The new password must be at least " + passwordMinLen + " characters long", "Failed");
            } else {
                if (passwordOneVal === passwordTwoVal) {
                    updatePassword(passwordOneVal);
                } else {
                    showError("Changing password failed because the passwords aren't match.", "Failed");
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

        if (json.isSuccess) {
            showSuccess("Password successfully changed");
        } else {
            showError(json.error);
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