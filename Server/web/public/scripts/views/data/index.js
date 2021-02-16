const importRowersForm = document.getElementById("importRowersForm");
const importBoatsForm = document.getElementById("importBoatsForm");
const importWeeklyActivitiesForm = document.getElementById("importWeeklyActivitiesForm");

const rowersFileInputEl = document.getElementById("rowerFileInput");
const boatsFileInputEl = document.getElementById("fileInput");
const weeklyActivitiesFileInputEl = document.getElementById("fileInput");
let deleteAllRowers;
let deleteAllBoats;
let deleteAllWeeklyActivities;


document.addEventListener("DOMContentLoaded", function () {
    importRowersForm.addEventListener("submit", importRowersClickEventHandler);
    importBoatsForm.addEventListener("submit", importBoatsClickEventEentHandler);
    importWeeklyActivitiesForm.addEventListener("submit", importWeeklyActivitiesEventHandler);

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
    // let data = new FormData()
    // data.append('file', rowersFileInputEl.files[0]);
    // data.append('deleteAll', deleteAllRowers.toString());

    fetch('/data/import/rowers', {
        method: 'post',
        body: rowersFileInputEl.files[0],
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

function importWeeklyActivitiesEventHandler() {

}

function importBoatsClickEventEentHandler() {

}

function boatsFileInputChangeEventHandler() {

}

function weeklyActivitiesFileInputChangeEventHandler() {

}





