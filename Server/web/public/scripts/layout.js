document.addEventListener("DOMContentLoaded", function () {
    getNavMenuItems();
});

const menuItemsListEl = document.getElementById("mainMenu")


function buildMenuItem(itemObject) {
    let active = itemObject.isActive ? " active" : "";
    let html = '<li class = "nav-item' + active + '">' +
        '<a href="#" class="nav-link" id="' + itemObject.id + '">' +
        '   <p class="simple-text">' + '<i class="' + itemObject.iconName + '"></i>' + itemObject.text + '</p>' +
        '</a>' +
        '</li>';

    return html;
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


