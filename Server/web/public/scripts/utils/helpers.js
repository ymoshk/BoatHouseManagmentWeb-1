const EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
const PHONE_PATTERN = "05\\d-\\d{7}";
const PHONE_WITHOUT_PATTERN = "05\\d{8}";

export function validateEmailAddress(email) {
    let reg = new RegExp(EMAIL_PATTERN);
    return reg.test(email);
}

export function validatePhone(phone) {
    let reg = new RegExp(PHONE_PATTERN);
    let regWO = new RegExp(PHONE_PATTERN);
    return reg.test(phone) || regWO.test(phone);
}

export function getBoostrapLinkEl(){
    const bootstrapLink = document.createElement("link");
    bootstrapLink.rel = "stylesheet";
    bootstrapLink.href = "https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css";

    return bootstrapLink;
}

export function getRowInTable(rowElements, rowNumber) {
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

    return tr;
}


export function createEmptyTable(colNames) {
    let res = document.createElement("div");
// res.appendChild(getBoostrapLinkEl()); //TODO - עושה בעיות
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
    tr.appendChild(indexCol);
    head.appendChild(tr);
    colNames.forEach(name => {
        let th = document.createElement("th");
        th.scope = "col";
        th.innerText = name;
        tr.appendChild(th);
    });

    table.appendChild(head);

//build tbody:
    let tbody = document.createElement("tbody");
    tbody.className += ".table-striped";
    tbody.id = "tableBody";
    table.appendChild(tbody);

    return res;
}

