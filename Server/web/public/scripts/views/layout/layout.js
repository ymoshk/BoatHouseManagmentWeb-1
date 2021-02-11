const menuItemsListEl = document.getElementById("mainMenu")
const currentYearElements = document.getElementsByClassName("currentYear");
const titleEl = document.getElementById("title");

document.addEventListener("DOMContentLoaded", function () {
    getNavMenuItems();
    setCurrentYear();

    $('.datepicker2').datepicker({
        uiLibrary: 'bootstrap4',
        format: 'dd/mm/yyyy'
    });
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
        if (menuItem.isActive) {
            titleEl.innerText = menuItem.text.toUpperCase();
        }

        html += buildMenuItem(menuItem) + "\n"
    });
    menuItemsListEl.innerHTML = html;
}

function getNavMenuItems() {
    fetch("/layout", {
        method: 'get'
    }).then((response) => {
        response.json().then((value) => {
            buildMenu(value.menu);
        });
    });
    // TODO - handle notifications
}


function initDateTmePicker() {
    $('.timepicker').datetimepicker({
        icons: {
            time: "fa fa-clock-o",
            up: "fa fa-chevron-up",
            down: "fa fa-chevron-down",
            previous: 'fa fa-chevron-left',
            next: 'fa fa-chevron-right',
            today: 'fa fa-screenshot',
            clear: 'fa fa-trash',
            close: 'fa fa-remove',
            pick12HourFormat: false
        },

        format: 'LT',
    });
}
