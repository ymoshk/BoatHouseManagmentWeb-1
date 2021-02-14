let maxNumOfRowers = 1;
let otherRowersValue = 0;
const weeklyActivityContainerEl = document.getElementById("weeklyActivityContainer");
const boatTypeSelectEl = document.getElementById("boatTypes");
const maxRowersLabel = document.getElementById("maxRowersAllowed");
const othersRowerLabel = document.getElementById("otherRowersCount");
const mainRowerEl = document.getElementById("mainRower");
const otherRowersEl = document.getElementById("otherRowers");

document.addEventListener("DOMContentLoaded", function () {
    initTime();
    initBoatTypes();
    $("#boatTypes").on("change", boatTypeChangedEventHandler);
    $("#otherRowers").on("change", otherRowersChangedEventHandler);
    initOtherRowers();
    maxRowersLabel.innerText = "0";
});

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
}


function initOtherRowers() {
    let mainRowerSerialNumber = mainRowerEl.value;
    getRowersFromServer(function (rower) {
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
    maxRowersLabel.innerText = maxNumOfRowers.toString();
}


function initTime() {
    getWeeklyActivitiesFromServer().then(function (weeklyActivities) {
        if (weeklyActivities !== undefined && weeklyActivities.length > 0) {
            insertWeeklyActivitySelect(weeklyActivities);
        } else {
            createWeeklyActivityInputs();
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