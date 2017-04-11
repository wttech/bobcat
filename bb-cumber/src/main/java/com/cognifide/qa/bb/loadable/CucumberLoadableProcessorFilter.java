/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.loadable;

import com.cognifide.qa.bb.loadable.mapper.TestClassInjectionListener;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.java.StepDefAnnotation;
import cucumber.runtime.java.guice.ScenarioScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;

/**
 * Registers Cucumber test runners in context of Loadable Components
 */
public class CucumberLoadableProcessorFilter implements LoadableProcessorFilter, TypeListener {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(CucumberLoadableProcessorFilter.class);

  @Override
  public boolean isApplicable(Class clazz) {
    return clazz != null
        && ((clazz.isAnnotationPresent(PageObject.class) && isStepsImplClassAbove())
            || isStepsImplementationClass(clazz));
  }

  private boolean isStepsImplClassAbove() {
    try {
      for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
        if (isStepsImplementationClass(
            Class.forName(stackTraceElement.getClassName()))) {
          return true;
        }
      }
    } catch (ClassNotFoundException e) {
      LOGGER.error("Didn't find class from the execution stack", e);
    }
    return false;
  }

  @Override
  public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
    if (isStepsImplementationClass(type.getRawType())) {
      encounter.register(new TestClassInjectionListener(encounter));
    }
  }

  private static boolean isStepsImplementationClass(Class clazz) {
    return clazz.isAnnotationPresent(ScenarioScoped.class)
        || Arrays.stream(clazz.getDeclaredMethods())
            .anyMatch(method -> Arrays.stream(method.getAnnotations())
                .anyMatch(annotation -> isHookAnnotation(annotation)
                    || isStepDefinitionAnnotation(annotation)));
  }

  private static boolean isHookAnnotation(Annotation annotation) {
    Class annotationType = annotation.annotationType();
    return annotationType.equals(Before.class) || annotationType.equals(After.class);
  }

  private static boolean isStepDefinitionAnnotation(Annotation annotation) {
    Class annotationType = annotation.annotationType();
    return annotationType.getAnnotation(StepDefAnnotation.class) != null;
  }
}
