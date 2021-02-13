export function getInfoDiv() {
    let div = document.createElement("div");
    div.innerHTML = getInfoHtml().trim();

    return div;
}

function getInfoHtml() {
    return "<div class=\"content\">\n" +
        "    <div class=\"container-fluid\">\n" +
        "        <div class=\"row\">\n" +
        "            <div class=\"col-12\">\n" +
        "                <div class=\"card\">\n" +
        "                    <div class=\"card-header card-header-primary\">\n" +
        "                        <div class=\"col-md-10\" style=\"text-align: left\">\n" +
        "                            <p style=\"margin-top:10px; font-size: 22px;\">Information</p>\n" +
        "                        </div>\n" +
        "                        <div class=\"col-md-2\"></div>\n" +
        "                        <hr>\n" +
        "                    </div>\n" +
        "                    <div class=\"card-body\" id=\"tableContainer\">\n" +
        "                        <div class=\"infoCard\">\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"boatType\">Boat Type</label>\n" +
        "                                        <input id=\"boatType\" name=\"boatType\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"name\">Boat Name</label>\n" +
        "                                        <input id=\"name\" name=\"name\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-3\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"startTime\">Start Time</label>\n" +
        "                                        <input style='text-align: left;' id=\"startTime\" name=\"startTime\" type=\"time\" class=\"form-control\"\n" +
        "                                               disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-3\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"endTime\">End Time</label>\n" +
        "                                        <input style='text-align: left' id=\"endTime\" name=\"endTime\" type=\"time\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <hr style=\"margin-top: 70px\">\n" +
        "                                <div class=\"clearfix\"></div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
}