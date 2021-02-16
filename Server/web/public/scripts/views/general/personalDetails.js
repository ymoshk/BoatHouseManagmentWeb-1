const editBtnEl = document.getElementById("editBtn");
let loggedInUserIsAdmin;

function initElements(loggedInUser) {
    changePasswordEl.value = loggedInUser
    updateSerialNumber(loggedInUser.serialNumber);
    nameEl.value = loggedInUser.name;
    phoneEl.value = loggedInUser.phone;
    ageEl.value = loggedInUser.age;
    emailEl.value = loggedInUser.email;
    notesEl.value = loggedInUser.notes.length !== 0 ? loggedInUser.notes : "";
    levelEl.selectedIndex = loggedInUser.rank;
    isAdminEl.checked = loggedInUser.isAdmin;
    subscriptionExp.value = loggedInUser.expirationDate;
    notesEl.value = notesEl.value.replaceAll(",", "\n");
    updateRedirectUrl("/personal-details");
}

function editClickEventListener(e) {
    e.preventDefault();
    let allElements = document.querySelectorAll("[disabled]");

    for (let i = 0, l = allElements.length; i < l; ++i) {
        if (allElements[i].id !== "isAdmin" && allElements[i].id !== "privateBoats" && allElements[i].id !== "expirationDate") {
            allElements[i].disabled = false;
        } else if ((allElements[i].id === "privateBoats" || allElements[i].id === "expirationDate") && loggedInUserIsAdmin) {
            allElements[i].disabled = false;
        }
    }
}

document.addEventListener("DOMContentLoaded", function () {
    getLoggedInUser().then(loggedInUser => {
        if (loggedInUser !== undefined) {
            initElements(loggedInUser);
            loggedInUserIsAdmin = loggedInUser.isAdmin;
            insertPrivateBoats();
            editBtnEl.addEventListener('click', (e) => editClickEventListener(e));
        } else {
            showError("Try again later");
            setTimeout((function () {
                window.location.replace("/home");
            }), timeOutTime);
        }
    });
});
