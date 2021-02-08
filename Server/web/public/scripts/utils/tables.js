export function getRowInTable(id, rowElements, rowNumber, onDelete, onUpdate, onEdit) {
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

    let actionButtons = createActionButton(id, onDelete, onUpdate, onEdit);
    tr.appendChild(actionButtons);
    tr.style.textAlign = "center";

    return tr;
}

export function createEmptyTable(colNames) {
    let res = document.createElement("div");
    let table = document.createElement("table");
    res.className += "table-responsive-sm";
    res.appendChild(table);
    table.className += "table-striped dataTable";

//build the head
    let head = document.createElement("thead");
    let tr = document.createElement("tr");
    tr.id = "tableHead"
    let indexCol = document.createElement("th");
    indexCol.scope = "col";
    indexCol.innerText = "#"
    indexCol.style.fontWeight = "bold";
    indexCol.style.textAlign = "center";
    indexCol.style.width = "5px";
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
    actions.style.width = "15%";
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
function createTableButton(id, tooltip, colorClass, iconClass, onClick) {
    let div = document.createElement("div");
    div.className += "col-md-4";
    let tableBtn = document.createElement("button");
    tableBtn.className += colorClass;
    tableBtn.style.textAlign = "center";
    tableBtn.style.paddingBottom = "5px";
    tableBtn.style.paddingTop = "5px";
    tableBtn.style.paddingRight = "10px";
    tableBtn.style.paddingLeft = "10px";
    tableBtn.style.fontSize = "15px";
    tableBtn.style.marginTop = "5px";
    tableBtn.style.marginBottom = "5px";
    tableBtn.setAttribute("title", tooltip);
    tableBtn.setAttribute("data-toggle", "tooltip");
    tableBtn.setAttribute("data-placement", "top");
    tableBtn.addEventListener("click", function () {
        onClick(id)
    });



    let icon = document.createElement("i");
    icon.className += iconClass;
    tableBtn.appendChild(icon);
    div.appendChild(tableBtn);

    return div;
}

function createActionButton(id, onDelete, onUpdate, onEdit) {
    let updateBtn = createTableButton(id, "Click to update", "btn-sm btn-secondary",
        "fa fa-pencil", onUpdate);

    let deleteBtn = createTableButton(id, "Click to delete", "btn-sm btn-danger",
        "fa fa-trash-o", onDelete);

    let infoBtn = createTableButton(id, "Click for more details", "btn-sm btn-info",
        "fa fa-info-circle", onEdit);

    let div = document.createElement("div");
    div.className += "row";
    div.appendChild(deleteBtn);
    div.appendChild(updateBtn);
    div.appendChild(infoBtn);

    let td = document.createElement("td");
    td.appendChild(div);

    return td;
}