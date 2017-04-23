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
package com.cognifide.qa.bb.loadable.hierarchy.util;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.cognifide.qa.bb.loadable.context.ConditionContext;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class LoadableComponentsUtil {

  private LoadableComponentsUtil() {
    // util class
  }

  /**
   *
   * @param field PageObject that can be annotated with {@link LoadableComponent} annotation
   * @return List of Condition Context objects describing conditions provided in the {@link LoadableComponent}
   * annotations
   */
  public static List<ConditionContext> getConditionsFormField(Field field) {
    List<ConditionContext> fieldConditionContext = new ArrayList<>();
    for (LoadableComponent loadableComponent : field.getAnnotationsByType(LoadableComponent.class)) {
      fieldConditionContext.add(new ConditionContext(loadableComponent, field.getName(), field.
              getDeclaringClass().getName()));
    }
    return fieldConditionContext;
  }

}
