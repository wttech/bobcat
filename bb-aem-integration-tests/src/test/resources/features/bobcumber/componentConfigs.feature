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
@component-configs
Feature: Component Configs

  Scenario: I can create component config using ComponentConfigBuilder

    Given my component configuration data is:
      | tab     | type                     | label                  | value               |
      | General | text area                | Text Area              | textarea            |
      | Tab1    | text field               | Text Field             | textfield           |
      | Tab1    | path field               | Path Field             | Node1/Node2/Node3   |
      | Tab2    | dropdown                 | Dropdown               | dropdown            |
      | Tab3    | checkbox                 | Checkbox               | TRUE                |
      | Tab4    | rich text                | Rich Text              | richtext            |
      | Tab4    | tags                     | Tags                   | Namespace:Tag1/Tag2 |
      | Tab4    | radio group              | Radio Group            | radiogroup          |
      | Tab4    | multifield#1#radio group | Multifield#Radio Group | radiogroup          |
      | Tab4    | fieldset#radio group     | Fieldset#Radio Group   | radiogroup          |

    # the configuration data is provided twice:
    # 1. in the above table
    # 2. in the below step as "default"
    #
    # this is on purpose as we want to test the ComponentConfigBuilder class
    When I build a "default" configuration with ComponentConfigBuilder

    Then "default" config matches the Data Table

  Scenario: I can use component configs defined in JSON files

    Given my component configuration data is:
      | tab     | type                     | label                  | value               |
      | General | text area                | Text Area              | textarea            |
      | Tab1    | text field               | Text Field             | textfield           |
      | Tab1    | path field               | Path Field             | Node1/Node2/Node3   |
      | Tab2    | dropdown                 | Dropdown               | dropdown            |
      | Tab3    | checkbox                 | Checkbox               | TRUE                |
      | Tab4    | rich text                | Rich Text              | richtext            |
      | Tab4    | tags                     | Tags                   | Namespace:Tag1/Tag2 |
      | Tab4    | radio group              | Radio Group            | radiogroup          |
      | Tab4    | multifield#1#radio group | Multifield#Radio Group | radiogroup          |
      | Tab4    | fieldset#radio group     | Fieldset#Radio Group   | radiogroup          |

    # the configuration data is provided twice:
    # 1. in the above table
    # 2. in "example.json" file
    #
    # this is on purpose as we want to test the JsonToComponentConfig class
    When I use the "example" configuration from JSON

    Then "example" config matches the Data Table
