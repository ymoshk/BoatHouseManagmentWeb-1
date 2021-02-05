const menuItemsListEl = document.getElementById("mainMenu")
const currentYearElements = document.getElementsByClassName("currentYear");

document.addEventListener("DOMContentLoaded", function () {
    getNavMenuItems();
    setCurrentYear();
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
    menuItemsList.forEach((menuItem) => html += buildMenuItem(menuItem) + "\n");
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


