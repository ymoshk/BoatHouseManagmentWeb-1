const tableContainer = document.getElementById("tableContainer");
const filterEl = document.getElementById("filter");
const onlyApprovedEl = document.getElementById("onlyApproved");

let loggedInUser;
let bySpecificDayEl;
let requestsList;
let currentTimeFilter;

let currentSpecificDay = "";

document.addEventListener("DOMContentLoaded", function () {
    getLoggedInUser().then(function (user) {
        loggedInUser = user;
        createTable(undefined, onlyApprovedFilter);
        filterEl.addEventListener("change", filterElSelectionChangeEventHandler);
        onlyApprovedEl.addEventListener("click", onlyApprovedClickEventHandler);
        bySpecificDayEl = $("#bySpecificDay");
        bySpecificDayEl.on('change', applySpecificDayFilter);
    });
});

function specificDayFilter(req) {
    if (bySpecificDayEl.val() !== "") {
        return req.trainingDate === bySpecificDayEl.val();
    } else {
        return true;
    }
}

function applySpecificDayFilter() {
    if (bySpecificDayEl.val() !== currentSpecificDay) {
        currentSpecificDay = bySpecificDayEl.val();
        tableContainer.innerHTML = "";
        createTable(specificDayFilter, onlyApprovedFilter);
    }
}

function onlyApprovedClickEventHandler() {
    tableContainer.innerHTML = "";
    createTable(currentTimeFilter, onlyApprovedFilter);
}


function filterElSelectionChangeEventHandler() {
    if (filterEl.selectedIndex !== 3) {
        document.getElementById('specificDayFilterContainer').style.display = "none";
        document.getElementById('spaceDiv').style.display = "block";
        let filterToInvoke = getFilterFromSelectedIndex(filterEl.selectedIndex);
        tableContainer.innerHTML = "";

        createTable(filterToInvoke, onlyApprovedFilter);
    } else {
        document.getElementById('specificDayFilterContainer').style.display = "block";
        document.getElementById('spaceDiv').style.display = "none";
    }
}

function onlyApprovedFilter(req) {
    if (onlyApprovedEl.checked) {
        return req.isApproved;
    }
    return true;
}

function reqIsPrevWeekFilter(req) {
    let reqDate = getDateObjectFromString(req.trainingDate);
    let lastWeek = new Date(Date.now());

    minusDaysToDate(lastWeek, 7);

    return reqDate <= Date.now() && reqDate >= lastWeek;
}

function reqIsNextWeekFilter(req) {
    let reqDate = getDateObjectFromString(req.trainingDate);

    let nextWeek = new Date(Date.now());
    addDaysToDate(nextWeek, 7);

    return reqDate >= Date.now() && reqDate <= nextWeek;
}

function getFilterFromSelectedIndex(selectedIndex) {
    switch (selectedIndex) {
        case 1:
            return reqIsNextWeekFilter;
        case 2:
            return reqIsPrevWeekFilter;
    }

    return undefined;
}


function buildRequestTableRow(req) {
    let res = [];
    res.push(document.createTextNode(req.creationDate));
    res.push(document.createTextNode(req.trainingDate));
    res.push(document.createTextNode(req.requestCreator.name));
    res.push(document.createTextNode(req.weeklyActivity.startTime));
    res.push(document.createTextNode(req.weeklyActivity.endTime));
    res.push(!req.isApproved ? getUnCheckedIcon() : getCheckedIcon());

    if (loggedInUser.isAdmin) {
        res.push(getApproveBtn(req));
    }

    return res;
}

function approveBtnClickEventHandler(id) {
    alert("approve " + id);
}

function getApproveBtn(req) {
    let approveBtn = document.createElement("button");
    approveBtn.className += "btn-sm btn-success";
    approveBtn.style.padding
    approveBtn.style.textAlign = "center";
    approveBtn.style.paddingBottom = "5px";
    approveBtn.style.paddingTop = "5px";
    approveBtn.style.paddingRight = "10px";
    approveBtn.style.paddingLeft = "10px";
    approveBtn.style.fontSize = "15px";
    approveBtn.style.marginTop = "5px";
    approveBtn.style.marginBottom = "5px";
    let icon = document.createElement("i");
    if (req.isApproved || getDateObjectFromString(req.trainingDate) < new Date(Date.now())) {
        disableBtn(approveBtn);
    }

    icon.className += "fa fa-check-circle-o";
    approveBtn.appendChild(icon);
    approveBtn.setAttribute("title", "Approve Request");
    approveBtn.setAttribute("data-toggle", "tooltip");
    approveBtn.setAttribute("data-placement", "top");
    approveBtn.addEventListener("click", function (e) {
        e.preventDefault();
        approveBtnClickEventHandler(req.id)
    })

    return approveBtn;
}

function disableBtn(button) {
    button.disabled = true;
    button.style.opacity = "0.5";
    button.style.cursor = "not-allowed";
}

