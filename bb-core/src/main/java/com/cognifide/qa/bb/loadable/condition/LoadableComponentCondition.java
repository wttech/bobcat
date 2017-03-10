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
package com.cognifide.qa.bb.loadable.condition;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;

/**
 *
 * Interface that should be implemented when providing a condition for {@link LoadableComponent}.
 * Implementations should have only default public constructor as they are instantiated by Guice.
 */
public interface LoadableComponentCondition {

  /**
   *
   * @param subject Instance of the object annotated with {@link LoadableComponent} annotation
   * @param data Data provided by annotation
   * @return Evaluation result
   */
  boolean check(Object subject, LoadableComponent data);

}
