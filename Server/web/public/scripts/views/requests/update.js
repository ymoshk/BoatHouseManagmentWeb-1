const errorListEl = document.getElementById("errors");
const id = document.getElementById("id").value;
const formEl = document.getElementById("updateRequestForm");
// form elements
const mainRowerEl = document.getElementById("mainRower");
const activityDate = document.getElementById("activityDate");
const weeklyActivityEl = document.getElementById("weeklyActivity");

document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', updateRequest);
});


function updateRequest(e) {
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

