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
package com.cognifide.qa.bb.loadablecomponent;

import com.cognifide.qa.bb.qualifier.LoadableComponent;

public class Loadable {

  private int delay;

  private int timeout;

  private Class conditionImplementation;

  public Loadable(LoadableComponent loadableComponent) {
    if (loadableComponent != null) {
      this.conditionImplementation = loadableComponent.conditionImplementation();
      this.timeout = loadableComponent.timeout();
      this.delay = loadableComponent.delay();
    }
  }

  public int getDelay() {
    return delay;
  }

  public int getTimeout() {
    return timeout;
  }

  public Class getConditionImplementation() {
    return conditionImplementation;
  }

}
