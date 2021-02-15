export function getInfoDiv() {
    let div = document.createElement("div");
    div.innerHTML = getInfoHtml();

    return div;
}

function getInfoHtml() {
    return "<div class=\"content\">\n" +
        "    <div class=\"container-fluid\">\n" +
        "        <div class=\"row\">\n" +
        "            <div class=\"col-12\">\n" +
        "                <div class=\"card\">\n" +
        "                    <div class=\"card-header\">\n" +
        "                        <div class=\"col-md-10\">\n" +
        "                            <p style=\"margin-top:10px; font-size: 22px;\">Request</p>\n" +
        "                        </div>\n" +
        "                        <div class=\"col-md-2\"></div>\n" +
        "                        <hr>\n" +
        "                    </div>\n" +
        "                    <div class=\"card-body\" id=\"tableContainer\">\n" +
        "                        <div class=\"row\">\n" +
        "                            <ul style=\"color:red;\" id=\"errors\">\n" +
        "                            </ul>\n" +
        "                        </div>\n" +
        "                        <form id=\"createRequestForm\">\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label class=\"label-control\">Main Rower</label>\n" +
        "                                        <input id=\"mainRower\" disabled name=\"mainRower\"\n" +
        "                                               class=\"form-control\"\n" +
        "                                               data-style=\"btn btn-link\" rows=\"1\">\n" +
        "                                        </input>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label class=\"label-control\">Activity Date</label>\n" +
        "                                        <input style=\"text-align: left\" disabled id=\"activityDate\" name=\"activityDate\"\n" +
        "                                               type=\"text\"\n" +
        "                                               class=\"form-control datepicker2\" autocomplete=\"off\"/>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-12\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"activityDetails\">Weekly Activity Details<br></label>\n" +
        "                                        <textarea disabled id=\"activityDetails\" class=\"form-control\"\n" +
        "                                                  data-style=\"btn btn-link\"\n" +
        "                                                  multiple=\"multiple\" rows=\"4\">\n" +
        "                                        </textarea>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"boatTypes\">Boat Types<br></label>\n" +
        "                                        <textarea disabled id=\"boatTypes\" class=\"form-control\"\n" +
        "                                                  data-style=\"btn btn-link\"\n" +
        "                                                  multiple=\"multiple\" rows=\"5\">\n" +
        "                                        </textarea>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"otherRowers\">Other Rowers<br></label>\n" +
        "                                        <textarea disabled id=\"otherRowers\" class=\"form-control\"\n" +
        "                                                  data-style=\"btn btn-link\"\n" +
        "                                                  multiple=\"multiple\" rows=\"5\">\n" +
        "                                        </textarea>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <hr style=\"margin-top: 70px\">\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-4\"></div>\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <button id='duplicateBtn' class=\"btn btn-dark btn-block\">\n" +
        "                                        Duplicate\n" +
        "                                    </button>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"clearfix\"></div>\n" +
        "                        </form>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
}