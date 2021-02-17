const selectBoatTab = document.getElementById('selectBoatTab');
const addRowersTab = document.getElementById('addRowersTab');
const removeRowersTab = document.getElementById('removeRowersTab');
const infoTab = document.getElementById('infoTab');
const nextStepBtn = document.getElementById('nextStepBtn');
const backStepBtn = document.getElementById('backStepBtn');
const progressBar = document.getElementById('progressBar');
const errors = document.getElementById('errors');
const boatListStepOne = document.getElementById('boatSelection');
// let id = document.getElementById("requestId").value;
let id = "181ba131-b9ee-462a-8bd5-8e423920460a";
let currentStep = 0;
handleElementsByStep();


// Tabs functionality
nextStepBtn.addEventListener('click', function () {
    if (validateCurrentStep() && currentStep < 3) {
        currentStep++;
        findMiddleStep();
        handleElementsByStep();
    }
})


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
        let selectedBoat = document.getElementById('boatSelection').value;

        if (selectedBoat === "") {
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
    }

    return result;
}

function close() {
    alert("exit function")
    //TODO
}

function handleElementsByStep() {
    errors.innerHTML = "";

    if (currentStep === 0) {
        getBoatsForRequestFromServer(id).then(function (boats) {
            if (boats.length === 0) {
                showErrorsInUnOrderedListEl(["Couldn't find any boat that can be used for this request"], errors);
            } else {
                boats.forEach(function (boat) {
                    boatListStepOne.appendChild(buildBoatOptionEl(boat, false));
                });
            }
        });
    } else if (currentStep === 2) {


    } else if (currentStep === 3) {

    } else {

    }

    changeTabs()
}

function findMiddleStep() {
    getRequestsFromServer(id).then(function (req) {
        alert(req);
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