document.addEventListener("DOMContentLoaded", function () {
    createTable();
});
const tableContainer = document.getElementById("tableContainer");


function buildRowerTableElements(rower) {
    let res = [];
    res.push(document.createTextNode(rower.name));
    res.push(document.createTextNode(rower.email));
    res.push(document.createTextNode(rower.phone));
    res.push(document.createTextNode(rower.age.toString()));
    res.push(document.createTextNode(getRankFromInt(rower.rank)));
    res.push(rower.isAdmin ? getCheckedIcon() : getUnCheckedIcon());

    return res;
}

async function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        fetch("/rowers/index/getRowers", {
            method: 'get'
        }).then(async function (response) {
            let rowers = await response.json();
            rowers = rowers.rowers;
            if (rowers.length !== 0) {
                let names = ["Name", "Email", "Phone", "Age", "Rank", "Is Admin"];
                let table = tables.createEmptyTable(names)
                tableContainer.appendChild(table);
                let i = 1;
                rowers.forEach((rower) => {
                    let tableElements = buildRowerTableElements(rower);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(rower.serialNumber, tableElements, i++, onDelete, onEdit, onInfo));
                })
            } else {
                tableContainer.appendChild(getNoDataEl());
            }
        }).catch(() => handleErrors());

    });
}

async function deleteRower(serialNumber, deleteRowerArgs) {
    let data = new FormData();
    data.append("serialNumber", serialNumber);
    data.append("args", deleteRowerArgs);

    await fetch('/rowers/delete', {
        method: 'post',
        body: data
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (!resAsJson.isSuccess) {
            showError(resAsJson.data[0]);
        } else {
            showSuccess("Rower successfully removed");
            setTimeout((function () {
                window.location.refresh(true);
            }, timeOutTime));
        }
    });
}

async function onDelete(serialNumber) {
    let reqData = new FormData();
    reqData.append("serialNumber", serialNumber);
    let deleteRowerArgs = {};
    deleteRowerArgs.shouldDeleteBoats = false;
    deleteRowerArgs.shouldDeleteRower = true;

    await fetch('/rowers/delete/info', {
        method: 'post',
        body: reqData,
        headers: {'Content-Type': 'application/json'}
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (!resAsJson.isSuccess) {
            showError(resAsJson.data[0]);
        } else {
            let data = resAsJson;

            if (data[0] === true) {
                deleteRowerArgs.shouldDeleteBoats =
                    await showAreYouSureMessage(
                        "This rower has private boats, are you want to delete them to?");
            }

            if (data[1] === true) {
                deleteRowerArgs.shouldDeleteRower =
                    await showAreYouSureMessage(
                        "The rower you want to delete participates in club activities. " +
                        "He will be removed from these activities, are you sure?");
            }
        }
    });

    await deleteRower(serialNumber, deleteRowerArgs);
}

function onEdit(serialNumber) {
    alert("Edit " + serialNumber);
}

function onInfo(serialNumber) {
    alert("INFO " + serialNumber);
}