const errorListEl = document.getElementById("errors");
const serialNumber = document.getElementById("serialNumber").value;
const formEl = document.getElementById("updateBoatForm");
// form elements
const optionalOwner = document.getElementById("owners");
const boatType = document.getElementById("boatType");


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', (e) => updateRower(e));
    insertOptionalRowers();
    insertOptionalTypes();
});

//
// function updateRower(e) {
//     e.preventDefault();
//
//     let data = JSON.stringify({
//         serialNumber: serialNumber,
//         email: emailEl.value,
//         name: nameEl.value,
//         age: ageEl.value,
//         phone: phoneEl.value,
//         expirationDate: subscriptionExp.value,
//         level: levelEl.selectedIndex.toString(),
//         isAdmin: isAdminEl.checked.toString(),
//         notes: notesEl.value,
//         boatsId: selectExtractor('privateBoats')
//     });
//
//     fetch('/rowers/update', {
//         method: 'post',
//         body: data,
//         headers: getPostHeaders()
//     }).then(async function (response) {
//         let json = await response.json()
//
//         if (json.result === false) {
//             showErrorsInUnOrderedListEl(json.error, errorListEl);
//         } else {
//             showSuccess("Rower successfully updated!");
//             setTimeout(function () {
//                 window.location = '/rowers/index';
//             }, 2000);
//         }
//     });
// }


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
                optionalOwner.appendChild(buildOwnerOptionEl(rower, selected));
            });
            if (!foundSelected) {
                optionalOwner.selectedIndex = 0;
            }
        } else {
            let notFoundEl = document.createElement('option');
            notFoundEl.disabled = true;
            notFoundEl.innerText = "Couldn't find any available rowers"
            optionalOwner.appendChild(notFoundEl);
        }
    })
}

function insertOptionalTypes() {
    getSimilarTypesFromServer(serialNumber).then(function (response) {
        if (response !== undefined && response.types.length > 0) {
            response.types.forEach(function (type) {
                boatType.appendChild(buildBoatTypeOptionEl(type));
            });
        } else {
            let notFoundEl = document.createElement('option');
            notFoundEl.disabled = true;
            notFoundEl.innerText = "Couldn't find a types";
            notFoundEl.selected = true;
            boatType.appendChild(notFoundEl);
        }
    })
}