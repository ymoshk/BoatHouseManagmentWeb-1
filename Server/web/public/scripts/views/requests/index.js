document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");
let requestsList;


function buildRequestTableRow(req) {
    let res = [];
    res.push(document.createTextNode(req.creationDate));
    res.push(document.createTextNode(req.trainingDate));
    res.push(document.createTextNode(req.requestCreator.name));
    res.push(document.createTextNode(req.weeklyActivity.startTime));
    res.push(document.createTextNode(req.weeklyActivity.endTime));
    res.push(!req.isApproved ? getUnCheckedIcon() : getCheckedIcon());

    return res;
}

async function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        getRequestsFromServer().then(requests => {
            if (requests.length !== 0) {
                requestsList = requests;
                let names = ["Creation Date", "Activity Date", "Creator", "Start Time", "End Time", "Is Approved"];
                let table = tables.createEmptyTable(names);
                tableContainer.appendChild(table);
                let i = 1;
                requests.forEach((req) => {
                    let requestsTableRow = buildRequestTableRow(req);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(req.id, requestsTableRow, i++, onDelete, onEdit, onInfo));
                });
            } else {
                tableContainer.appendChild(tables.getNoDataEl());
            }
        }).catch(() => handleErrors());
    });
}

function onDelete(id){
alert("Delete " + id);
}

function onEdit(id){
    alert("Edit " + id);
}

function onInfo(id){
    alert("info" + id);
}