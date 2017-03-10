<#--
 #%L
 Bobcat Parent
 %%
 Copyright (C) 2016 Cognifide Ltd.
 %%
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
      http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 #L%
-->
<#escape x as x?html>
<html>
<head>
    <meta content="text/html; charset=UTF-8" http-equiv="content-type">
    <title>Bobcat Test Results</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            padding-top: 60px;
        }

        td {
            padding: 4px;
        }

        .navbar .navbar-nav .nav-item-danger > a {
            background-color: #D9534F;
            color: white;
        }

        .navbar .navbar-nav .nav-item-success > a {
            background-color: #5CB85C;
            color: white;
        }

        .navbar .navbar-nav .nav-item-info > a {
            background-color: #2A6496;
            color: white;
        }

        #flash-message-container {
            position: fixed;
            top: 0px;
            width: 100%;
            z-index: 9999;
        }

        .panel-success table {
            background-color: #dff0d8;
        }

        .panel-danger table {
            background-color: #f2dede;
        }

    </style>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>

    <script type="text/javascript">

        var reportSorter = (function ($, document) {
            "use strict";

            var api = {},

                    selectors = {
                        container: '.test-results',
                        panel: 'div.panel',
                        panelHeading: '.panel-heading',
                        panelHeadingTitle: '.panel-title > a'
                    },

                    statusValueAdapter = function (status) {
                        if (status === 'success') {
                            return 0;
                        } else if (status === 'warning') {
                            return 1;
                        } else {
                            return 2;
                        }
                    },

                    getStatus = function (elem) {
                        return $(elem).data('status');
                    },

                    getName = function (elem) {
                        return $(elem).children(selectors.panelHeading).find(selectors.panelHeadingTitle).text().trim();
                    },

                    comparators = {
                        nameComparator: function (a, b) {
                            if (a === b) {
                                return 0;
                            }
                            return a > b ? 1 : -1;
                        },

                        statusComparator: function (a, b) {
                            var aStatusValue = statusValueAdapter(a),
                                    bStatusValue = statusValueAdapter(b);

                            if (aStatusValue === bStatusValue) {
                                return 0;
                            }
                            return aStatusValue > bStatusValue ? -1 : 1;
                        },

                        statusNameComparator: function (a, b) {
                            var aStatus = getStatus(a),
                                    bStatus = getStatus(b),
                                    aName = getName(a),
                                    bName = getName(b);

                            var result = comparators.statusComparator(aStatus, bStatus);
                            if (result === 0) {
                                result = comparators.nameComparator(aName, bName);
                            }
                            return result;
                        }
                    };

            api.sort = function () {
                var $container = $(selectors.container);

                $container.each(function () {
                    var $panels = $(this).children(selectors.panel);

                    $panels.sort(comparators.statusNameComparator);
                    $panels.each(function () {
                        var $subPanels = $(this).find(selectors.panel);
                        $subPanels.sort(comparators.statusNameComparator);
                        $(this).find('.panel-body:first').append($subPanels.detach());
                    });

                    $(this).append($panels.detach());
                });
            };

            return api;

        })($, document);

        var buttonGroupToggle = (function ($, document, config) {
            "use strict";

            var selectors = {
                btnGroup: '.btn-group',
                btn: 'button'
            };

            $(document).ready(function () {
                var $elements = $(selectors.btnGroup);

                $elements.children(selectors.btn).click(function () {
                    var $elem = $(this),
                            $parent = $elem.parent();
                    $parent.children(selectors.btn).removeClass(config.activeClass).addClass(config.disabledClass);
                    $elem.removeClass(config.disabledClass).addClass(config.activeClass);
                });
            });
        })($, document, {
            activeClass: 'btn-primary',
            disabledClass: 'btn-default'
        });

        var toggleVisibility = (function ($, document, config) {
            "use strict";

            var selectors = {
                btn: '.toggle-visibility-button'
            };

            $(document).ready(function () {
                var $elements = $(selectors.btn);

                $elements.click(function () {
                    var $elem = $(this),
                            $target = $($elem.data('target'));

                    $target.each(function () {
                        var $elem = $(this);
                        if ($elem.hasClass(config.toggleClass)) {
                            $elem.removeClass(config.toggleClass);
                        } else {
                            $elem.addClass(config.toggleClass);
                        }
                    });
                });
            });
        })($, document, {
            toggleClass: 'hide'
        });

        var reportEventHandler = (function ($, document) {
            "use strict";

            var api = {},

                    ui = {},

                    selectors = {
                        reportRow: ".report-row",
                        toggleAllBtn: ".toggle-all-btn",
                        toggleAllErrorsBtn: ".toggle-all-errors-btn",
                        baseReportRowCollapsible: '#container > div > section > div.test-results > div.report-row > .collapse',
                        baseReportRowCollapsibleError: '.panel-danger > .collapse',
                        navbar: '.navbar',
                        testSection: '.test-section'
                    },

                    $selectedReportRow = null,

                    isExpandedAll = false,

                    isExpandedErrors = false,

                    registerUi = function () {
                        ui.$reportRow = $(selectors.reportRow);
                        ui.$toogleAllBtn = $(selectors.toggleAllBtn);
                        ui.$toogleErrorsBtn = $(selectors.toggleAllErrorsBtn);
                        ui.$baseReportRowCollapsible = $(selectors.baseReportRowCollapsible);
                        ui.$baseReportRowCollapsibleError = $(selectors.baseReportRowCollapsibleError);
                        ui.$navbar = $(selectors.navbar);
                    },

                    registerEvents = function () {
                        ui.$reportRow.click(function (event) {
                            var $elem = $(this);
                            if (event.shiftKey) {
                                api.recursiveReportRowToggle($elem);
                            } else if (event.which === 2 || (event.which === 1 && event.ctrlKey)) {
                                api.openReportRowSourcePage($elem);
                            }
                            $selectedReportRow = $(event.currentTarget);
                        });

                        ui.$toogleAllBtn.click(function (event) {
                            api.toggleAllTests();
                        });

                        ui.$toogleErrorsBtn.click(function (event) {
                            api.toggleErrorTests();
                        });

                    },

                    registerKeyEvents = function () {
                        $(document.body).keyup(function (event) {
                            if (event.which === 81) { //q key
                                api.toggleAllTests();
                            } else if (event.which === 69) {// e key
                                api.toggleErrorTests();
                            } else if (event.which === 221) {// ] key
                                api.scrollToNextTest();
                            } else if (event.which === 219) {// [ key
                                api.scrollToPrevTest();
                            }
                        });
                    };

            api.scrollToNextTest = function () {
                var $nextItem;
                if ($selectedReportRow === null) {
                    $selectedReportRow = $(selectors.reportRow).first();
                }
                $nextItem = $selectedReportRow.next();
                $(window).scrollTop($nextItem.offset().top - ui.$navbar.height() - 10);
                $selectedReportRow = $nextItem;
            };

            api.scrollToPrevTest = function () {
                var $prevItem;
                if ($selectedReportRow === null) {
                    $selectedReportRow = $(selectors.reportRow).first();
                }
                $prevItem = $selectedReportRow.prev();
                $(window).scrollTop($prevItem.offset().top - ui.$navbar.height() - 10);
                $selectedReportRow = $prevItem;
            };

            api.toggleAllTests = function () {
                var $elems = ui.$baseReportRowCollapsible;
                if (isExpandedAll) {
                    api.collapseTests($elems);
                } else {
                    api.expandTests($elems);
                }
                isExpandedAll = !isExpandedAll;
                isExpandedErrors = isExpandedAll;
            };

            api.toggleErrorTests = function () {
                var $elems = ui.$baseReportRowCollapsibleError;
                if (isExpandedErrors) {
                    api.collapseTests($elems);
                } else {
                    api.expandTests($elems);
                }
                isExpandedErrors = !isExpandedErrors;
            };

            api.expandTests = function ($elem) {
                $elem.collapse('show');
            };

            api.collapseTests = function ($elem) {
                $elem.collapse('hide');
            };

            api.recursiveReportRowToggle = function ($elem) {
                var $collapsible = $elem.children('.collapse'),
                        $collapsibleChildren = $collapsible.find('.collapse');
                if ($collapsible.hasClass('in')) {
                    api.collapseTests($collapsibleChildren)
                } else {
                    api.expandTests($collapsibleChildren)
                }
            };

            api.openReportRowSourcePage = function ($elem) {
                window.open($elem.find('a:first').data('source'));
                event.preventDefault();
                event.stopPropagation();
            }

            $(document).ready(function () {
                registerUi();
                registerEvents();
                registerKeyEvents();
            });

            return api;
        })($, document);

        $(document).ready(function () {
            reportSorter.sort();
            $('[data-toggle="tooltip"]').tooltip();
        });

    </script>
