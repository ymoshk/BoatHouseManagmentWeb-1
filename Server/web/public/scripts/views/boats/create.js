const errorListEl = document.getElementById("errors");
const serialNumber = document.getElementById("serialNumber");
const nameEl = document.getElementById("name");
const ownersEl = document.getElementById("owners");
const formEl = document.getElementById("createBoatForm");

function fillInOwners() {
    getRowersFromServer().then(rowers => {
        rowers.forEach((rower) => {
           ownersEl.appendChild(buildOwnerOptionEl(rower));
        });
    });
}

document.addEventListener("DOMContentLoaded", function () {
    fillInOwners();
    formEl.addEventListener('submit', validateForm);
});

function validateForm(event) {
    event.preventDefault();
    alert("validate");
}