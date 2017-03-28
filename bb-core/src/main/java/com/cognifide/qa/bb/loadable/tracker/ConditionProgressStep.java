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
 *
 * Holds the information about evaluating single condition from {@link LoadableComponent} annotated fields.
 */
public class ConditionProgressStep {

  private final String loadableComponentInfo;

  private ConditionStatus stepStatus;

  public ConditionProgressStep(String loadableComponentInfo) {
    this.loadableComponentInfo = loadableComponentInfo;
    this.stepStatus = ConditionStatus.DIDINT_RUN;
  }

  /**
   *
   * @param stepStatus Step status
   */
  public void setStepStatus(ConditionStatus stepStatus) {
    this.stepStatus = stepStatus;
  }

  /**
   *
   * @return Text information about the progress step.
   */
  public String getLoadableComponentInfo() {
    return loadableComponentInfo;
  }

  /**
   *
   * @return Step's current status
   */
  public ConditionStatus getStepStatus() {
    return stepStatus;
  }

  @Override
  public String toString() {
    return loadableComponentInfo + " (" + stepStatus.getMessage() + ")";
  }

}
