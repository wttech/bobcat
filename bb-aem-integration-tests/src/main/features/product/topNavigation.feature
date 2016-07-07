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
@topNavigation
Feature: Top navigation

  Scenario: Men page is available from top navigation
    Given I open home page
    When I click men link
    Then men page is displayed

  Scenario: Women page is available from top navigation
    Given I open home page
    When I click woman link
    Then woman page is displayed
