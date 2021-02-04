const



function buildMenuItem(itemObject){
    return  '<a class="nav-link" id=' + itemObject.id + ' href=' + itemObject.href + '>' +
            '   <p class="simple-text"><i class= ' + itemObject.icon +'></i>' + itemObject.text + '</p>' +
            '</a>'
}