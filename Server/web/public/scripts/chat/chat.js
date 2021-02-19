const chatEl = document.getElementById('chat');

// init chat

document.addEventListener('DOMContentLoaded', function () {
    let link = document.createElement('link');
    link.rel = "stylesheet";
    link.href = "/public/css/chat.css";
    chatEl.appendChild(link);
    let htmlEl = document.createElement('html');
    htmlEl.innerHTML = html;
    chatEl.appendChild(htmlEl);
    initChatStyle();
})

function initChatStyle() {
    chatEl.style.position = "fixed";
    chatEl.style.right = "6vh";
    chatEl.style.bottom = "5vh";
    chatEl.style.width = "35vh";
}


const css = "\n" +
    ".card-bordered {\n" +
    "    border: 1px solid #ebebeb\n" +
    "}\n" +
    "\n" +
    ".card {\n" +
    "    border: 0;\n" +
    "    border-radius: 0px;\n" +
    "    margin-bottom: 30px;\n" +
    "    -webkit-box-shadow: 0 2px 3px rgba(0, 0, 0, 0.03);\n" +
    "    box-shadow: 0 2px 3px rgba(0, 0, 0, 0.03);\n" +
    "    -webkit-transition: .5s;\n" +
    "    transition: .5s;\n" +
    "}\n" +
    "\n" +
    ".padding {\n" +
    "    padding: 3rem !important\n" +
    "}\n" +
    "\n" +
    "body {\n" +
    "    background-color: #f9f9fa\n" +
    "}\n" +
    "\n" +
    ".card-header:first-child {\n" +
    "    border-radius: calc(.25rem - 1px) calc(.25rem - 1px) 0 0\n" +
    "}\n" +
    "\n" +
    ".card-header {\n" +
    "    display: -webkit-box;\n" +
    "    display: flex;\n" +
    "    -webkit-box-pack: justify;\n" +
    "    justify-content: space-between;\n" +
    "    -webkit-box-align: center;\n" +
    "    align-items: center;\n" +
    "    padding: 15px 20px;\n" +
    "    background-color: transparent;\n" +
    "    border-bottom: 1px solid rgba(77, 82, 89, 0.07)\n" +
    "}\n" +
    "\n" +
    ".card-header .card-title {\n" +
    "    padding: 0;\n" +
    "    border: none\n" +
    "}\n" +
    "\n" +
    "h4.card-title {\n" +
    "    font-size: 17px\n" +
    "}\n" +
    "\n" +
    ".card-header > *:last-child {\n" +
    "    margin-right: 0\n" +
    "}\n" +
    "\n" +
    ".card-header > * {\n" +
    "    margin-left: 8px;\n" +
    "    margin-right: 8px\n" +
    "}\n" +
    "\n" +
    ".btn-secondary {\n" +
    "    color: #4d5259 !important;\n" +
    "    background-color: #e4e7ea;\n" +
    "    border-color: #e4e7ea;\n" +
    "    color: #fff\n" +
    "}\n" +
    "\n" +
    ".btn-xs {\n" +
    "    font-size: 11px;\n" +
    "    padding: 2px 8px;\n" +
    "    line-height: 18px\n" +
    "}\n" +
    "\n" +
    ".btn-xs:hover {\n" +
    "    color: #fff !important\n" +
    "}\n" +
    "\n" +
    ".card-title {\n" +
    "    font-family: Roboto, sans-serif;\n" +
    "    font-weight: 300;\n" +
    "    line-height: 1.5;\n" +
    "    margin-bottom: 0;\n" +
    "    padding: 15px 20px;\n" +
    "    border-bottom: 1px solid rgba(77, 82, 89, 0.07)\n" +
    "}\n" +
    "\n" +
    ".ps-container {\n" +
    "    position: relative\n" +
    "}\n" +
    "\n" +
    ".ps-container {\n" +
    "    -ms-touch-action: auto;\n" +
    "    touch-action: auto;\n" +
    "    overflow: hidden !important;\n" +
    "    -ms-overflow-style: none\n" +
    "}\n" +
    "\n" +
    ".media-chat {\n" +
    "    padding-right: 64px;\n" +
    "    margin-bottom: 0\n" +
    "}\n" +
    "\n" +
    ".media {\n" +
    "    padding: 16px 12px;\n" +
    "    -webkit-transition: background-color .2s linear;\n" +
    "    transition: background-color .2s linear\n" +
    "}\n" +
    "\n" +
    ".media .avatar {\n" +
    "    flex-shrink: 0\n" +
    "}\n" +
    "\n" +
    ".avatar {\n" +
    "    position: relative;\n" +
    "    display: inline-block;\n" +
    "    width: 36px;\n" +
    "    height: 36px;\n" +
    "    line-height: 36px;\n" +
    "    text-align: center;\n" +
    "    border-radius: 100%;\n" +
    "    background-color: #f5f6f7;\n" +
    "    color: #8b95a5;\n" +
    "    text-transform: uppercase\n" +
    "}\n" +
    "\n" +
    ".media-chat .media-body {\n" +
    "    -webkit-box-flex: initial;\n" +
    "    flex: initial;\n" +
    "    display: table\n" +
    "}\n" +
    "\n" +
    ".media-body {\n" +
    "    min-width: 0\n" +
    "}\n" +
    "\n" +
    ".media-chat .media-body p {\n" +
    "    position: relative;\n" +
    "    padding: 6px 8px;\n" +
    "    margin: 4px 0;\n" +
    "    background-color: #f5f6f7;\n" +
    "    border-radius: 3px;\n" +
    "    font-weight: 100;\n" +
    "    color: #9b9b9b\n" +
    "}\n" +
    "\n" +
    ".media > * {\n" +
    "    margin: 0 8px\n" +
    "}\n" +
    "\n" +
    ".media-chat .media-body p.meta {\n" +
    "    background-color: transparent !important;\n" +
    "    padding: 0;\n" +
    "    opacity: .8\n" +
    "}\n" +
    "\n" +
    ".media-meta-day {\n" +
    "    -webkit-box-pack: justify;\n" +
    "    justify-content: space-between;\n" +
    "    -webkit-box-align: center;\n" +
    "    align-items: center;\n" +
    "    margin-bottom: 0;\n" +
    "    color: #8b95a5;\n" +
    "    opacity: .8;\n" +
    "    font-weight: 400\n" +
    "}\n" +
    "\n" +
    ".media {\n" +
    "    padding: 16px 12px;\n" +
    "    -webkit-transition: background-color .2s linear;\n" +
    "    transition: background-color .2s linear\n" +
    "}\n" +
    "\n" +
    ".media-meta-day::before {\n" +
    "    margin-right: 16px\n" +
    "}\n" +
    "\n" +
    ".media-meta-day::before,\n" +
    ".media-meta-day::after {\n" +
    "    content: '';\n" +
    "    -webkit-box-flex: 1;\n" +
    "    flex: 1 1;\n" +
    "    border-top: 1px solid #ebebeb\n" +
    "}\n" +
    "\n" +
    ".media-meta-day::after {\n" +
    "    content: '';\n" +
    "    -webkit-box-flex: 1;\n" +
    "    flex: 1 1;\n" +
    "    border-top: 1px solid #ebebeb\n" +
    "}\n" +
    "\n" +
    ".media-meta-day::after {\n" +
    "    margin-left: 16px\n" +
    "}\n" +
    "\n" +
    ".media-chat.media-chat-reverse {\n" +
    "    padding-right: 12px;\n" +
    "    padding-left: 64px;\n" +
    "    -webkit-box-orient: horizontal;\n" +
    "    -webkit-box-direction: reverse;\n" +
    "    flex-direction: row-reverse\n" +
    "}\n" +
    "\n" +
    ".media-chat {\n" +
    "    padding-right: 64px;\n" +
    "    margin-bottom: 0\n" +
    "}\n" +
    "\n" +
    ".media {\n" +
    "    padding: 16px 12px;\n" +
    "    -webkit-transition: background-color .2s linear;\n" +
    "    transition: background-color .2s linear\n" +
    "}\n" +
    "\n" +
    ".media-chat.media-chat-reverse .media-body p {\n" +
    "    float: right;\n" +
    "    clear: right;\n" +
    "    background-color: #48b0f7;\n" +
    "    color: #fff\n" +
    "}\n" +
    "\n" +
    ".media-chat .media-body p {\n" +
    "    position: relative;\n" +
    "    padding: 6px 8px;\n" +
    "    margin: 4px 0;\n" +
    "    background-color: #f5f6f7;\n" +
    "    border-radius: 3px\n" +
    "}\n" +
    "\n" +
    ".border-light {\n" +
    "    border-color: #f1f2f3 !important\n" +
    "}\n" +
    "\n" +
    ".bt-1 {\n" +
    "    border-top: 1px solid #ebebeb !important\n" +
    "}\n" +
    "\n" +
    ".publisher {\n" +
    "    position: relative;\n" +
    "    display: -webkit-box;\n" +
    "    display: flex;\n" +
    "    -webkit-box-align: center;\n" +
    "    align-items: center;\n" +
    "    padding: 12px 20px;\n" +
    "    background-color: #f9fafb\n" +
    "}\n" +
    "\n" +
    ".publisher > *:first-child {\n" +
    "    margin-left: 0\n" +
    "}\n" +
    "\n" +
    ".publisher > * {\n" +
    "    margin: 0 8px\n" +
    "}\n" +
    "\n" +
    ".publisher-input {\n" +
    "    -webkit-box-flex: 1;\n" +
    "    flex-grow: 1;\n" +
    "    border: none;\n" +
    "    outline: none !important;\n" +
    "    background-color: transparent\n" +
    "}\n" +
    "\n" +
    "button,\n" +
    "input,\n" +
    "optgroup,\n" +
    "select,\n" +
    "textarea {\n" +
    "    font-family: Roboto, sans-serif;\n" +
    "    font-weight: 300\n" +
    "}\n" +
    "\n" +
    ".publisher-btn {\n" +
    "    background-color: transparent;\n" +
    "    border: none;\n" +
    "    color: #8b95a5;\n" +
    "    font-size: 16px;\n" +
    "    cursor: pointer;\n" +
    "    overflow: -moz-hidden-unscrollable;\n" +
    "    -webkit-transition: .2s linear;\n" +
    "    transition: .2s linear\n" +
    "}\n" +
    "\n" +
    ".file-group {\n" +
    "    position: relative;\n" +
    "    overflow: hidden\n" +
    "}\n" +
    "\n" +
    ".publisher-btn {\n" +
    "    background-color: transparent;\n" +
    "    border: none;\n" +
    "    color: #cac7c7;\n" +
    "    font-size: 16px;\n" +
    "    cursor: pointer;\n" +
    "    overflow: -moz-hidden-unscrollable;\n" +
    "    -webkit-transition: .2s linear;\n" +
    "    transition: .2s linear\n" +
    "}\n" +
    "\n" +
    ".file-group input[type=\"file\"] {\n" +
    "    position: absolute;\n" +
    "    opacity: 0;\n" +
    "    z-index: -1;\n" +
    "    width: 20px\n" +
    "}\n" +
    "\n" +
    ".text-info {\n" +
    "    color: #48b0f7 !important\n" +
    "}\n" +
    "\n" +
    "#chatIframe {\n" +
    "    display: none;\n" +
    "}";

