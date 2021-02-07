import ("/public/scripts/utils/tables.js").then((helpers) => {
    document.addEventListener("DOMContentLoaded", function () {
        createTable();

    });
});
const bodyTest = document.getElementById("bodyTest");

function createTable() {
    let names = ["col1", "col2", "col3", "col4", 'col5', 'col5'];
    let table = createEmptyTable(names)
    bodyTest.appendChild(table);
    let textEl = [];
    textEl.push(document.createTextNode("row1"));
    textEl.push(document.createTextNode("row2"));
    textEl.push(document.createTextNode("row3"));
    textEl.push(document.createTextNode("row4"));
    textEl.push(document.createTextNode("row5"));
    let btn = document.createElement("button");
    btn.innerText = "BTN TEST"
    // btn.style.width = "999px";
    // btn.style.height = "999px";
    textEl.push(btn);
    table.querySelector("#tableBody").appendChild(getRowInTable(1, textEl, 1));
    table.querySelector("#tableBody").appendChild(getRowInTable(2, textEl, 2));
    table.querySelector("#tableBody").appendChild(getRowInTable(3, textEl, 3));
}
