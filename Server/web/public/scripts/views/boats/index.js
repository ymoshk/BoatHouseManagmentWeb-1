document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");
let boatsList;

function buildBoatTableRow(boat) {
    let res = [];
    res.push(document.createTextNode(boat.name));
    res.push(document.createTextNode(boat.description));
    res.push(boat.isSeaBoat ? getCheckedIcon() : getUnCheckedIcon());
    res.push(boat.isWide ? getCheckedIcon() : getUnCheckedIcon());
    res.push(boat.isDisable ? getUnCheckedIcon() : getCheckedIcon());
    res.push(document.createTextNode(boat.owner !== undefined ? boat.owner.name : "None"));
    res.push(document.createTextNode(boat.code));

    return res;
}

async function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        getBoatsFromServer().then(boats => {
            if (boats.length !== 0) {
                boatsList = boats;
                let names = ["Name", "Description", "Sea boat", "Wide Boat", "Active", "Owner", "Code"];
                let table = tables.createEmptyTable(names);
                tableContainer.appendChild(table);
                let i = 1;
                boats.forEach((boat) => {
                    let boatTableRow = buildBoatTableRow(boat);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(boat.serialNumber, boatTableRow, i++, onDelete, onEdit, onInfo));
                });
            } else {
                tableContainer.appendChild(tables.getNoDataEl());
            }
        }).catch(() => handleErrors());
    });
}


async function deleteBoat(serialNumber) {
    let data = JSON.stringify({
        serialNumber: serialNumber
    })

    fetch('/boats/delete', {
        method: "post",
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (!resAsJson.isSuccess) {
            showError("Error");
        } else {
            let data = resAsJson.data;
            if (data[0] === true) {
                showSuccess("Success!", "Boat successfully removed");
                setTimeout((function () {
                    location.reload();
                }), timeOutTime);
            }
        }
    });
}

async function onDelete(serialNumber) {
    let shouldBoatRemoved = true;

    let data = JSON.stringify({
        serialNumber: serialNumber
    })

    await fetch('/boats/delete/info', {
        method: "post",
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (!resAsJson.isSuccess) {
            showError("Error", resAsJson.data[0]);
        } else {
            let data = resAsJson.data;

            if (data[0] === false) {
                shouldBoatRemoved = await showAreYouSureMessage(
                    "The boat that you want to delete participates in club activities. " +
                    "Would you like to remove these activities?");
            }

            if (shouldBoatRemoved) {
                deleteBoat(serialNumber);
            }
        }
    })

}

function onEdit(serialNumber) {
    let data = JSON.stringify({
        serialNumber: serialNumber,
    });

    fetch('/boats/update/init', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            window.location = "/boats/update";
        } else {
            showError(resAsJson.error);
        }
    });
}

function onInfo(serialNumber) {
    const boat = boatsList.filter(boat => boat.serialNumber === serialNumber)[0];
    createInfoPage(boat).then(infoPageEl => {
        showInfoPopup(infoPageEl);
    });
}

function createInfoPage(boat){
    return import ("/public/scripts/views/boats/info.js").then((info) => {
        let infoEl = info.getInfoDiv();
        let serialNumberEl = infoEl.querySelector("#serialNumber");
        let nameEl = infoEl.querySelector("#name");
        let ownerEl = infoEl.querySelector("#owner");
        let isWideEl = infoEl.querySelector("#isWide");
        let isDisableEl = infoEl.querySelector("#isDisable");
        let isSeaBoatEl = infoEl.querySelector("#isSeaBoat");
        let boatTypeEl = infoEl.querySelector("#boatType");

        serialNumberEl.value = boat.serialNumber;
        nameEl.value = boat.name;
        isWideEl.checked = boat.isWide;
        isDisableEl.checked = boat.isDisable;
        isSeaBoatEl.checked = boat.isSeaBoat;
        ownerEl.value = boat.owner !== undefined ? boat.owner : "The boat has no owner";
        boatTypeEl.value = boat.description;

        return infoEl;
    });
}