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

import static java.util.stream.Collectors.toSet;
import static org.junit.platform.commons.support.AnnotationSupport.findAnnotation;
import static org.junit.platform.commons.support.AnnotationSupport.findRepeatableAnnotations;

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
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * Extension that will start guice and is responsible for the injections to test instance
 *
 */
public class GuiceExtension implements TestInstancePostProcessor {

  private static final Namespace NAMESPACE =
      Namespace.create("com", "cognifide", "qa", "bb", "junit", "guice");

  @Override
  public void postProcessTestInstance(Object testInstance, ExtensionContext context)
      throws Exception {

    getOrCreateInjector(context).ifPresent(injector -> injector.injectMembers(testInstance));

  }

  private static Optional<Injector> getOrCreateInjector(ExtensionContext context)
      throws NoSuchMethodException,
      InstantiationException,
      IllegalAccessException,
      InvocationTargetException {
    if (!context.getElement().isPresent()) {
      return Optional.empty();
    }

    AnnotatedElement element = context.getElement().get();
    Store store = context.getStore(NAMESPACE);

    Injector injector = store.get(element, Injector.class);
    if (injector == null) {
      injector = createInjector(context);
      store.put(element, injector);
    }

    return Optional.of(injector);
  }

  private static Injector createInjector(ExtensionContext context)
      throws NoSuchMethodException,
      InstantiationException,
      IllegalAccessException,
      InvocationTargetException {
    Optional<Injector> parentInjector = getParentInjector(context);
    List<? extends Module> modules = getNewModules(context);

    return parentInjector
        .map(injector -> injector.createChildInjector(modules))
        .orElseGet(() -> Guice.createInjector(modules));
  }

  private static Optional<Injector> getParentInjector(ExtensionContext context)
      throws NoSuchMethodException,
      InstantiationException,
      IllegalAccessException,
      InvocationTargetException {
    if (context.getParent().isPresent()) {
      return getOrCreateInjector(context.getParent().get());
    }
    return Optional.empty();
  }

  private static List<? extends Module> getNewModules(ExtensionContext context)
      throws NoSuchMethodException,
      InstantiationException,
      IllegalAccessException,
      InvocationTargetException {
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

  private static Set<Class<? extends Module>> getNewModuleTypes(ExtensionContext context) {
    if (!context.getElement().isPresent()) {
      return Collections.emptySet();
    }

    Set<Class<? extends Module>> moduleTypes = getModuleTypes(context.getElement().get());
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
    // TODO: Cache?

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

    return Arrays.stream(classes.get()).collect(toSet());

  }
}
