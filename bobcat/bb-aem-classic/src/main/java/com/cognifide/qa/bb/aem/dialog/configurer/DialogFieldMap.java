/*-
 * #%L
 * Bobcat
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
package com.cognifide.qa.bb.aem.dialog.configurer;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.aem.DialogComponent;
import com.google.inject.Singleton;

/**
 * This class stores the classes of dialog fields.
 *
 * @author Mariusz Kubiś Date: 13.05.15
 * @author karol.kujawiak
 */
@Singleton
public class DialogFieldMap {

  private static final Logger LOG = LoggerFactory.getLogger(DialogFieldMap.class);

  private final Map<String, Class> fields = new HashMap<>();

  /**
   * Gets class for dialog field.
   *
   * @param componentName dialog field name
   * @return field class
   */
  public synchronized Class<?> getField(String componentName) {
    if (fields.isEmpty()) {
      initializeFields();
    }
    if (fields.containsKey(componentName)) {
      return fields.get(componentName);
    }
    String msg = "There is no implementation for: '" + componentName + "' field type";
    LOG.error(msg);
    throw new IllegalArgumentException(msg);
  }

  private void initializeFields() {
    Collection<URL> urls = ClasspathHelper.forJavaClassPath();
    ConfigurationBuilder builder = new ConfigurationBuilder().addUrls(urls);
    Reflections reflections = new Reflections(builder);

    for (Class<?> clazz : findDialogComponents(reflections)) {
      String componentName = getComponentName(clazz);
      fields.put(componentName, clazz);
      LOG.debug("added new field: '{}' for component name: '{}'",
          clazz.getSimpleName(), componentName);
    }
  }

  private Set<Class<?>> findDialogComponents(Reflections reflections) {
    Set<Class<?>> dialogComponents = reflections.getTypesAnnotatedWith(DialogComponent.class);
    if (LOG.isDebugEnabled()) {
      Set<URL> urls = reflections.getConfiguration().getUrls();
      StringBuilder sb = new StringBuilder();
      for (URL url : urls) {
        sb.append("\n\t").append(url.toExternalForm());
      }
      LOG.debug("found '{}' dialog components. scanned urls '{}'{}",
          dialogComponents.size(), urls.size(), sb.toString());
    }
    return dialogComponents;
  }

  private String getComponentName(Class clazz) {
    DialogComponent component = (DialogComponent) clazz.getAnnotation(DialogComponent.class);
    return component.value();
  }
}
