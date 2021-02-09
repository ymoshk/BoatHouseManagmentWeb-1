document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");

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
                let names = ["Name", "Description", "Sea boat", "Wide", "Active", "Owner", "Code"];
                let table = tables.createEmptyTable(names);
                tableContainer.appendChild(table);

                let i = 1;
                boats.forEach((boat) => {
                    let boatTableRow = buildBoatTableRow(boat);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(boat.serialNumber, boatTableRow, i++, onDelete, onEdit, onInfo));
                });
            } else {
                tableContainer.appendChild(getNoDataEl());
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
    alert("Edit " + serialNumber);
}

function onInfo(serialNumber) {
    alert("Update " + serialNumber);
}