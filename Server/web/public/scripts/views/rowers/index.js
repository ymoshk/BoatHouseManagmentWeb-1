document.addEventListener("DOMContentLoaded", function () {
    createTable();
});
const tableContainer = document.getElementById("tableContainer");


function buildRowerTableElements(rower) {
    let res = [];
    res.push(document.createTextNode(rower.name));
    res.push(document.createTextNode(rower.email));
    res.push(document.createTextNode(rower.phone));
    res.push(document.createTextNode(rower.age.toString()));
    res.push(document.createTextNode(getRankFromInt(rower.rank)));
    res.push(rower.isAdmin ? getCheckedIcon() : getUnCheckedIcon());

    return res;
}

async function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        fetch("/rowers/index/getRowers", {
            method: 'get'
        }).then(async function(response){
            let rowers = await response.json();
            rowers = rowers.rowers;
            if(rowers.length !== 0) {
                let names = ["Name", "Email", "Phone", "Age", "Rank", "Is Admin"];
                let table = tables.createEmptyTable(names)
                tableContainer.appendChild(table);
                let i = 1;
                rowers.forEach((rower) => {
                    let tableElements = buildRowerTableElements(rower);
                    table.querySelector("#tableBody")
                        .appendChild(tables.getRowInTable(rower.serialNumber, tableElements, i++,onDelete, onEdit, onInfo));
                })
            }
            else{
                tableContainer.appendChild(getNoDataEl());
            }
        }).catch(() => handleErrors());

    });
}

async function onDelete(id){
    data = id;

    fetch('/rowers/delete', {
        method: 'get',
        body: data
    }).then(async function(response) {
        let resAsJson = await response.json();
        if(!resAsJson.isSuccess){
            showError("Rowe Dont Found")
        }

    });
}

function onEdit(id){
    alert( "Edit " + id);
}

function onInfo(id){
    alert("INFO " + id);
}