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
@aemFieldset
Feature: aemFieldset

  Background:
    Given I am logged into AEM as admin

  Scenario: Configuring text field in fieldset
    Given I have opened Feedback page
    And I edit start form component
    And my component configuration data is:
      | tab      | type                | label                             | value           |
      | Advanced | fieldset#text field | Action Configuration#Content Path | /tmp/formtest/* |
    When I set the rest of the properties using my component configuration data
    Then component is configured according to configuration data