// chat container html
// "<link href=\"https://use.fontawesome.com/releases/v5.7.2/css/all.css\" rel=\"stylesheet\">\n"

const html =
    "<div class=\"page-content page-container\" id=\"page-content\">\n" +
    "    <div class=\"padding\">\n" +
    "        <div class=\"row d-flex justify-content-center\">\n" +
    "            <div class=\"col-md-12\">\n" +
    "                <div class=\"card card-bordered\">\n" +
    "                    <div class=\"card-header\">\n" +
    "                        <h4 class=\"card-title\"><strong>Chat</strong></h4>\n" +
    "                    </div>\n" +
    "                    <div class=\"ps-container ps-theme-default ps-active-y\" id=\"chat-content\"\n" +
    "                         style=\"overflow-y: scroll !important; height:400px !important;\">\n" +
    "                        <div class=\"media media-chat\">\n" +
    "                            <img class=\"avatar\" src=\"https://img.icons8.com/color/36/000000/administrator-male.png\"\n" +
    "                                 alt=\"...\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Hi</p>\n" +
    "                                <p>How are you ...???</p>\n" +
    "                                <p>What are you doing tomorrow?<br> Can we come up a bar?</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">23:58</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-meta-day\">Today</div>\n" +
    "                        <div class=\"media media-chat media-chat-reverse\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Hiii, I'm good.</p>\n" +
    "                                <p>How are you doing?</p>\n" +
    "                                <p>Long time no see! Tomorrow office. will be free on sunday.</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:06</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat\"><img class=\"avatar\"\n" +
    "                                                           src=\"https://img.icons8.com/color/36/000000/administrator-male.png\"\n" +
    "                                                           alt=\"...\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Okay</p>\n" +
    "                                <p>We will go on sunday? </p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:07</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat media-chat-reverse\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>That's awesome!</p>\n" +
    "                                <p>I will meet you Sandon Square sharp at 10 AM</p>\n" +
    "                                <p>Is that okay?</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:09</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat\"><img class=\"avatar\"\n" +
    "                                                           src=\"https://img.icons8.com/color/36/000000/administrator-male.png\"\n" +
    "                                                           alt=\"...\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Okay i will meet you on Sandon Square </p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:10</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat media-chat-reverse\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Do you have pictures of Matley Marriage?</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:10</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat\"><img class=\"avatar\"\n" +
    "                                                           src=\"https://img.icons8.com/color/36/000000/administrator-male.png\"\n" +
    "                                                           alt=\"...\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Sorry I don't have. i changed my phone.</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:12</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"media media-chat media-chat-reverse\">\n" +
    "                            <div class=\"media-body\">\n" +
    "                                <p>Okay then see you on sunday!!</p>\n" +
    "                                <p class=\"meta\">\n" +
    "                                    <time datetime=\"2018\">00:12</time>\n" +
    "                                </p>\n" +
    "                            </div>\n" +
    "                        </div>\n" +
    "                        <div class=\"ps-scrollbar-x-rail\" style=\"left: 0px; bottom: 0px;\">\n" +
    "                            <div class=\"ps-scrollbar-x\" tabindex=\"0\" style=\"left: 0px; width: 0px;\"></div>\n" +
    "                        </div>\n" +
    "                        <div class=\"ps-scrollbar-y-rail\" style=\"top: 0px; height: 0px; right: 2px;\">\n" +
    "                            <div class=\"ps-scrollbar-y\" tabindex=\"0\" style=\"top: 0px; height: 2px;\"></div>\n" +
    "                        </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"publisher bt-1 border-light\">\n" +
    "                        <img class=\"avatar avatar-xs\"\n" +
    "                             src=\"https://img.icons8.com/color/36/000000/administrator-male.png\"\n" +
    "                             alt=\"...\">\n" +
    "                        <input class=\"publisher-input\" type=\"text\" placeholder=\"Write something\">\n" +
    "                        <a class=\"publisher-btn text-info\" href=\"#\" data-abc=\"true\"><i class=\"fa fa-paper-plane\"></i>\n" +
    "                        </a>\n" +
    "                    </div>\n" +
    "                </div>\n" +
    "            </div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>";


