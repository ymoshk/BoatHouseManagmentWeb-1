function getRankFromInt(num) {
    switch (num) {
        case 0:
            return "Beginner";
        case 1:
            return "Average";
        case 2:
            return "Professional";
    }

    return "None";
}

function getBoatTypeFromNum(num) {
    let types = [
        "Single",
        "Due (2X)",
        "Due with coxwain (2X+)",
        "Due Single oar (2-)",
        "Due Single oar with coxwain (2+)",
        "Four (4X)",
        "Four with coxwain (4X+)",
        "Four single oar (4-)",
        "Four single oar with coxwain (4+)",
        "Eight (8X+)",
        "Eight single oar (8+)",
    ];

    return num >= 0 ? types[num] : "None";
}


function buildOwnerOptionEl(rower, selected) {
    let res = document.createElement("option");
    res.className = rower.serialNumber;
    res.innerText = rower.name + ' (' + rower.email + ', ' + rower.phone + ')';
    res.value = rower.serialNumber;

    if (selected !== undefined) {
        res.setAttribute("selected", "selected");
    }

    return res;
}

function buildBoatOptionEl(boat, selected) {
    let res = document.createElement("option");
    res.className = boat.serialNumber;
    res.innerText = boat.name + ' (' + boat.code + ')';
    res.value = boat.serialNumber;

    if (selected) {
        res.setAttribute('selected', 'selected');
    }

    return res;
}

function buildBoatTypeOptionEl(type) {
    let res = document.createElement("option");
    res.innerText = type.name;
    res.value = type.index;

    if (res.select) {
        res.setAttribute('selected', 'selected');
    }
    return res;
}


function getBoatsFromServer(filter) {
    return fetch("/boats/index/getBoats", {
        method: 'get'
    }).then(async function (response) {
        let resAsJson = await response.json();
        let boats = resAsJson.boats;

        if (filter !== undefined) {
            boats = boats.filter(boat => filter(boat));
        }

        return boats;
    });
}


function getRowersFromServer(filter) {
    return fetch("/rowers/index/getRowers", {
        method: 'get'
    }).then(async function (response) {
        let rowers = await response.json();

        if (filter !== undefined) {
            rowers.rowers = rowers.rowers.filter(rower => filter(rower));
        }

        return rowers.rowers;
    });
}

function getWeeklyActivitiesFromServer() {
    return fetch("/weekly-activities/index/getWeeklyActivities", {
        method: 'get'
    }).then(async function (response) {
        let activities = await response.json();

        return activities.activities;
    });
}


function getSimilarTypesFromServer(serialNumber) {
    let data = JSON.stringify({
        serialNumber: serialNumber
    });
    return fetch("/collectors/SimilarBoatTypes", {
        data: data,
        method: 'post',
        headers: getPostHeaders(),
    }).then(async function (response) {
        let activities = await response.json();

        return activities.activities;
    });
}

