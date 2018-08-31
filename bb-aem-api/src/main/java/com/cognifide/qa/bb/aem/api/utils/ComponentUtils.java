/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
 * %%
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
 * #L%
 */
package com.cognifide.qa.bb.aem.api.utils;

import java.lang.annotation.Annotation;

import com.cognifide.qa.bb.aem.api.Dialog;
import com.cognifide.qa.bb.aem.api.Hoverbar;
import com.cognifide.qa.bb.aem.api.qualifiers.Component;

public class ComponentUtils {
  public static String getName(Class component) {
    return getAnnotation(component).name();
  }

  public static Class<? extends Dialog> getDialog(Class component) {
    return getAnnotation(component).dialog();
  }

  public static Class<? extends Hoverbar> getHoverbar(Class component) {
    return getAnnotation(component).hoverbar();
  }

  private static Component getAnnotation(Class component) {
    Annotation componentAnnotation = component.getAnnotation(Component.class);
    if (componentAnnotation instanceof Component) {
      return (Component) componentAnnotation;
    } else {
      throw new IllegalArgumentException(component
          + " is not a valid Component! Either decorate your Page Object with @Component or provide a valid type.");
    }
  }
}
