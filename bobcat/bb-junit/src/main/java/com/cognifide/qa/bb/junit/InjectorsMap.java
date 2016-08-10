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
package com.cognifide.qa.bb.junit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Singleton that stores injectors for modules and modules groups.
 * The same injector is used for tests that have same modules declared
 * within <code>@Modules</code> annotation.
 */
public enum InjectorsMap {

  INSTANCE;

  private static final Logger LOG = LoggerFactory.getLogger(InjectorsMap.class);

  private static final String SEPARATOR_FOR_MODULES_CLASS_NAMES = ",";

  private static final Map<String, Injector> INJECTORS = new ConcurrentHashMap<>();

  /**
   * @param moduleClass class of Module implementation
   * @return the proper {@link Injector} based on the module class
   * @throws InitializationError if the module creation failed
   */
  public synchronized Injector forModule(Class<? extends Module> moduleClass)
      throws InitializationError {
    String keyForModule = moduleClass.getName();

    if (!INJECTORS.containsKey(keyForModule)) {
      Module instance = createInstance(moduleClass);
      List<Module> modules = Arrays.asList(instance);
      Injector newInjector = Guice.createInjector(modules);

      INJECTORS.put(keyForModule, newInjector);
    }
    return INJECTORS.get(keyForModule);
  }

  /**
   * @param testClassToRun class to run
   * @return the proper {@link Injector} based on the modules inside the {@link Modules} annotation.
   * @throws InitializationError if the test class is malformed or if module creation failed
   */
  synchronized Injector forClass(Class<?> testClassToRun) throws InitializationError {
    Set<Class<? extends Module>> modules = retrieveModules(testClassToRun);
    String keyForModules = createKeyFromModules(modules);

    if (!INJECTORS.containsKey(keyForModules)) {
      List<Module> modulesFromAnnotation = getModulesInstances(modules);
      Injector newInjector = Guice.createInjector(modulesFromAnnotation);

      INJECTORS.put(keyForModules, newInjector);
    }
    return INJECTORS.get(keyForModules);
  }

  /**
   * @param modules list of modules within <code>@Modules(...)</code>annotation
   * @return representation of sorted modules names (fully qualified)
   */
  private String createKeyFromModules(Set<Class<? extends Module>> modules) {
    List<Class<? extends Module>> modulesList = new ArrayList<>(modules);
    Collections.sort(modulesList, (o1, o2) -> o1.getName().compareTo(o2.getName()));
    return Joiner.on(SEPARATOR_FOR_MODULES_CLASS_NAMES).join(modulesList);
  }

  private Set<Class<? extends Module>> retrieveModules(Class<?> annotatedClass)
      throws InitializationError {
    Modules annotation = annotatedClass.getAnnotation(Modules.class);
    if (annotation == null) {
      throw new InitializationError(
          "Missing @Modules annotation for '" + annotatedClass.getCanonicalName() + "' class.");
    } else {
      Class<? extends Module>[] modulesClasses = annotation.value();
      // creates a copy of array
      Set<Class<? extends Module>> modules = new HashSet<>(modulesClasses.length);
      Collections.addAll(modules, modulesClasses);
      return modules;
    }
  }

  private List<Module> getModulesInstances(Set<Class<? extends Module>> modules)
      throws InitializationError {
    List<Module> instances = new ArrayList<>(modules.size());
    for (Class<? extends Module> moduleClass : modules) {
      Module instance = createInstance(moduleClass);
      instances.add(instance);
    }
    return instances;
  }

  private Module createInstance(Class<? extends Module> moduleClass) throws InitializationError {
    Module instance;
    try {
      instance = moduleClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      LOG.error("error while creating instance of: '{}'", moduleClass.getName(), e);
      throw new InitializationError(e);
    }
    return instance;
  }

}
