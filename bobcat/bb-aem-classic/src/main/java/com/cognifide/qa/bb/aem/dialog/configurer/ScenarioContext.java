package com.cognifide.qa.bb.aem.dialog.configurer;

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


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.runtime.java.guice.ScenarioScoped;

/**
 * Collects the objects shared between steps.
 */
@ScenarioScoped
public class ScenarioContext {

  private Map<String, Object> context;

  /**
   * Constructs the ScenarioContext.
   */
  public ScenarioContext() {
    context = new HashMap<>();
  }

  /**
   * Adds an object to the context.
   *
   * @param key Identifier of the object in the context.
   * @param obj Object to be stored in the context.
   */
  public void add(String key, Object obj) {
    context.put(key, obj);
  }

  /**
   * Fetches the object from the context. Throws IllegalArgumentException if the provided key is missing.
   *
   * @param key  Identifier of the object in the context.
   * @param type Type of the object.
   * @param <T> class of object type
   * @return The object fetched from the context.
   */
  @SuppressWarnings("unchecked")
  public <T> T get(String key, Class<T> type) {
    if (context.containsKey(key)) {
      return (T) context.get(key);
    } else {
      throw new IllegalArgumentException("Specified key does not exist in ScenarioContext map");
    }
  }

  /**
   * Fetches the string from the context, short for "get(key, String.class)".
   *
   * @param key Identifier of the object in the context.
   * @return value for the key
   */
  public String getString(String key) {
    return get(key, String.class);
  }

  /**
   * Fetches the list from the context, short for "get(key, List.class)".
   *
   * @param key Identifier of the object in the context.
   * @param clazz list class
   * @param <T> list class
   * @return value for the key
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> getList(String key, Class<T> clazz) {
    return get(key, List.class);
  }

  /**
   * Method for checking if scenarioContext map contains a mapping for the specified
   * key.
   *
   * @param key Identifier of the object in the context.
   * @return true - if scenarioContext map contains the specified key, otherwise false
   */
  public boolean containsKey(String key) {
    return context.containsKey(key);
  }
}
