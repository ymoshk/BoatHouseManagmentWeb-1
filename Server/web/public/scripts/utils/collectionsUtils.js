function getRankFromInt(num) {
    switch (num) {
        case 0:
            return "Beginner";
        case 1:
            return "Avarage";
        case 2:
            return "Professional";
    }

    return "None";
}


function buildOwnerOptionEl(rower) {
    let res = document.createElement("option");
    res.className = rower.serialNumber;
    res.innerText = rower.name + ' (' + rower.email + ', ' + rower.phone + ')';

    return res;
}

function buildBoatOptionEl(boat) {
    let res = document.createElement("option");
    res.className = boat.serialNumber;
    res.innerText = boat.name + ' (' + boat.code + ')';

    return res;
}


// TODO - add a filter
function getBoatsFromServer(filter) {
    return fetch("/boats/index/getBoats", {
        method: 'get'
    }).then(boats = async function (response) {
        let resAsJson = await response.json();
        let boats = resAsJson.boats;

        if(filter !== undefined){
            boats = boats.filter(boat => filter(boat));
        }

        return boats;
    });
}

// TODO - add a filter
function getRowersFromServer(filter) {
    return fetch("/rowers/index/getRowers", {
        method: 'get'
    }).then(async function (response) {
        let rowers = await response.json();

        if(filter !== undefined){
            rowers.rowers = rowers.rowers.filter(rower => filter(rower));
        }

        return rowers.rowers;
    });
}

function getWeeklyActivitiesFromServer(){
    return fetch("/weekly-activities/index/getWeeklyActivities", {
        method: 'get'
    }).then(async function (response) {
        let activities = await response.json();

        return activities.activities;
    });
}