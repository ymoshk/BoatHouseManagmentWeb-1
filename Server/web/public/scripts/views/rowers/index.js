document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const tableContainer = document.getElementById("tableContainer");


function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        let names = ["Name", "Email", "Phone", "Age", "Rank", "Is Admin"];
        let table = tables.createEmptyTable(names)
        tableContainer.appendChild(table);
        let textEl = [];
        textEl.push(document.createTextNode("row1"));
        textEl.push(document.createTextNode("row2"));
        textEl.push(document.createTextNode("row3"));
        textEl.push(document.createTextNode("row4"));
        textEl.push(document.createTextNode("row5"));
        textEl.push(document.createTextNode("row6"));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(1, textEl, 1));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(2, textEl, 2));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(3, textEl, 3));
    });
}