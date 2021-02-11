const formEl = document.getElementById("createWeeklyActivityForm");
const nameEl = document.getElementById("name");
const startTimeEl = document.getElementById("startTime");
const endTimeEl = document.getElementById("endTime");
const boatTypeEl = document.getElementById("boatType");
const errorsListEl = document.getElementById("errors");


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', validateForm);
});


async function validateForm(event) {
    event.preventDefault();
    let errors = [];
    let startTime = startTimeEl.value.toString();
    let endTime = endTimeEl.value.toString();

    if (compareTime(startTime, endTime)) {
        errors.push("End time should be after start time");
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
        boatType: boatTypeEl.selectedIndex.toString(),
        startTime: startTimeEl.value.toString(),
        endTime: endTimeEl.value.toString()
    })

    fetch("/weekly-activities/create", {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }
    ).then(async function (response) {
        let data = await response.json();

        if (data.isSuccess) {
            showSuccess("Weekly activity added successfully!");
        } else {
            showError("Weekly activity couldn't added");
        }

        setTimeout(function () {
            window.location = '/weekly-activities/index';
        }, timeOutTime);
    });
}