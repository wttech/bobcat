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
@aemDropdown
Feature: aemDropdown

  Background:
    Given I am logged into AEM as admin

  Scenario: Select by text
    Given I have opened Feedback page
    And my component configuration data is:
      | tab             | type     | label       | value |
      | Title Component | dropdown | Type / Size | Small |
    When I edit the "Title" component located on "par" parsys
    And I configure the properties in the dialog using my component configuration data
    Then Title's component font size is set to "h3"
