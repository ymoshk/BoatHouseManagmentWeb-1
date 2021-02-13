document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");
let weeklyActivities;


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
                weeklyActivities = activities;
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

function onDelete(id) {
    let data = JSON.stringify({
        serialNumber: id
    })

    fetch('/weekly-activities/delete', {
        method: "post",
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            showSuccess("Weekly activity successfully removed");
            setTimeout((function () {
                location.reload();
            }), timeOutTime);
        } else {
            showError("Deleting weekly activity failed, try again later.");
        }
    });
}

function onEdit(id) {
    let data = JSON.stringify({
        id: id,
    });

    fetch('/weekly-activities/update/init', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            window.location = "/weekly-activities/update";
        } else {
            showError(resAsJson.error);
        }
    });
}

function onInfo(id) {
    const weeklyActivity = weeklyActivities.filter(activity => activity.id === id)[0];
    createInfoPage(weeklyActivity).then(infoPageEl => {
        showInfoPopup(infoPageEl);
    });
}

function createInfoPage(weeklyActivity) {
    return import ("/public/scripts/views/weeklyActivities/info.js").then((info) => {
        let infoEl = info.getInfoDiv();
        let nameEl = infoEl.querySelector("#name");
        let boatTypeEl = infoEl.querySelector("#boatType");
        let startTimeEl = infoEl.querySelector("#startTime");
        let endTimeEl = infoEl.querySelector("#endTime");

        nameEl.value = weeklyActivity.name;
        boatTypeEl.value = weeklyActivity.boatTypeDescription !== undefined ? weeklyActivity.boatTypeDescription : "None";
        startTimeEl.value = weeklyActivity.startTime;
        endTimeEl.value = weeklyActivity.endTime;

        return infoEl;
    });
}


