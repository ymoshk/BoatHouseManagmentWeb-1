export function getInfoDiv() {
    let div = document.createElement("div");
    div.innerHTML = getInfoHtml();

    return div;
}

function getInfoHtml() {
    return "<style>.form-check .form-check-input[disabled] ~ .form-check-sign .check {\n" +
        "    opacity: 1 !important; */\n" +
        "}" +
        "fieldset[disabled] .form-check, fieldset[disabled] .form-check .form-check-input, .form-check .form-check-input[disabled] ~ .form-check-sign .check, .form-check .form-check-input[disabled] + .circle {\n" +
        "    opacity: 1 !important; \n" +
        "}" +
        "</style>" +
        "<div class=\"content\">\n" +
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
        "                                        <label for=\"serialNumber\">ID Number</label>\n" +
        "                                        <input id=\"serialNumber\" name=\"serialNumber\" type=\"text\" class=\"form-control\"\n" +
        "                                               disabled>\n" +
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
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"owner\">Owner</label>\n" +
        "                                        <input id=\"owner\" name=\"owner\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-2\">\n" +
        "                                    <div class=\"form-check pull-left\">\n" +
        "                                        <label style=\"margin-top: 20px; margin-left: 0px\" class=\"form-check-label\">\n" +
        "                                            <input id=\"isWide\" disabled class=\"form-check-input\" type=\"checkbox\"\n" +
        "                                                   value=\"\">Is Wide\n" +
        "                                            <span class=\"form-check-sign\"><span class=\"check\"></span></span>\n" +
        "                                        </label>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-2\">\n" +
        "                                    <div class=\"form-check pull-left\">\n" +
        "                                        <label style=\"margin-top: 20px; margin-left: 0px\" class=\"form-check-label\">\n" +
        "                                            <input id=\"isSeaBoat\" disabled class=\"form-check-input\" type=\"checkbox\"\n" +
        "                                                   value=\"\">Is Sew Boat\n" +
        "                                            <span class=\"form-check-sign\"><span class=\"check\"></span></span>\n" +
        "                                        </label>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-2\">\n" +
        "                                    <div class=\"form-check pull-left\">\n" +
        "                                        <label style=\"margin-top: 20px; margin-left: 0px\" class=\"form-check-label\">\n" +
        "                                            <input id=\"isDisable\" disabled class=\"form-check-input\" type=\"checkbox\"\n" +
        "                                                   value=\"\">Is Disable\n" +
        "                                            <span class=\"form-check-sign\"><span class=\"check\"></span></span>\n" +
        "                                        </label>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"boatType\">Boat Type</label>\n" +
        "                                        <input id=\"boatType\" name=\"boatType\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"clearfix\"></div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>\n" +
        "\n";
}