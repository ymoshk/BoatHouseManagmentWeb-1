const errorListEl = document.getElementById("errors");
const serialNumberEl = document.getElementById("serialNumber");
const nameEl = document.getElementById("name");
const ownersEl = document.getElementById("owners");
const formEl = document.getElementById("createBoatForm");
const boatTypeSelectEl = document.getElementById("boatType");
const isWideEl = document.getElementById("isWide");
const isDisableEl = document.getElementById("isDisable");
const isSeaBoatEl = document.getElementById("isSeaBoat");


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

async function addBoat() {
    let data = JSON.stringify({
        serialNumber: serialNumberEl.value,
        name: nameEl.value,
        ownerSerialNumber: ownersEl[ownersEl.selectedIndex].className,
        isWide: isWideEl.checked.toString(),
        isSeaBoatEl: isSeaBoatEl.checked.toString(),
        isDisable: isDisableEl.checked.toString(),
        boatType: boatTypeSelectEl.selectedIndex.toString()
    });

    fetch("/boats/create", {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }
    ).then(async function (response) {
        let resAsJson = await response.json();
        if(resAsJson.isSuccess){
            showSuccess("Boat successfully added!");

            setTimeout(function () {
                window.location = '/boats/index';
            }, timeOutTime);
        }else{
            let errors = resAsJson.data;
            if(errors.length !== 0){
                showErrorsInUnOrderedListEl(errors, errorListEl);
            }
            else {
                showError("Something happened, try again later");
                setTimeout(function () {
                    window.location = '/boats/index';
                }, timeOutTime);
            }
        }
    }).catch()
}

function validateForm(event) {
    event.preventDefault();
    let errors = [];

    if (boatTypeSelectEl.selectedIndex.valueOf() === 0) {
        errors.push("You must to pick a boat type");
    }

    if (errors.length === 0) {
        addBoat();
    } else {
        showErrorsInUnOrderedListEl(errors, errorListEl);
    }
}