/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
(function ($) {
  var Bobcat = {};
  Bobcat.Quarantine = (function (url) {
    var api = {};
    var serviceUrl = url;
    var modal = document.getElementById('quarantineModal');
    var span = document.getElementsByClassName("close")[0];
    var tommorow = new Date();
    tommorow.setDate(tommorow.getDate() + 1);
    var minDate = tommorow.toISOString().substring(0, 10);
    $('#dueDate').prop('min', minDate);

    api.redrawWithQuarantineContext = function () {
      var features = $('.feature-line');
      $.each(features, function (index, feature) {
        var featureName = $(feature).text().replace('Feature: ', '').trim();
        var scenarios = $(feature).parent().parent().find('.element-name');
        $.each(scenarios, function (index, scenario) {

          var scenarioName = $(scenario).text().trim();
          var havePassed = $(scenario).closest('div').hasClass('passed');
          var testInfo = {
            featureName: featureName,
            scenarioName: scenarioName
          };
          $(scenario).parent().find('button').remove();
          $(scenario).parent().find('.quarantine-description').remove();
          var quarantinedTest = api.getQuarantinedTest(testInfo);
          if (!quarantinedTest && !havePassed) {
            event.preventDefault();
            $(scenario).parent().append('<button class="btn"> Quarantine test </button>');
            $(scenario).parent().find('button').click(function (event) {
              modal.style.display = "block";
              $(modal).find('input[type="button"]').click(function (event) {
                if (document.getElementById('performer').validity.valid) {
                  testInfo.quarantinedBy = $('#performer').val(),
                  testInfo.description = $('#description').val(),
                      testInfo.dueDate = $('#dueDate').val();
                  api.putTestIntoQuarantine(testInfo);
                  $(scenario).closest('div').removeClass('failed');
                  $(scenario).closest('div').addClass('undefined');
                  modal.style.display = "none";
                } else {
                  $('#performer').addClass('not-valid');
                }
              });
            });
          } else if (quarantinedTest) {
            $(scenario).closest('div').removeClass('failed');
            $(scenario).closest('div').addClass('undefined');
            $(scenario).parent().append('<button class="btn"> Remove from quarantine </button>');
            $(scenario).parent().find('button').click(function (event) {
              api.removeFromQuarantine(testInfo);
              $(scenario).closest('div').removeClass('undefined');
              $(scenario).closest('div').addClass('skipped');
              $(scenario).parent().find('.quarantine-description').remove();
            });
            $(scenario).parent().append('<div class="undefined quarantine-description">   This scenario has been put into quarantine by: <b>' + quarantinedTest.quarantinedBy + '</b>. </div>');
            if (quarantinedTest.description) {
              $(scenario).parent().append('<div class="undefined quarantine-description"> Description: ' + quarantinedTest.description + '</div>');
            }
            if (quarantinedTest.dueDate) {
              var date = new Date(quarantinedTest.dueDate);
              $(scenario).parent().append('<div class="undefined quarantine-description"> The scenario will be removed from quarantine on ' + date.toDateString() + '</div>');
            } else {
              $(scenario).parent().append('<div class="undefined quarantine-description"> No due date have been specified for this test. </div>');
            }
          }
        });
      });
    };

    span.onclick = function () {
      modal.style.display = "none";
    };

    window.onclick = function (event) {
      if (event.target == modal) {
        modal.style.display = "none";
      }
    };


    api.loadQuarantinedTests = function () {
      $.ajax
      ({
        type: 'GET',
        url: serviceUrl,
        success: function (data)
        {
          api.quarantinedTests = data;
          api.redrawWithQuarantineContext();
        }
      });
    };
    api.getQuarantinedTest = function (testInfo) {
      var result;
      for (var i = 0; i < api.quarantinedTests.length; i++) {
        if (api.quarantinedTests[i].featureName === testInfo.featureName
            && api.quarantinedTests[i].scenarioName === testInfo.scenarioName) {
          result = api.quarantinedTests[i];
          break;
        }
      }
      return result;
    };
    api.putTestIntoQuarantine = function (testInfo) {
      $.ajax
      ({
        type: 'POST',
        url: serviceUrl,
        data: JSON.stringify(testInfo),
        success: function () {
          api.loadQuarantinedTests();
        }
      });
    };
    api.removeFromQuarantine = function (testInfo) {
      $.ajax
      ({
        type: 'DELETE',
        url: serviceUrl + '?' +
        $.param({'featureName': testInfo.featureName, 'scenarioName': testInfo.scenarioName}),
        success: function () {
          api.loadQuarantinedTests();
        }
      });
    };
    return api;
  });

  $(document).ready(function () {
    document.body.innerHTML += '<div id="quarantineModal" class="modal">'
      + ' <div class = "modal-content">'
      + '   <div class = "modal-header">'
      + '      <span class = "close" > × </span>'
      + '      <h2> Add to quarantine </h2>'
      + '   </div>'
      + '  <div class = "modal-body" >'
      + '     <label for="performer"> By </label> '
      + '     <input type="text" id = "performer" required> <br/>'
      + '     <label for="description"> Description </label>  '
      + '     <input type="text" id = "description"> <br/> '
      + '     <label for="dueDate"> Due Date </label>  '
      + '     <input type ="date" id="dueDate"> <br/>'
      + '     <input type="button" quarantine-action="add" value="Add to quarantine"> '
      + '  </div>'
      + '  <div class = "modal-footer" >'
      + '     <h5> Quarantined tests will not fail the build. Please use this functionality with caution </h5>'
      + '  </div>'
      + ' </div> '
      + '</div>';
    Bobcat.Quarantine('<#serviceUrl>').loadQuarantinedTests();

  });
})(jQuery);