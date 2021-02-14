const errorListEl = document.getElementById("errors");
const serialNumber = document.getElementById("serialNumber").value;
const formEl = document.getElementById("updateBoatForm");
// form elements
const optionalOwnerEl = document.getElementById("owners");
const boatTypeEl = document.getElementById("boatType");
const isWideEl = document.getElementById("isWide");
const isSeaBoatEl = document.getElementById("isSeaBoat");
const isDisabledEl = document.getElementById("isDisable");
const nameEl = document.getElementById("name");


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', (e) => updateBoat(e));
    insertOptionalRowers();
    insertOptionalTypes();
});


function updateBoat(e) {
    e.preventDefault();

    let data = JSON.stringify({
        serialNumber: serialNumber,
        name: nameEl.value,
        owner: optionalOwnerEl.value,
        boatType: boatTypeEl.value.toString(),
        isWide: isWideEl.checked.toString(),
        isSeaBoat: isSeaBoatEl.checked.toString(),
        isDisabled: isDisabledEl.checked.toString(),
    });

    fetch('/boats/update', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()

        if (json.isSuccess) {
            showSuccess("Boat successfully updated!");
            setTimeout(function () {
                window.location = '/boats/index';
            }, 2000);
        } else {
            showErrorsInUnOrderedListEl(json.data, errorListEl);
        }
    });
}


function insertOptionalRowers() {
    let serial = serialNumber;
    let foundSelected = false;
    getRowersFromServer().then(function (rowers) {
        if (rowers !== false && rowers.length > 0) {
            rowers.forEach(function (rower) {
                let selected = rower.boatsId.includes(serial)
                if (selected) {
                    foundSelected = true;
                }
                optionalOwnerEl.appendChild(buildRowerOptionEl(rower, selected));
            });
            if (!foundSelected) {
                optionalOwnerEl.selectedIndex = 0;
            }
        }
    })
}

function insertOptionalTypes() {
    getSimilarTypesFromServer(serialNumber).then(function (response) {
        if (response !== undefined && response.types.length > 0) {
            response.types.forEach(function (type) {
                boatTypeEl.appendChild(buildBoatTypeOptionEl(type));
            });
        } else {
            let notFoundEl = document.createElement('option');
            notFoundEl.disabled = true;
            notFoundEl.innerText = "Couldn't find a types";
            notFoundEl.selected = true;
            boatTypeEl.appendChild(notFoundEl);
        }
    })
}