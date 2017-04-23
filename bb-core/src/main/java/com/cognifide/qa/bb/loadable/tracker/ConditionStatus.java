/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cognifide.qa.bb.loadable.tracker;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;

/**
 * Describes status of condition provided in {@link LoadableComponent} annotated fields.
 */
public enum ConditionStatus {

  SUCCESS("Success"),
  FAIL("Fail"),
  DIDINT_RUN("Didn't run");

  private final String message;

  ConditionStatus(String message) {
    this.message = message;
  }

  /**
   *
   * @return User friendly status message
   */
  public String getMessage() {
    return message;
  }

}
