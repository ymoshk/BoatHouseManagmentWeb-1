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
    tr.style.textAlign = "center";

    return tr;
}

export function createEmptyTable(colNames) {
    let res = document.createElement("div");
    let table = document.createElement("table");
    res.className += "table-responsive";
    res.appendChild(table);
    table.className += "table-striped";

//build the head
    let head = document.createElement("thead");
    let tr = document.createElement("tr");
    tr.id = "tableHead"
    let indexCol = document.createElement("th");
    indexCol.scope = "col";
    indexCol.innerText = "#"
    indexCol.style.fontWeight = "bold";
    indexCol.style.textAlign = "center";
    tr.appendChild(indexCol);
    head.appendChild(tr);
    colNames.forEach(name => {
        let th = document.createElement("th");
        th.scope = "col";
        th.innerText = name;
        th.style.fontWeight = "bold";
        th.style.textAlign = "center";
        tr.appendChild(th);
    });

    let actions = document.createElement("th");
    actions.scope = "col";
    actions.innerText = "Actions"
    actions.style.fontWeight = "bold";
    actions.style.textAlign = "center";
    actions.style.width = "10%";
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
    let toolT = document.createElement("div");
    toolT.className += "ripple-container";
    let tableBtn = document.createElement("button");
    tableBtn.className += colorClass;
    tableBtn.value = id;
    tableBtn.style.textAlign = "center";
    tableBtn.style.paddingBottom = "5px";
    tableBtn.style.paddingTop = "5px";
    tableBtn.style.paddingRight = "10px";
    tableBtn.style.paddingLeft = "10px";
    // tableBtn.setAttribute("rel", "tooltip");
    // tableBtn.setAttribute("data-original-title", tooltip);
    tableBtn.setAttribute("title", tooltip);

    let icon = document.createElement("i");
    icon.className += iconClass;
    tableBtn.appendChild(icon);
    tableBtn.appendChild(toolT);
    div.appendChild(tableBtn);

    return div;
}

function createActionButton(id) {
    let updateBtn = createTableButton(id, "Click to update", "btn-sm btn-secondary",
        "fa fa-pencil-square-o");

    let deleteBtn = createTableButton(id, "Click to delete", "btn-sm btn-danger",
        "fa fa-trash-o");

    let div = document.createElement("div");
    div.className += "row col-12";
    div.appendChild(deleteBtn);
    div.appendChild(updateBtn);

    let td = document.createElement("td");
    td.appendChild(div);

    return td;
}