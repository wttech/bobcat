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
package com.cognifide.qa.bb.loadable.context;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;

/**
 *
 * This wraps the {@link LoadableComponent} annotation data with information helpful in case of errors
 */
public class ConditionContext {

  private LoadableComponent loadableComponent;

  private String fieldName;

  private String declaringClassName;

  public ConditionContext(LoadableComponent loadableComponent, String fieldName, String enlosingClassName) {
    if (loadableComponent != null) {
      this.loadableComponent = loadableComponent;
      this.fieldName = fieldName;
      this.declaringClassName = enlosingClassName;
    }
  }

  /**
   *
   * @return name of field annotated with {@link LoadableComponent} which is currently under processing
   */
  public String getFieldName() {
    return fieldName;
  }

  /**
   *
   * @return name of class that declares field annotated with {@link LoadableComponent} which is currently
   * under processing
   */
  public String getDeclaringClassName() {
    return declaringClassName;
  }

  /**
   *
   * @return {@link LoadableComponent} data from annotation
   */
  public LoadableComponent getLoadableComponent() {
    return loadableComponent;
  }
}
