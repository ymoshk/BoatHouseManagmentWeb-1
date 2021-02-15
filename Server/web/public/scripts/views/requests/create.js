let maxNumOfRowers = 0;
let otherRowersValue = 0;
let overLoadResult = false;
let newWeeklyActivity = false;
const weeklyActivityContainerEl = document.getElementById("weeklyActivityContainer");
const boatTypeSelectEl = document.getElementById("boatTypes");
const maxRowersLabel = document.getElementById("maxRowersAllowed");
const othersRowerLabel = document.getElementById("otherRowersCount");
const mainRowerEl = document.getElementById("mainRower");
const otherRowersEl = document.getElementById("otherRowers");
const activityDateEl = document.getElementById("activityDate");
const formEl = document.getElementById("createRequestForm");
const errorsEl = document.getElementById("errors");


document.addEventListener("DOMContentLoaded", function () {
    initTime();
    initBoatTypes();
    initMainRower();
    $("#boatTypes").on("change", boatTypeChangedEventHandler);
    $("#otherRowers").on("change", otherRowersChangedEventHandler);
    initOtherRowers();
    maxRowersLabel.innerText = "0";
    mainRowerEl.addEventListener("change", mainRowerChangedEventHandler)
    formEl.addEventListener('submit', createRequest);
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
            mainRowerSerial: mainRowerEl.value,
            activityDate: activityDateEl.value,
            isNewWeeklyActivity: newWeeklyActivity.toString(),
            startTime: startTimeValue,
            endTime: endTimeValue,
            weeklyActivityId: weeklyActivityId,
            boatTypes: selectExtractor("boatTypes"),
            otherRowers: selectExtractor("otherRowers")
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
                showErrorsInUnOrderedListEl(json.data, errorListEl);
            }
        });

    } else {
        showErrorsInUnOrderedListEl(errors, errorsEl);
    }
}

function validateForm() {
    let errors = [];

    if (mainRowerEl.value === "none") {
        errors.push("You must select a main rower for the request.")
    }

    if (selectExtractor(boatTypeSelectEl).length <= 0) {
        errors.push("You must select at least one boat type to create the request.")
    }

    if (overLoadResult) {
        errors.push("You've selected too many other rowers. You can't " +
            "select more rowers than the biggest selected boat type can contain");
    }

    if (!newWeeklyActivity) {
        let value = document.getElementById("weeklyActivity").value;

        if (value === "none") {
            errors.push("You must select a weekly activity");
        }
    } else {
        let startTime = document.getElementById("startTime").value;
        let endTime = document.getElementById("endTime").value;

        if (compareTime(startTime, endTime)) {
            errors.push("End time must be greater then the start time.")
        }
    }

    return errors;
}


async function mainRowerChangedEventHandler() {
    let selectedSerials = [];
    let finalSelects = [];
    otherRowersValue = 0;

    otherRowersEl.childNodes.forEach(function (option) {
        if (option.selected) {
            selectedSerials.push(option.value);
        }
    });

    selectedSerials.forEach(function (serial) {
        if (serial !== mainRowerEl.value) {
            finalSelects.push(serial);
        }
    });

    otherRowersEl.querySelectorAll('option').forEach(function (option) {
        otherRowersEl.removeChild(option);
    })
    await initOtherRowers();
    let othersSelect2 = $('#otherRowers');
    othersSelect2.val(finalSelects);
    othersSelect2.trigger('change');
}

function checkOverloadRowers() {
    if (otherRowersValue > maxNumOfRowers) {
        overLoadResult = true;
        maxRowersLabel.style.color = "#FF0000";
        othersRowerLabel.style.color = "#FF0000";

    } else {
        overLoadResult = false;
        maxRowersLabel.style.color = "";
        othersRowerLabel.style.color = "";
    }
}

function initBoatTypes() {
    getSimilarTypesFromServer().then(function (response) {
        if (response.types !== undefined && response.types.length > 0) {
            response.types.forEach(function (type) {
                let boatTypeOptionEl = buildBoatTypeOptionEl(type);
                boatTypeOptionEl.setAttribute('maxRowers', type.numOfRowers);
                boatTypeSelectEl.appendChild(boatTypeOptionEl);
            })
        }
    });
}

function otherRowersChangedEventHandler() {
    otherRowersValue = 0;
    otherRowersEl.childNodes.forEach(function (option) {
        if (option.selected) {
            otherRowersValue++;
        }
    });
    othersRowerLabel.innerText = otherRowersValue + " / ";
    checkOverloadRowers();
}

function initMainRower() {
    getRowersFromServer().then(function (rowers) {
        rowers.forEach(function (rower) {
            mainRowerEl.appendChild(buildRowerOptionEl(rower));
        });
    });
}

async function initOtherRowers() {
    let mainRowerSerialNumber = mainRowerEl.value;
    await getRowersFromServer(function (rower) {
        return rower.serialNumber !== mainRowerSerialNumber
    }).then(function (rowers) {
        rowers.forEach(function (rower) {
            otherRowersEl.appendChild(buildRowerOptionEl(rower));
        });
    });
    othersRowerLabel.innerText = otherRowersValue + " / ";
}

function boatTypeChangedEventHandler() {
    maxNumOfRowers = 1;
    boatTypeSelectEl.childNodes.forEach(function (option) {
        if (option.selected) {
            maxNumOfRowers = Math.max(maxNumOfRowers, option.getAttribute("maxRowers"));
        }

    });
    maxNumOfRowers--;
    maxRowersLabel.innerText = maxNumOfRowers.toString();
    checkOverloadRowers();
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