const id = document.getElementById("id").value;
const formEl = document.getElementById("updateRequestForm");
const weeklyActivityEl = document.getElementById("weeklyActivity");
let weeklyActivitiesExists = false;


document.addEventListener("DOMContentLoaded", function () {
    formEl.addEventListener('submit', updateRequest);
    initWeeklyActivities();
    initSelections();
    // more init functionality is located in request/utils.js file.
});


function initSelections() {
    getRequestsFromServer().then(function (requests) {
        requests.forEach(function (request) {
            if (request.id === id) {
                mainRowerEl.querySelectorAll('option').forEach(function (option) {
                    if (option.value === request.mainRower.serialNumber) {
                        option.selected = true;
                    }
                });

                weeklyActivityEl.querySelectorAll('option').forEach(function (option) {
                    if (option.value === request.weeklyActivity.id) {
                        option.selected = true;
                    }
                });

                let requestOtherRowersIds = [];
                request.otherRowersList.forEach(rower => requestOtherRowersIds.push(rower.serialNumber));

                let otherRowers = $('#otherRowers');
                otherRowers.val(requestOtherRowersIds);
                otherRowers.trigger('change');

                let boatTypes = $('#boatTypes');
                boatTypes.val(request.boatTypesList);
                boatTypes.trigger('change');
            }
        })
    })
}

function initWeeklyActivities() {
    getWeeklyActivitiesFromServer().then(function (weeklyActivities) {
        if (weeklyActivities !== undefined && weeklyActivities.length > 0) {
            weeklyActivitiesExists = true;
            document.getElementById("weeklyActivityNoneOption").innerText = "No Change";
            document.getElementById("weeklyActivityNoneOption").disabled = false;
            weeklyActivities.forEach(activity => {
                let option = buildWeeklyActivityOptionEl(activity);
                weeklyActivityEl.appendChild(option);
            })
        }
    });
}


function updateRequest(e) {
    e.preventDefault();
    newWeeklyActivity = false // must be false to use the global validateForm function in update page
    let errors = validateForm(false);

    if (errors.length === 0) {

        let data = JSON.stringify({
            id: id,
            mainRower: mainRowerEl.value,
            activityDate: activityDateEl.value,
            weeklyActivity: weeklyActivityEl.value,
            boatTypes: selectExtractor("boatTypes"),
            otherRowers: selectExtractor("otherRowers")
        });

        fetch('/requests/update', {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }).then(async function (response) {
            let json = await response.json()
            if (json.isSuccess) {
                showSuccess("Request successfully updated!");
                setTimeout(function () {
                    window.location = '/requests/index';
                }, timeOutTime);
            } else {
                showErrorsInUnOrderedListEl(json.data, errorsEl);
            }
        });
    } else {
        showErrorsInUnOrderedListEl(errors, errorsEl);
    }
}

