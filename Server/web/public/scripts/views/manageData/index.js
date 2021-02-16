const importRowersForm = document.getElementById("importRowersForm");
const importBoatsForm = document.getElementById("importBoatsForm");
const importWeeklyActivitiesForm = document.getElementById("importWeeklyActivitiesForm");
const exportRowersBtn = document.getElementById("exportRowersBtn");
const exportBoatsBtn = document.getElementById("exportBoatsBtn");
const exportWeeklyActivitiesBtn = document.getElementById("exportWeeklyActivitiesBtn");
const rowersFileInputEl = document.getElementById("rowerFileInput");
const boatsFileInputEl = document.getElementById("fileInput");
const weeklyActivitiesFileInputEl = document.getElementById("fileInput");
let deleteAllRowers;
let deleteAllBoats;
let deleteAllWeeklyActivities;


document.addEventListener("DOMContentLoaded", function () {
    importRowersForm.addEventListener("submit", importRowersClickEventHandler);
    exportRowersBtn.addEventListener("click", exportRowersClickEventHandler);
    importBoatsForm.addEventListener("submit", importBoatsClickEventEentHandler);
    exportBoatsBtn.addEventListener("click", exportBoatsClickEventEentHandler);
    importWeeklyActivitiesForm.addEventListener("submit", importWeeklyActivitiesEventHandler);
    exportWeeklyActivitiesBtn.addEventListener("click", exportWeeklyActivitiesEventHandler);

    rowersFileInputEl.addEventListener("change", rowersFileInputChangeEventHandler);
    boatsFileInputEl.addEventListener("change", boatsFileInputChangeEventHandler);
    weeklyActivitiesFileInputEl.addEventListener("change", weeklyActivitiesFileInputChangeEventHandler);
});

async function importRowersClickEventHandler(e) {
    e.preventDefault();
    deleteAllRowers = await showAreYouSureMessage("You want to overwrite the current data?");

    rowersFileInputEl.click();
}

function rowersFileInputChangeEventHandler() {
    let data = new FormData()
    data.append('file', rowersFileInputEl.files[0])
    data.append('deleteAll', deleteAllRowers.toString())

    fetch('/data/import/rowers', {
        method: 'post',
        body: data,
        headers: new Headers({'Content-Type': 'multipart/form-data'})
    }).then(function (response) {
        let resAsJson = response.json();

        if (resAsJson.isSuccess) {
            showSuccess(resAsJson.data);
        } else {
            showError(resAsJson.data);
        }
    });
}

function exportRowersClickEventHandler() {

}

function exportBoatsClickEventEentHandler() {

}

function importWeeklyActivitiesEventHandler() {

}

function importBoatsClickEventEentHandler() {

}

function exportWeeklyActivitiesEventHandler() {

}


function boatsFileInputChangeEventHandler() {

}

function weeklyActivitiesFileInputChangeEventHandler() {

}





