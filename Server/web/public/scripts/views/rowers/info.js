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
        "                                <div class=\"col-md-5\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"serialNumber\">ID Number</label>\n" +
        "                                        <input id=\"serialNumber\" name=\"serialNumber\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-5\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"name\">Full Name</label>\n" +
        "                                        <input id=\"name\" name=\"name\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-2\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"age\">Age</label>\n" +
        "                                        <input id=\"age\" name=\"age\" type=\"number\" class=\"form-control\"\n" +
        "                                               value=\"13\" min=\"13\"\n" +
        "                                               max=\"120\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"phone\">Phone Number</label>\n" +
        "                                        <input id=\"phone\" name=\"phone\" type=\"text\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"email\">Email Address</label>\n" +
        "                                        <input id=\"email\" name=\"email\" type=\"email\" class=\"form-control\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-check\">\n" +
        "                                        <label style=\"margin-top: 20px; margin-left: 20px\" class=\"form-check-label\">\n" +
        "                                            <input id=\"isAdmin\" disabled class=\"form-check-input\" type=\"checkbox\"\n" +
        "                                                   value=\"\">Is Admin\n" +
        "                                            <span class=\"form-check-sign\"><span class=\"check\"></span></span>\n" +
        "                                        </label>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"rank\">Rank</label>\n" +
        "                                        <input id=\"rank\" class=\"form-control\" minlength=\"4\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"joiningDate\">Joining Date</label>\n" +
        "                                        <input id=\"joiningDate\" class=\"form-control\" minlength=\"4\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-4\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"expDate\">Expiration Date</label>\n" +
        "                                        <input id=\"expDate\" class=\"form-control\" minlength=\"4\" disabled>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <div class=\"row\">\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"notes\">Notes</label>\n" +
        "                                        <textarea id=\"notes\" disabled name=\"notes\" class=\"form-control\" rows=\"4\"></textarea>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                                <div class=\"col-md-6\">\n" +
        "                                    <div class=\"form-group bmd-form-group\">\n" +
        "                                        <label for=\"privateBoats\">Private Boats</label>\n" +
        "                                        <textarea id=\"privateBoats\" disabled class=\"form-control\" rows=\"4\"></textarea>\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>\n" +
        "                            <hr style=\"margin-top: 70px\">\n" +
        "                            <div class=\"clearfix\"></div>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "    <script src=\"/public/scripts/views/rowers/create.js\"></script>\n" +
        "</div>\n" +
        "\n"
}