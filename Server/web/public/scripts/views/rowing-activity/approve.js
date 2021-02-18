const selectBoatTab = document.getElementById('selectBoatTab');
const addRowersTab = document.getElementById('addRowersTab');
const removeRowersTab = document.getElementById('removeRowersTab');
const infoTab = document.getElementById('infoTab');
const nextStepBtn = document.getElementById('nextStepBtn');
const backStepBtn = document.getElementById('backStepBtn');
const progressBar = document.getElementById('progressBar');
const errors = document.getElementById('errors');
// step 1 elements
const boatListStepOne = document.getElementById('boatSelection');

//step 2 elements
const requestRowersSelectEl = document.getElementById("requestRowersSelect");
const deletedRowersSelectEl = document.getElementById("deleteRowersSelect");
const deleteRowerBtn = document.getElementById("deleteFromRequestBtn");
const addToRequestBtn = document.getElementById("addToRequestBtn");
const howManyLeftToRemoveTextEl = document.getElementById("howManyLeftText");

// let id = document.getElementById("requestId").value;
let id = "e07753de-1668-4979-8e59-981243b31bac";
let currentStep = 0;
let relevantBoats;
let theBoat;

document.addEventListener("DOMContentLoaded", function () {
    //TODO delete
    handleElementsByStep();
})


// Tabs functionality
nextStepBtn.addEventListener('click', function () {
    validateCurrentStep().then(function (result) {
        if (result) {
            changeStep();
        }
    });
})

async function changeStep() {
    if (currentStep === 0) {
        findMiddleStep();
    } else {
        currentStep = 3;
        handleElementsByStep();
    }

}

backStepBtn.addEventListener('click', function () {
    if (currentStep > 0) {
        currentStep = 0;
        handleElementsByStep();
    } else {
        close();
    }
})

async function validateCurrentStep() {
    let result = false;
    if (currentStep === 0) {
        let selectedBoatId = document.getElementById('boatSelection').value;
        theBoat = boatsList.filter(boat => boat.serialNumber === selectedBoatId)[0];

        if (selectedBoatId === "") {
            showErrorsInUnOrderedListEl(["You must select a boat to move next."], errors)
            setTimeout(function () {
                errors.innerHTML = "";
            }, longTimeOutTime);
        } else {
            await removeUnavailableRowers().then(function (isSuccess) {
                if (isSuccess) {
                    result = true;
                }
            });
        }
    } else {
        result = true;
    }

    return result;
}

function close() {
    alert("exit function")
    //TODO
}

function initSelectList() {

}

function changeHowManyLeft(howManyLeft) {

}

function handleTooManyRowers() {
    getRequestsFromServer(id).then(function (request) {
        alert(JSON.stringify(theBoat));
        // let howManyLeft = request.otherRowers.length + 1;
        // changeHowManyLeft(howManyLeft);
        // initSelectList();
    });
}

function handleElementsByStep() {
    errors.innerHTML = "";

    if (currentStep === 0) {
        getBoatsForRequestFromServer(id).then(function (boats) {
            boatsList = boats;
            if (boats.length === 0) {
                showErrorsInUnOrderedListEl(["Couldn't find any boat that can be used for this request"], errors);
            } else {
                boats.forEach(function (boat) {
                    boatListStepOne.appendChild(buildBoatOptionEl(boat, false));
                });
            }
        });
    } else if (currentStep === 1) {
        handleTooManyRowers();

    } else if (currentStep === 2) {
        // not enough

    } else if (currentStep === 3) {
        // finish

    } else {

    }

    changeTabs();
}

function findMiddleStep() {
    checkRowersCountStatus().then(function (result) {
        if (result === 0) {
            currentStep = 3;
        } else if (result > 0) {
            currentStep = 1;
        } else {
            currentStep = 2;
        }

        handleElementsByStep();
    });
}

function checkRowersCountStatus() {
    let data = JSON.stringify({
        id: id,
        boatId: boatListStepOne.value
    })

    return fetch("/requests/find-rowers-status", {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            return await resAsJson.data;
        } else {
            showError(resAsJson.data);
            setTimeout(function () {
                location.reload();
            }, longTimeOutTime);
        }
    });
}

function removeUnavailableRowers() {
    let data = JSON.stringify({
        id: id
    })

    return fetch("/requests/removeUnavailableRowers", {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            return true;
        } else {
            showError(resAsJson.data);
            setTimeout(function () {
                location.reload();
            }, longTimeOutTime);
        }
    });

}

function changeTabs() {
    if (currentStep === 0) {
        selectBoatTab.classList.add("active");
        addRowersTab.classList.remove("active");
        removeRowersTab.classList.remove("active");
        infoTab.classList.remove("active");
        nextStepBtn.disabled = false;
        backStepBtn.disabled = false;
        nextStepBtn.style.cursor = "default"
        progressBar.style.width = "33.33%";
        backStepBtn.innerText = "Close"


    } else if (currentStep === 1) {
        selectBoatTab.classList.remove("active");
        addRowersTab.classList.add("active");
        removeRowersTab.classList.remove("active");
        infoTab.classList.remove("active");
        allowBothButtons();

    } else if (currentStep === 2) {
        selectBoatTab.classList.remove("active");
        addRowersTab.classList.remove("active");
        removeRowersTab.classList.add("active");
        infoTab.classList.remove("active");
        allowBothButtons();

    } else {
        selectBoatTab.classList.remove("active");
        addRowersTab.classList.remove("active");
        removeRowersTab.classList.remove("active");
        infoTab.classList.add("active");
        backStepBtn.remove();
        nextStepBtn.disabled = false;
        nextStepBtn.style.cursor = "default"
        nextStepBtn.innerText = "close";
        progressBar.style.width = "100%";
    }
}

function allowBothButtons() {
    backStepBtn.disabled = false;
    nextStepBtn.disabled = false;
    backStepBtn.style.cursor = "default";
    nextStepBtn.style.cursor = "default";
    progressBar.style.width = "66.66%";
    backStepBtn.innerText = "Back";
}

function getBoatsForRequestFromServer(requestId) {
    let data = JSON.stringify({
        id: requestId
    })

    return fetch("/collectors/boats/getRelevantBoatsForRequest", {
        method: 'post',
        body: data,
        headers: getPostHeaders()
    }).then(async function (response) {
        let resAsJson = await response.json();
        if (resAsJson.isSuccess) {
            return resAsJson.data.boats;
        } else {
            handleErrors();
        }
    });
}