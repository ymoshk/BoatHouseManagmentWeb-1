const errorListEl = document.getElementById("errors");
const serialNumberEl = document.getElementById("serialNumber");
const nameEl = document.getElementById("name");
const ownersEl = document.getElementById("owners");
const formEl = document.getElementById("createBoatForm");
const boatTypeSelectEl = document.getElementById("boatType");
const isWideEl = document.getElementById("isWide");
const isDisableEl = document.getElementById("isDisable");
const isSeaBoatEl = document.getElementById("isSeaBoat");

document.addEventListener("DOMContentLoaded", function () {
    fillInOwners();
    insertBoatTypes();
    formEl.addEventListener('submit', validateForm);
});

function insertBoatTypes() {
    getSimilarTypesFromServer().then(function (response) {
        if (response.types !== undefined && response.types.length > 0) {
            response.types.forEach(function (type) {
                boatTypeSelectEl.appendChild(buildBoatTypeOptionEl(type));
            })
        }
    });
}

function fillInOwners() {
    getRowersFromServer().then(rowers => {
        rowers.forEach((rower) => {
            ownersEl.appendChild(buildRowerOptionEl(rower));
        });
    });
}

async function addBoat() {

    let data = JSON.stringify({
        serialNumber: serialNumberEl.value,
        name: nameEl.value,
        ownerSerialNumber: ownersEl.value,
        isWide: isWideEl.checked.toString(),
        isSeaBoatEl: isSeaBoatEl.checked.toString(),
        isDisable: isDisableEl.checked.toString(),
        boatType: boatTypeSelectEl.value.toString()
    });

    fetch("/boats/create", {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }
    ).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            showSuccess("Boat successfully added!");

            setTimeout(function () {
                window.location = '/boats/index';
            }, timeOutTime);
        } else {
            let errors = resAsJson.data;
            if (errors.length !== 0) {
                showErrorsInUnOrderedListEl(errors, errorListEl);
            } else {
                showError("Boat creation failed due to unknown error.");
                setTimeout(function () {
                    window.location = '/boats/index';
                }, timeOutTime);
            }
        }
    })
}

function validateForm(event) {
    event.preventDefault();
    let errors = [];

    if (boatTypeSelectEl.value === "-1") {
        errors.push("You must to select a boat type");
    }

    if (errors.length === 0) {
        addBoat();
    } else {
        showErrorsInUnOrderedListEl(errors, errorListEl);
    }
}