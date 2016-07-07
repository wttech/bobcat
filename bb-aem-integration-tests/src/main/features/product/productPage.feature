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
@productPage
Feature: Making an order

  Scenario: User is able to open product page
    Given I have opened men page
    And men page is displayed
    When I click product link
    Then I should see product page

  Scenario: User is able to add product to the cart
    Given I have opened product page
    When I click add to cart button
    Then cart page is displayed
    And recommendations are loaded
    When I click checkout button
    Then checkout page is displayed