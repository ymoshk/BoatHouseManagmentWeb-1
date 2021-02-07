export function getRowInTable(id, rowElements, rowNumber) {
    let tr = document.createElement("tr");
    let th = document.createElement("th");
    th.scope = "row";
    th.innerText = rowNumber;
    tr.appendChild(th);

    rowElements.forEach(element => {
        let td = document.createElement("td");
        td.appendChild(element);
        tr.appendChild(td);
    });

    let actionButtons = createActionButton(id);
    tr.appendChild(actionButtons);

    return tr;
}

export function createEmptyTable(colNames) {
    let res = document.createElement("div");
    let table = document.createElement("table");
    res.appendChild(table);
    table.className += "table table-striped";

//build the head
    let head = document.createElement("thead");
    let tr = document.createElement("tr");
    tr.id = "tableHead"
    let indexCol = document.createElement("th");
    indexCol.scope = "col";
    indexCol.innerText = "#"
    indexCol.style.fontWeight = "bold";
    tr.appendChild(indexCol);
    head.appendChild(tr);
    colNames.forEach(name => {
        let th = document.createElement("th");
        th.scope = "col";
        th.innerText = name;
        th.style.fontWeight = "bold";
        tr.appendChild(th);
    });

    let actions = document.createElement("th");
    actions.scope = "col";
    actions.innerText = "Actions"
    actions.style.fontWeight = "bold";
    tr.appendChild(actions);

    table.appendChild(head);

//build tbody:
    let tbody = document.createElement("tbody");
    tbody.className += "table-striped";
    tbody.id = "tableBody";
    table.appendChild(tbody);

    return res;
}

// build action buttons
function createTableButton(id, tooltip, colorClass, iconClass) {
    let div = document.createElement("div");
    div.className += "col-6";
    let span = document.createElement("span");
    span.className += "tooltip";
    span.innerText = tooltip;
    let tableBtn = document.createElement("button");
    tableBtn.className += "btn";
    tableBtn.className += "btn-block";
    tableBtn.className += colorClass;
    tableBtn.value = id;

    let icon = document.createElement("i");
    icon.className += iconClass;

    tableBtn.appendChild(icon);

    div.appendChild(tableBtn);
    div.appendChild(span);

    return div;
}

function createActionButton(id) {
    let updateBtn = createTableButton(id, "Click to update", "btn-secondary",
        "fa fa-pencil-square-o");

    let deleteBtn = createTableButton(id, "Click to delete", "btn-danger",
        "fa fa-trash-o");

    let div = document.createElement("div");
    div.className += "row";
    div.className += "col-12";
    div.appendChild(deleteBtn);
    div.appendChild(updateBtn);

    let td = document.createElement("td");
    td.appendChild(div);

    return td;
}