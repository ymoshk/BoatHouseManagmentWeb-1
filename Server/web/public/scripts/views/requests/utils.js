let maxNumOfRowers = 0;
let otherRowersValue = 0;
let overLoadResult = false;

const boatTypeSelectEl = document.getElementById("boatTypes");
const maxRowersLabel = document.getElementById("maxRowersAllowed");
const othersRowerLabel = document.getElementById("otherRowersCount");
const mainRowerEl = document.getElementById("mainRower");
const otherRowersEl = document.getElementById("otherRowers");
const activityDateEl = document.getElementById("activityDate");
const errorsEl = document.getElementById("errors");

document.addEventListener("DOMContentLoaded", function () {
    initMainRower();
    initBoatTypes();
    initOtherRowers();
    $("#boatTypes").on("change", boatTypeChangedEventHandler);
    $("#otherRowers").on("change", otherRowersChangedEventHandler);
    mainRowerEl.addEventListener("change", mainRowerChangedEventHandler);
    maxRowersLabel.innerText = "0";
});

function validateForm(checkWeeklyActivity) {

    let errors = [];

    if (mainRowerEl.value === "none") {
        errors.push("You must select a main rower for the request.")
    }

    if (selectExtractor('boatTypes').length <= 0) {
        errors.push("You must select at least one boat type to create the request.")
    }

    if (overLoadResult) {
        errors.push("You've selected too many other rowers. You can't " +
            "select more rowers than the biggest selected boat type can contain");
    }

    if (getDateObjectFromString(activityDateEl.value) <= new Date()) {
        errors.push("Activity date must be greater then today.");
    }

    if (!newWeeklyActivity) {
        let value = document.getElementById("weeklyActivity").value;

        if (value === "none" && checkWeeklyActivity !== false) {
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
