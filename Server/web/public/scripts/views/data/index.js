const rowersTab = document.getElementById('rowersTab');
const rowersBtn = document.getElementById('rowersBtn');
const boatsTab = document.getElementById('boatsTab');
const boatsBtn = document.getElementById('boatsBtn');
const weeklyActivitiesTab = document.getElementById('weeklyActivitiesTab');
const weeklyActivitiesBtn = document.getElementById('weeklyActivitiesBtn');
const rowersFileUploadInput = document.getElementById('rowerFileInput');
const boatsFileUploadInput = document.getElementById('boatsFileInput');
const weeklyActivitiesFileUploadInput = document.getElementById('weeklyActivitiesFileInput');


// Tabs functionality

rowersBtn.addEventListener('click', function () {
    rowersTab.classList.add("active");
    rowersBtn.classList.add("active");
    boatsTab.classList.remove("active")
    boatsBtn.classList.remove("active");
    weeklyActivitiesTab.classList.remove("active");
    weeklyActivitiesBtn.classList.remove("active");
    hideResult();
})

boatsBtn.addEventListener('click', function () {
    rowersTab.classList.remove("active");
    rowersBtn.classList.remove("active");
    boatsTab.classList.add("active")
    boatsBtn.classList.add("active");
    weeklyActivitiesTab.classList.remove("active");
    weeklyActivitiesBtn.classList.remove("active");
    hideResult();
})

weeklyActivitiesBtn.addEventListener('click', function () {
    rowersTab.classList.remove("active");
    rowersBtn.classList.remove("active");
    boatsTab.classList.remove("active")
    boatsBtn.classList.remove("active");
    weeklyActivitiesTab.classList.add("active");
    weeklyActivitiesBtn.classList.add("active");
    hideResult();
})

// File upload functionality

document.getElementById('inputRowersContainer').addEventListener('click', function () {
    document.getElementById('rowerFileInput').click();
})

document.getElementById('inputBoatsContainer').addEventListener('click', function () {
    document.getElementById('boatsFileInput').click();
})

document.getElementById('inputWeeklyActivitiesContainer').addEventListener('click', function () {
    document.getElementById('weeklyActivitiesFileInput').click();
})

rowersFileUploadInput.addEventListener('change', function () {
    changeBtnText(this, 'selectRowersFileBtnText');
})

boatsFileUploadInput.addEventListener('change', function () {
    changeBtnText(this, 'selectBoatsFileBtnText');
})

weeklyActivitiesFileUploadInput.addEventListener('change', function () {
    changeBtnText(this, 'selectWeeklyActivitiesFileBtnText');
})

function changeBtnText(element, textEl) {
    let textElement = document.getElementById(textEl);
    if (element.value === "" || element.value === undefined) {
        textElement.innerText = "SELECT A FILE";
    } else {
        let index = element.value.toString().lastIndexOf('\\');
        if (index !== -1) {
            textElement.innerText = element.value.toString().substr(index + 1);
        }
    }
}

document.getElementById('exportRowersBtn').addEventListener('click', function () {
    exportHandler('rowers');
})

document.getElementById('exportBoatsBtn').addEventListener('click', function () {
    exportHandler('boats');
})

document.getElementById('exportWeeklyActivitiesBtn').addEventListener('click', function () {
    exportHandler('weekly-activities');
})

function exportHandler(type) {
    hideResult();
    let downloadLink = document.createElement('a');
    downloadLink.href = '/data/export/' + type;
    downloadLink.click();
    downloadLink.remove();
}

document.getElementById('importRowersBtn').addEventListener('click', function () {
    importHandler(rowersFileUploadInput, "rowers");
})

document.getElementById('importBoatsBtn').addEventListener('click', function () {
    importHandler(boatsFileUploadInput, "boats");
})

document.getElementById('importWeeklyActivitiesBtn').addEventListener('click', function () {
    importHandler(weeklyActivitiesFileUploadInput, "weekly-activities");
})

function importHandler(inputEl, modelName) {
    if (inputEl.value === "" || inputEl.value === undefined) {
        showError("You must select a file to upload.");
    } else if (!inputEl.value.toString().endsWith('.xml')) {
        showError("Please select a xml file only.");
    } else {
        toDeleteAll(modelName).then(function (toDelete) {
            importData(inputEl, toDelete, modelName);
        })
    }
}

async function toDeleteAll(modelName) {
    return await showAreYouSureMessage("Would you like to delete all the " + modelName + " from the system " +
        "before importing the file?")
}

function importData(inputEL, toDelete, modelName) {
    hideResult();
    let data = new FormData()
    data.append('file', inputEL.files[0])
    data.append('deleteAll', toDelete.toString())

    fetch('/data/import/' + modelName, {
        method: 'post',
        body: data,
    }).then(async function (response) {
        let resAsJson = await response.json();

        if (resAsJson.isSuccess) {
            showSuccess("You can now watch the results.");
            showResults(resAsJson.data)
        } else {
            showError(resAsJson.data);
        }
    });
}

function showResults(results) {
    document.getElementById("resultContainer").innerText = results;
    document.getElementById("resultsCard").style.display = "block";
    document.getElementById("resultTitle").innerText = new Date().toDateString();
    weeklyActivitiesFileUploadInput.value = "";
    boatsFileUploadInput.value = "";
    rowersFileUploadInput.value = "";
    document.getElementById("selectBoatsFileBtnText").innerText = "SELECT A FILE";
    document.getElementById("selectRowersFileBtnText").innerText = "SELECT A FILE";
    document.getElementById("selectWeeklyActivitiesFileBtnText").innerText = "SELECT A FILE";
}

function hideResult() {
    document.getElementById("resultContainer").innerText = "";
    document.getElementById("resultsCard").style.display = "none";
    document.getElementById("resultTitle").innerText = "";
}