async function createTable(timeFilterToInvoke, isApprovedFilterToInvoke) {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        currentTimeFilter = timeFilterToInvoke;
        getRequestsFromServer().then(requests => {
            requestsList = timeFilterToInvoke === undefined ? requests.filter(isApprovedFilterToInvoke) :
                requests.filter(timeFilterToInvoke).filter(isApprovedFilterToInvoke);

            if (requestsList.length !== 0) {
                let names = ["Creation Date", "Activity Date", "Creator", "Start Time", "End Time", "Is Approved"];

                if (loggedInUser.isAdmin) {
                    names.push("Approve Request");
                }

                let table = tables.createEmptyTable(names);
                tableContainer.appendChild(table);
                let i = 1;
                requestsList.forEach((req) => {
                    let requestsTableRow = buildRequestTableRow(req);
                    let reqRow = tables.getRowInTable(req.id, requestsTableRow, i++, onDelete, onEdit, onInfo)
                    table.querySelector("#tableBody")
                        .appendChild(reqRow);

                });
            } else {
                tableContainer.appendChild(tables.getNoDataEl());
            }
        }).catch(() => handleErrors());
    });
}

function requestTrainingDateInFuture(request) {
    let trainingDate = getDateObjectFromString(request.trainingDate);

    return trainingDate >= new Date();
}

function onDelete(id) {
    const request = requestsList.filter(req => req.id === id)[0];
    if (request.isApproved && requestTrainingDateInFuture(request)) {
        showError("You can't to delete an approved request");
    } else {
        deleteRequest(request);
    }
}

async function deleteRequest(request) {
    let data = JSON.stringify({
        reqId: request.id
    });

    if (await showAreYouSureMessage("Are you sure that you want to delete this request?")) {

        await fetch('/requests/delete', {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }).then(async function (response) {
            let resAsJson = await response.json();
            if (resAsJson.isSuccess) {
                showSuccess("Request successfully removed!");
            } else {
                showError("Request removed failed!");
            }

            setTimeout(function () {
                window.location.reload();
            }, timeOutTime);
        });
    }
}

function onEdit(id) {
    const request = requestsList.filter(req => req.id === id)[0];
    if (request.isApproved) {
        showError("You can't to edit an approved request");
    } else {
        let data = JSON.stringify({
            id: id,
        });

        fetch('/requests/update/init', {
            method: 'post',
            body: data,
            headers: getPostHeaders()
        }).then(async function (response) {
            let resAsJson = await response.json();
            if (resAsJson.isSuccess) {
                window.location = "/requests/update";
            } else {
                showError(resAsJson.error);
            }
        });
    }
}

function onInfo(id) {
    const request = requestsList.filter(req => req.id === id)[0];
    createInfoPage(request).then(infoPageEl => {
        showInfoPopup(infoPageEl);
    });
}


function createInfoPage(request) {
    return import ("/public/scripts/views/requests/info.js").then((info) => {
        let infoEl = info.getInfoDiv();
        let mainRowerEl = infoEl.querySelector("#mainRower");
        let activityDateEl = infoEl.querySelector("#activityDate");
        let weeklyActivityDetailsEl = infoEl.querySelector("#activityDetails");
        let otherRowersEl = infoEl.querySelector("#otherRowers");
        let boatTypesEl = infoEl.querySelector("#boatTypes");

        mainRowerEl.value = getRowerStringFormat(request.mainRower);
        activityDateEl.value = request.trainingDate;
        weeklyActivityDetailsEl.value = getWeeklyActivityInfoString(request.weeklyActivity);
        initOtherRowersList(request.otherRowersList, otherRowersEl);
        initBoatTypes(request.boatTypesList, boatTypesEl);

        let duplicateBtn = infoEl.querySelector("#duplicateBtn");
        if (loggedInUser.isAdmin) {
            duplicateBtn.addEventListener("click", e => handleUpdateClick(e, request))
        } else {
            duplicateBtn.hidden = true;
        }


        return infoEl;
    });
}

function initBoatTypes(boatTypes, boatTypesEl) {
    boatTypesEl.value = "";

    if (boatTypes === undefined || boatTypes.length === 0) {
        boatTypesEl.value = "There aren't any other rowers in this request.";
    } else {
        boatTypes.forEach(function (type) {
            boatTypesEl.value += getBoatTypeFromNum(type) + "\n";
        });
    }
}

function handleUpdateClick(e, request) {
    e.preventDefault();
    duplicateRequestInServer(request.id).then(function (duplicated) {
        if (duplicated !== undefined) {
            showSuccess("Request successfully duplicated!");
        } else {
            showError("Duplicating request has failed.");
        }

        setTimeout(function () {
            window.location.reload();
        }, longTimeOutTime);
    }).catch(handleErrors);
}

function initOtherRowersList(otherRowers, otherRowersEl) {
    otherRowersEl.value = "";

    if (otherRowers === undefined || otherRowers.length === 0) {
        otherRowersEl.value = "There aren't any other rowers in this request.";
    } else {
        otherRowers.forEach(function (rower) {
            otherRowersEl.value += getRowerStringFormat(rower) + "\n";
        });
    }
}

function getWeeklyActivityInfoString(weeklyActivity) {
    return "Name: " + weeklyActivity.name + "\n" +
        "Start Time: " + weeklyActivity.startTime + "\n" +
        "End Time: " + weeklyActivity.endTime + "\n" +
        "Boat Type Description: " + weeklyActivity.boatTypeDescription + "\n";
}
