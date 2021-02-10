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
function getBoatsFromServer() {
    return fetch("/boats/index/getBoats", {
        method: 'get'
    }).then(boats = async function (response) {
        let resAsJson = await response.json();
        return resAsJson.boats;
    });
}

// TODO - add a filter
function getRowersFromServer() {
    return fetch("/rowers/index/getRowers", {
        method: 'get'
    }).then(async function (response) {
        let rowers = await response.json();
        return rowers.rowers;
    });
}