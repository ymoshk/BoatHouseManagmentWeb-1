const changePasswordEl = document.getElementById("changePassword");

document.addEventListener("DOMContentLoaded", function () {
    changePasswordEl.addEventListener('click', (e) => changePasswordClickEventHandler(e));
});

function changePasswordClickEventHandler(e) {
    Swal.fire({
        title: 'Change Password',
        //icon: 'warning',
        html: getChangePasswordInputs(),
        showCloseButton: true,
        showCancelButton: true,
        focusConfirm: false,
        confirmButtonText:
            '<i class="fa fa-thumbs-up"></i> Great!',
        confirmButtonAriaLabel: 'Thumbs up, great!',
        cancelButtonText:
            '<i class="fa fa-thumbs-down"></i>',
        cancelButtonAriaLabel: 'Thumbs down'
    })
}

function getChangePasswordInputs() {

    let result = '<div class="row">';

    result += '<div class="col-md-12">' +
        '<div class="form-group bmd-form-group">' +
        '<input id="password" name="password" type="password" class="form-control" ' +
        'minlength="4" required placeholder="Enter Password">' +
        ' </div>' +
        '< /div>';

    result += '<div class="col-md-12">' +
        '<div class="form-group bmd-form-group">' +
        '<input id="password2" name="password2" type="password" class="form-control" ' +
        'minlength="4" required placeholder="Retype Password">' +
        ' </div>' +
        '</div>';
    result += '</div>';
}