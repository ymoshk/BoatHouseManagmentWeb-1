export function getInfoDiv() {
    let div = document.createElement("div");
    div.innerHTML = getInfoHtml();

    return div;
}

function getInfoHtml() {
    return "<div class=\"container-fluid\">\n" +
        "    <div class=\"row\">\n" +
        "        <div class=\"col-12\">\n" +
        "            <div class=\"card\">\n" +
        "                <div class=\"card-header card-header-primary\">\n" +
        "                    <div class=\"col-md-10\" style=\"text-align: left\">\n" +
        "                        <p style=\"margin-top:10px; font-size: 22px;\">Information</p>\n" +
        "                    </div>\n" +
        "                    <div class=\"col-md-2\"></div>\n" +
        "                    <hr>\n" +
        "                </div>\n" +
        "                <div class=\"card-body\" id=\"tableContainer\">\n" +
        "                    <div class=\"infoCard\">\n" +
        "                        <div class=\"row\">\n" +
        "                            <div class=\"col-md-9\"></div>\n" +
        "                            <div class=\"col-md-3\">\n" +
        "                                <button id=\"duplicateBtn\" class=\"btn btn-dark btn-block\">\n" +
        "                                    Duplicate\n" +
        "                                </button>\n" +
        "                            </div>\n" +
        "                            <hr style=\"margin-top: 70px\">\n" +
        "                        </div>\n" +
        "                        <div class=\"row\">\n" +
        "                            <div class=\"col-md-6\">\n" +
        "                                <div class=\"form-group bmd-form-group\">\n" +
        "                                    <label class=\"label-control\">Main Rower</label>\n" +
        "                                    <input id=\"mainRower\" disabled=\"\" name=\"mainRower\" class=\"form-control\"\n" +
        "                                           data-style=\"btn btn-link\" rows=\"1\">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"col-md-6\">\n" +
        "                                <div class=\"form-group bmd-form-group\">\n" +
        "                                    <label class=\"label-control\">Activity Date</label>\n" +
        "                                    <input style=\"text-align: left\" disabled=\"\" id=\"activityDate\" name=\"activityDate\"\n" +
        "                                           type=\"text\" class=\"form-control datepicker2\" autocomplete=\"off\">\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                        <div class=\"row\">\n" +
        "                            <div class=\"col-md-12\">\n" +
        "                                <div class=\"form-group bmd-form-group\">\n" +
        "                                    <label for=\"activityDetails\">Weekly Activity Details<br></label>\n" +
        "                                    <textarea disabled=\"\" id=\"activityDetails\" class=\"form-control\"\n" +
        "                                              data-style=\"btn btn-link\" multiple=\"multiple\"\n" +
        "                                              rows=\"5\">                                        </textarea>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                        <div class=\"row\">\n" +
        "                            <div class=\"col-md-6\">\n" +
        "                                <div class=\"form-group bmd-form-group\">\n" +
        "                                    <label for=\"boatTypes\">Boat Types<br></label>\n" +
        "                                    <textarea disabled=\"\" id=\"boatTypes\" class=\"form-control\" data-style=\"btn btn-link\"\n" +
        "                                              multiple=\"multiple\"\n" +
        "                                              rows=\"5\">                                        </textarea>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"col-md-6\">\n" +
        "                                <div class=\"form-group bmd-form-group\">\n" +
        "                                    <label for=\"otherRowers\">Other Rowers<br></label>\n" +
        "                                    <textarea disabled=\"\" id=\"otherRowers\" class=\"form-control\"\n" +
        "                                              data-style=\"btn btn-link\" multiple=\"multiple\"\n" +
        "                                              rows=\"5\">                                        </textarea>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                        </div>\n" +
        "                        <div class=\"clearfix\"></div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
}