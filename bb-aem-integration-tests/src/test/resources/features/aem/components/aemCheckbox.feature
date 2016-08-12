###
# #%L
# Bobcat Parent
# %%
# Copyright (C) 2016 Cognifide Ltd.
# %%
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
# 
#      http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# #L%
###
@aemCheckbox
Feature: aemCheckbox
  In order to toggle display of labels
  As an author
  I want to click label checkbox inside component dialog

  Background:
    Given I am logged into AEM as admin

  Scenario: As an author I can show title of Text Field Component
    Given I have opened Feedback page
    And parsys I'm working with is named "par"
    And my component configuration data is:
      | tab            | type       | label        | value |
      | Title and Text | text field | Element Name | name  |
      | Title and Text | text field | Title        | Name  |
      | Title and Text | checkbox   | Hide Title   | FALSE |
      | Constraints    | checkbox   | Required     | TRUE  |
    And I configure the "Text Field Component" component index "2" using my component configuration data
    Then Text Field Component with index "2" Title field is visible

  Scenario: As an author I can hide title of Text Field Component
    Given I have opened Feedback page
    And parsys I'm working with is named "par"
    And my component configuration data is:
      | tab            | type       | label        | value |
      | Title and Text | text field | Element Name | name  |
      | Title and Text | text field | Title        | Name  |
      | Title and Text | checkbox   | Hide Title   | TRUE  |
      | Constraints    | checkbox   | Required     | TRUE  |
    And I configure the "Text Field Component" component index "2" using my component configuration data
    Then Text Field Component with index "2" Title field is not visible
