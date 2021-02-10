document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");


function buildActivityRow(activity) {
    let res = [];
    res.push(document.createTextNode(activity.name));
    res.push(document.createTextNode(activity.startTime));
    res.push(document.createTextNode(activity.endTime));
    res.push(document.createTextNode(activity.boatTypeDescription));

    return res;
}

function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        getWeeklyActivitiesFromServer().then(activities => {
            if (activities.length !== 0) {
                let names = ["Name", "Start Time", "End Time", "Boat Type"];
                let table = tables.createEmptyTable(names);
                tableContainer.appendChild(table);

                let i = 1;

                activities.forEach(activity => {
                    let activityTableTow = buildActivityRow(activity);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(activity.id, activityTableTow, i++, onDelete, onEdit, onInfo));
                })
            } else {
                tableContainer.appendChild(tables.getNoDataEl());
            }
        });
    });
}

function onDelete(id){
    alert("DELETE " + id);
}

function onEdit(id){
    alert("Edit " + id);
}

function onInfo(id){
    alert("INFO " + id);
}


