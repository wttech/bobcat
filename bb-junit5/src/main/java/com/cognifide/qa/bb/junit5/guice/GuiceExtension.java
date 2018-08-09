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
package com.cognifide.qa.bb.junit5.guice;

import static com.cognifide.qa.bb.junit5.JUnit5Constants.NAMESPACE;
import static java.util.stream.Collectors.toSet;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * Extension that will start guice and is responsible for the injections to test instance Based on
 * <a href="https://github.com/JeffreyFalgout/junit5-extensions/tree/master/guice-extension">Guice
 * Extension</a>
 */
public class GuiceExtension implements TestInstancePostProcessor {

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {

    getOrCreateInjector(context).ifPresent(injector -> injector.injectMembers(testInstance));

  }

  /**
   * Create {@link Injector} or get existing one from test context
   */
  private static Optional<Injector> getOrCreateInjector(ExtensionContext context)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

    Optional<AnnotatedElement> optionalAnnotatedElement = context.getElement();
    if (!optionalAnnotatedElement.isPresent()) {
      return Optional.empty();
    }

    AnnotatedElement element = optionalAnnotatedElement.get();
    Store store = context.getStore(NAMESPACE);

    Injector injector = store.get(element, Injector.class);
    if (injector == null) {
      injector = createInjector(context);
      store.put(element, injector);
    }

    return Optional.of(injector);
  }

  /**
   * Creates {@link Injector} from test context
   */
  private static Injector createInjector(ExtensionContext context)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Optional<Injector> parentInjector = getParentInjector(context);
    List<? extends Module> modules = getNewModules(context);

    return parentInjector
        .map(injector -> injector.createChildInjector(modules))
        .orElseGet(() -> Guice.createInjector(modules));
  }

  /**
   * Retrieves {@link Injector} from parent test context
   */
  private static Optional<Injector> getParentInjector(ExtensionContext context)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    final Optional<ExtensionContext> optionalParent = context.getParent();
    if (optionalParent.isPresent()) {
      return getOrCreateInjector(optionalParent.get());
    }
    return Optional.empty();
  }

  /**
   * Gets all new {@link Module} instances for injections
   */
  private static List<? extends Module> getNewModules(ExtensionContext context)
      throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
    Set<Class<? extends Module>> moduleTypes = getNewModuleTypes(context);
    List<Module> modules = new ArrayList<>(moduleTypes.size());
    for (Class<? extends Module> moduleType : moduleTypes) {
      Constructor<? extends Module> moduleCtor = moduleType.getDeclaredConstructor();
      moduleCtor.setAccessible(true);

      modules.add(moduleCtor.newInstance());
    }

    context.getElement().ifPresent(element -> {
      if (element instanceof Class) {
        modules.add(new AbstractModule() {
          @Override
          protected void configure() {
            requestStaticInjection((Class<?>) element);
          }
        });
      }
    });

    return modules;
  }

  /**
   * Returns all new {@link Module} declared in current context (returns empty set if modules where
   * already declared)
   */
  private static Set<Class<? extends Module>> getNewModuleTypes(ExtensionContext context) {
    Optional<AnnotatedElement> optionalAnnotatedElement = context.getElement();
    if (!optionalAnnotatedElement.isPresent()) {
      return Collections.emptySet();
    }

    Set<Class<? extends Module>> moduleTypes = getModuleTypes(optionalAnnotatedElement.get());
    context.getParent()
        .map(GuiceExtension::getContextModuleTypes)
        .ifPresent(moduleTypes::removeAll);

    return moduleTypes;
  }

  private static Set<Class<? extends Module>> getContextModuleTypes(ExtensionContext context) {
    return getContextModuleTypes(Optional.of(context));
  }

  /**
   * Returns module types that are present on the given context or any of its enclosing contexts.
   */
  private static Set<Class<? extends Module>> getContextModuleTypes(
      Optional<ExtensionContext> context) {

    Set<Class<? extends Module>> contextModuleTypes = new LinkedHashSet<>();
    while (context.isPresent() && (hasAnnotatedElement(context) || hasParent(context))) {
      context
          .flatMap(ExtensionContext::getElement)
          .map(GuiceExtension::getModuleTypes)
          .ifPresent(contextModuleTypes::addAll);
      context = context.flatMap(ExtensionContext::getParent);
    }

    return contextModuleTypes;
  }

  private static boolean hasAnnotatedElement(Optional<ExtensionContext> context) {
    return context.flatMap(ExtensionContext::getElement).isPresent();
  }

  private static boolean hasParent(Optional<ExtensionContext> context) {
    return context.flatMap(ExtensionContext::getParent).isPresent();
  }

  private static Set<Class<? extends Module>> getModuleTypes(AnnotatedElement element) {

    Optional<Class<? extends Module>[]> classes = findAnnotation(element, Modules.class)
        .map(Modules::value);

    if (classes.isPresent()) {
      return Arrays.stream(classes.get()).collect(toSet());
    }
    return Sets.newHashSet();

  }
}
