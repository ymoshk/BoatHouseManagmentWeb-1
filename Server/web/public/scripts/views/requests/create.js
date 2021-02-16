let newWeeklyActivity = false;
const weeklyActivityContainerEl = document.getElementById("weeklyActivityContainer");

const formEl = document.getElementById("createRequestForm");


document.addEventListener("DOMContentLoaded", function () {
    initTime();
    formEl.addEventListener('submit', createRequest);
    // more init functionality is located in request/utils.js file.
});


function createRequest(e) {
    e.preventDefault();
    errorsEl.innerHTML = "";
    let errors = validateForm();
    let startTimeValue;
    let endTimeValue;
    let weeklyActivityId;

    if (newWeeklyActivity) {
        startTimeValue = document.getElementById("startTime").value;
        endTimeValue = document.getElementById("endTime").value;
    } else {
        weeklyActivityId = document.getElementById("weeklyActivity").value;
    }

    if (errors.length === 0) {

        let data = JSON.stringify({
            mainRowerSerial: mainRowerEl.value,
            activityDate: activityDateEl.value,
            isNewWeeklyActivity: newWeeklyActivity.toString(),
            startTime: startTimeValue,
            endTime: endTimeValue,
            weeklyActivityId: weeklyActivityId,
            boatTypes: selectExtractor('boatTypes'),
            otherRowers: selectExtractor('otherRowers')
        });

        fetch('/requests/create', {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }).then(async function (response) {
            let json = await response.json()

            if (json.isSuccess) {
                showSuccess("Request successfully created!");
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


function initTime() {
    getWeeklyActivitiesFromServer().then(function (weeklyActivities) {
        if (weeklyActivities !== undefined && weeklyActivities.length > 0) {
            insertWeeklyActivitySelect(weeklyActivities);
            newWeeklyActivity = false;
        } else {
            createWeeklyActivityInputs();
            newWeeklyActivity = true;
        }
    });
}

function insertWeeklyActivitySelect(weeklyActivities) {
    let selectEl = getWeeklyActivitySelect();
    weeklyActivities.forEach(activity => {
        let option = buildWeeklyActivityOptionEl(activity);
        selectEl.querySelector("#weeklyActivity").appendChild(option);
    });

    weeklyActivityContainerEl.appendChild(selectEl);
}

function createWeeklyActivityInputs() {
    let startTime = createTimeEl("startTime", "Start Time");
    let endTime = createTimeEl("endTime", "End  Time");
    weeklyActivityContainerEl.innerHTML += startTime;
    weeklyActivityContainerEl.innerHTML += endTime;
}

function createTimeEl(name, label) {
    return "<div class=\"col-md-6\">\n" +
        "    <div class=\"form-group bmd-form-group\">\n" +
        "        <label for=\" " + name + "\" class=\"label-control\">" + label + "</label>\n" +
        "        <input style='text-align: left' id=\"" + name + "\" type=\"time\" class=\"form-control\" value=\"00:00\"/>\n" +
        "    </div>\n" +
        "</div>"
}


function getWeeklyActivitySelect() {
    let res = document.createElement("div");
    res.className += "col-md-12";
    res.innerHTML +=
        "        <div class=\"form-group bmd-form-group\">\n" +
        "            <label class=\"label-control\">Weekly Activity</label>" +
        "            <select id=\"weeklyActivity\" name=\"weeklyActivity\" class=\"form-control selectpicker\"\n" +
        "                    data-style=\"btn btn-link\">\n" +
        "                <option value=\"none\" disabled selected>Select Weekly Activity</option>\n" +
        "            </select>\n" +
        "        </div>\n";

    return res;
}