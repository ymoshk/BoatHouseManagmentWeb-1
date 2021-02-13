const formEl = document.getElementById("createWeeklyActivityForm");
const nameEl = document.getElementById("name");
const startTimeEl = document.getElementById("startTime");
const endTimeEl = document.getElementById("endTime");
const boatTypeEl = document.getElementById("boatType");
const errorsListEl = document.getElementById("errors");


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', validateForm);
    insertBoatTypes();
});


function insertBoatTypes() {
    getSimilarTypesFromServer().then(function (response) {
        if (response.types !== undefined && response.types.length > 0) {
            response.types.forEach(function (type) {
                boatTypeEl.appendChild(buildBoatTypeOptionEl(type));
            })
        }
    });
}


async function validateForm(event) {
    event.preventDefault();
    let errors = [];
    let startTime = startTimeEl.value.toString();
    let endTime = endTimeEl.value.toString();

    if (compareTime(startTime, endTime)) {
        errors.push("End time must be after the start time");
    }

    if (errors.length !== 0) {
        showErrorsInUnOrderedListEl(errors, errorsListEl);
    } else {
        addWeeklyActivity();
    }
}

function addWeeklyActivity() {
    let data = JSON.stringify({
        name: nameEl.value,
        boatType: boatTypeEl.value.toString(),
        startTime: startTimeEl.value.toString(),
        endTime: endTimeEl.value.toString()
    })

    fetch("/weekly-activities/create", {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }
    ).then(async function (response) {
        let result = await response.json();

        if (result.isSuccess) {
            showSuccess("Weekly activity successfully added!");
        } else {
            showError("Adding weekly activity failed.");
        }

        setTimeout(function () {
            window.location = '/weekly-activities/index';
        }, timeOutTime);
    });
}