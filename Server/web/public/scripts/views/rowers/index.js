document.addEventListener("DOMContentLoaded", function () {
    createTable();
});

const bodyTest = document.getElementById("bodyTest");

function createTable() {
    import ("/public/scripts/utils/tables.js").then((tables) => {
        let names = ["col1", "col2", "col3", "col4", 'col5'];
        let table = tables.createEmptyTable(names)
        bodyTest.appendChild(table);
        let textEl = [];
        textEl.push(document.createTextNode("row1"));
        textEl.push(document.createTextNode("row2"));
        textEl.push(document.createTextNode("row3"));
        textEl.push(document.createTextNode("row4"));
        textEl.push(document.createTextNode("row5"));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(1, textEl, 1));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(2, textEl, 2));
        table.querySelector("#tableBody").appendChild(tables.getRowInTable(3, textEl, 3));
    });
}
