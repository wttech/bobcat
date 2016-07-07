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
@aemLogin
Feature: Login
  In order to edit site
  As a site administrator
  I want to log in

  Scenario Outline: Login with valid credentials
    Given I have opened login page
    And I am not logged in
    When I enter following credentials "<username>", "<password>"
    And I press login button
    Then I should see welcome page

    Examples:
      | username | password |
      | author   | author   |

  Scenario: Fail to login with invalid credentials
    Given I have opened login page
    And I am not logged in
    When I enter following credentials "invalid", "user"
    And I press login button
    Then Authorization error message should appear