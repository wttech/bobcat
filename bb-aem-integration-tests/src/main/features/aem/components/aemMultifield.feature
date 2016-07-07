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
@aemMultifield
Feature: aemMultifield

  Background:
    Given I am logged into AEM as admin

  Scenario: Multifield configuration works as expected
    Given I have opened Customer survey page
    And parsys I'm working with is named "par"
    And my component configuration data is:
      | tab            | type                    | label          | value            |
      | Title and Text | multifield#0#text field | Items#no label | multifield value |
    When I edit the "Radio Group" component
    And I removed all items from multifield
    Then multifield contains 0 elements
    When I added 1 item to multifield
    And I set the rest of the properties using my component configuration data
    Then component is configured according to configuration data

  Scenario: Multifield configuration for radio group
    Given I have opened Customer survey page
    And parsys I'm working with is named "par"
    And my component configuration data is:
      | tab            | type                    | label          | value            |
      | Title and Text | multifield#0#text field | Items#no label | multifield value |
    When I edit the Radio Group component
    And I removed all items from multifield
    Then multifield contains 0 elements
    When I added 1 item to multifield
    And I configure the properties in the dialog using my component configuration data
    Then Radio Group component has " multifield value" item
