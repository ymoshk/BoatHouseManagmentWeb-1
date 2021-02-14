const errorListEl = document.getElementById("errors");
const id = document.getElementById("id").value;
const formEl = document.getElementById("updateWeeklyActivityForm");
// form elements
const nameEl = document.getElementById("name");
const startTimeEl = document.getElementById("startTime");
const endTimeEl = document.getElementById("endTime");
const boatTypeEl = document.getElementById("boatType");


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', (e) => updateWeeklyActivity(e));
});


function updateWeeklyActivity(e) {
    e.preventDefault();

    let data = JSON.stringify({
        id: id,
        name: nameEl.value,
        startTime: startTimeEl.value,
        endTime: endTimeEl.value,
        BoatTypeIndex: boatTypeEl.value
    });

    fetch('/weekly-activities/update', {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let json = await response.json()
        if (json.isSuccess) {
            showSuccess("Weekly activity successfully updated!");
            setTimeout(function () {
                window.location = '/weekly-activities/index';
            }, 2000);
        } else {
            showErrorsInUnOrderedListEl([json.data], errorListEl);
        }
    });
}