</head>
<body>
<div role="navigation" class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="#" class="navbar-brand hidden-xs">Bobcat Tests Report
                <span>${date}</span></a>
        </div>
        <div class="collapse navbar-collapse navbar-right">
            <ul class="nav navbar-nav">
                <li data-original-title="Failed: ${failed} / ${total}" class="nav-item-danger" data-toggle="tooltip"
                    data-placement="bottom" title="">
                    <a href="#">
					${failedPercent}%
                        <i class="glyphicon glyphicon-remove"></i>
                    </a>
                </li>
                <li data-original-title="Passed: ${passed} / ${total}" class="nav-item-success" data-toggle="tooltip"
                    data-placement="bottom" title="">
                    <a href="#">
					${passedPercent}%
                        <i class="glyphicon glyphicon-ok"></i>
                    </a>
                </li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="glyphicon glyphicon glyphicon-cog"></i>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li>
                            <a href="#" class="toggle-all-btn">
                                <i class="glyphicon glyphicon-chevron-down"></i>
                                Toggle All
                            </a>
                        </li>
                        <li>
                            <a href="#" class="toggle-all-errors-btn">
                                <i class="glyphicon glyphicon-chevron-down"></i>
                                Toggle Errors
                            </a>
                        </li>
                        <li>
                            <a href="#help" data-target="#help" data-toggle="modal" class="nav-item-info">
                                <i class="glyphicon glyphicon glyphicon-info-sign"></i>
                                Help
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</div>
<div id="container">
    <div class="col-md-12">
        <section class="test-section">
            <h1>Environment</h1>
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h4 class="panel-title">
                        <a class="collapsed" data-toggle="collapse" href="#properties">
                            <span class="glyphicon glyphicon-chevron-down"></span>
                        Properties
                        </a>
                    </h4>
                </div>
                <div id="properties" class="panel-collapse collapse">
                    <div class="panel-body">
                        <table border="1">
                            <#list collector.properties as entry>
                                <tr><td>${entry.key}</td><td>${entry.value}</td></tr>
                            </#list>
                        </table>
                    </div>
                </div>
            </div>
            <h2>
                Test results
            </h2>
            <div class="test-results" data-test-name="Test results">
				<#assign sub_id=0>
				<#list collector.testInfoEntries as test>
					<#if test.result = "OK">
                    <div class="panel report-row panel-success" data-status="success">
					<#else>
                    <div class="panel report-row panel-danger" data-status="failed">
					</#if>
                    <div class="panel-heading">
                        <h4 class="panel-title">
                            <a class="collapsed" data-toggle="collapse" data-parent="#tests-accordion"
                               href="#test-${sub_id}">
                                <span class="glyphicon glyphicon-chevron-down"></span>
							${test.testName}
                            </a>
                        </h4>
                    </div>
                    <div id="test-${sub_id}" class="panel-collapse collapse">
                        <div class="panel-body">
                            <table border=1>
								<#list test.logEntries as entry>
                                <tr class="${entry.class.simpleName}">

									<#switch entry.class.simpleName>
										<#case "ErrorEntry">
                                            <td colspan="3">${entry.message!""}</td>
											<#break>
										<#case "EventEntry">
                                            <td>${entry.duration}</td>
                                            <td>${entry.event}</td>
                                            <td>${entry.parameter!""}</td>
											<#break>
										<#case "ExceptionEntry">
                                            <td colspan="2">Exception: ${entry.exception.class}
												<#if entry.message?has_content>
                                                    <br/> Message: ${entry.message}
												</#if>
                                            </td>
                                            <td>
                                                <div class="nested" id="rep${sub_id}" style="display: block;">
													<#assign sub_id=sub_id+1>
													<#list entry.stack as stackElement>
													${stackElement.className}.${stackElement.methodName}
                                                        () ${stackElement.lineNumber}<br/>
													</#list>
                                                </div>
                                            </td>
											<#break>
										<#case "InfoEntry">
                                            <td colspan="3">${entry.message}</td>
											<#break>
										<#case "ScreenshotEntry">
                                            <td colspan="2">${entry.message!""}</td>
                                            <td align="middle">
                                                <a href="${entry.fileName}" target="_blank"
                                                   title="Click to open full size pattern">
                                                    <img src="${entry.fileName}" style="width:40%;height:40%;border:0">
                                                </a>
                                            </td>
											<#break>
										<#case "SubreportStartEntry">
                                            <td>
                                                <a class='plus' name="rep${sub_id}" href="#"
                                                   onclick='return togglePart(this)'>+</a>${entry.name}
                                            </td>
										<#case "SubreportEndEntry">
											<#break>
										<#case "AssertionFailedEntry">
                                            <td colspan="3">${entry.message!"[AssertionFailedEntry]"}</td>
											<#break>
										<#case "SoftAssertionFailedEntry">
                                            <td colspan="3">${entry.message!"[SoftAssertionFailedEntry]"}</td>
											<#break>
                                        <#case "BrowserLogEntry">
                                            <td colspan="2">JS Console Errors</td>
											<td>${entry.message}</td>
                                            <#break>
									</#switch>
									<#if entry.class.simpleName == "SubreportStartEntry">
                                    </tr>
                                    <tr class="nested" id="rep${sub_id}" style="display: none;">
                                    <td>
										<#assign sub_id=sub_id+1>
                                    <table>
									</#if>

									<#if entry.class.simpleName == "SubreportEndEntry">
                                        </tr></table></td>
									</#if>
									<#if entry.class.simpleName != "SubreportStartEntry">
                                    </tr>
									</#if>
								</#list>
                            </table>
                        </div>
                    </div>
                </div>
					<#assign sub_id=sub_id+1>
				</#list>

            </div>
                <hr>
        </section>
    </div>
</div>
<div class="modal fade" id="help" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h4 class="modal-title" id="myModalLabel">Help</h4>
            </div>
            <div class="modal-body">
                <p>Hotkeys:</p>
                <span class="label label-default">shift + click</span> on item to expand/collapse it with all subitems
                <br>
                <span class="label label-default">ctrl + click</span> or <span class="label label-default">middle mouse click</span>
                on item to open url in new page<br>
                press <span class="label label-default">q</span> to expand/collapse all items <br>
                press <span class="label label-default">e</span> to expand/collapse all <span
                    class="label label-danger">ERROR</span> items <br>
                use <span class="label label-default">[ , ]</span> to navigate between issues
            </div>
        </div>
    </div>
</div>
<footer>
    <div class="col-md-12">
        <span class="muted pull-right">All content © 2014 Cognifide. All rights reserved.</span>
    </div>
</footer>

</body>
</html>
</#escape>
