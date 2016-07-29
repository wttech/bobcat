/*-
 * #%L
 * Bobcat Parent
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.touch.data.componentproperties;

import static java.util.stream.Collectors.toMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.touch.data.components.Components;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class ComponentState {
  private static final Logger LOG = LoggerFactory.getLogger(ComponentState.class);

  @Inject
  private Components components;

  public Map<String, String> getActual(Object object) {
    Map<String, String> actual = Maps.newHashMap();
    for (Method method : object.getClass().getSuperclass().getMethods()) {
      Property annotation = method.getAnnotation(Property.class);
      if (annotation != null) {
        try {
          actual.put(annotation.value(), String.valueOf(method.invoke(object)));
        } catch (IllegalAccessException | InvocationTargetException e) {
          LOG.error(
              String.format("Could not invoke the %s method in %s component", method.getName(),
                  object.getClass().getName()), e);
        }
      }
    }
    return actual;
  }

  /**
   * Translates provided configuration to a map containing expected values in form of:
   * <code>tab:label -&gt; value</code>
   * Filters out all complex fields e.g. Multifield
   *
   * @param config        component configuration map
   * @param componentName name of the component
   * @return map of the expected state
   */
  public Map<String, String> getExpected(Map<String, List<FieldConfig>> config,
      String componentName) {
    return config.entrySet().stream() //
        .flatMap(entry -> entry.getValue().stream() //
            .filter(fieldConfig -> fieldConfig.getValue() instanceof String)
            .map(fieldConfig -> new AbstractMap.SimpleEntry<>(
                toProperty(entry.getKey(), fieldConfig.getLabel()),
                translateValue(componentName, fieldConfig.getValue())))) //
        .collect(toMap( //
            Map.Entry::getKey, //
            Map.Entry::getValue));
  }

  private String toProperty(String tab, String label) {
    return String
        .format("%s:%s", tab.toLowerCase().replace(" ", "-"),
            label.toLowerCase().replace(" ", "-"));
  }

  private String translateValue(String component, Object rawValue) {
    return Optional.ofNullable(components.getVariantValue(component, String.valueOf(rawValue)))
        .orElseGet(() -> String.valueOf(rawValue));
  }
}
