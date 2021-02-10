const menuItemsListEl = document.getElementById("mainMenu")
const currentYearElements = document.getElementsByClassName("currentYear");
const titleEl = document.getElementById("title");

document.addEventListener("DOMContentLoaded", function () {
    getNavMenuItems();
    setCurrentYear();
    initDateTmePicker();
});


function setCurrentYear() {
    let year = new Date().getFullYear();
    for (let i = 0; i < currentYearElements.length; i++) {
        currentYearElements.item(i).textContent = year;
    }

}

function buildMenuItem(itemObject) {
    let active = itemObject.isActive ? " active" : "";
    return '<li class = "nav-item' + active + '">' +
        '<a href="' + itemObject.href + '" class="nav-link" id="' + itemObject.id + '">' +
        '   <p class="simple-text">' + '<i class="' + itemObject.iconName + '"></i>' + itemObject.text + '</p>' +
        '</a>' +
        '</li>';
}

function buildMenu(menuItemsList) {
    let html = "";
    menuItemsList.forEach((menuItem) => {
        html += buildMenuItem(menuItem) + "\n"
    });
    menuItemsListEl.innerHTML = html;
}

function getNavMenuItems() {
    fetch("/layout", {
        method: 'get'
    }).then((response) => {
        response.json().then((value) => {
            titleEl.innerText = value.active;
            buildMenu(value.menu);
        });
    });
    // TODO - handle notifications
}

function initDateTmePicker() {
    $('.datetimepicker').datetimepicker({
        icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-chevron-up",
            down: "fa fa-chevron-down",
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-screenshot',
            clear: 'fa fa-trash',
            close: 'fa fa-remove'
        },
        format: 'DD/MM/YYYY',
        minDate: Date.now(),
    });
}
