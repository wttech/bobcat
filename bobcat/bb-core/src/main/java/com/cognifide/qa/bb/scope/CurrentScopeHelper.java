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
package com.cognifide.qa.bb.scope;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Contains methods for working with page objects current scopes.
 * Current scope, annotated by {@link CurrentScope} annotation,
 * is searched within page objects hierarchy.
 */
public class CurrentScopeHelper {

  private static final Logger LOG = LoggerFactory.getLogger(CurrentScopeHelper.class);

  private static final String NO_CURRENT_SCOPE_MESSAGE_FORMAT =
      "%s doesn't contain @CurrentScope WebElement";

  @Inject
  private WebDriver webDriver;

  @Inject
  @Named(ConfigKeys.WEBDRIVER_DEFAULT_TIMEOUT)
  private int defaultTimeout;

  /**
   * Verifies if @CurrentScope from @PageObject class is visible.
   * Current scope is searched in first classes in hierarchy annotated with @PageObject annotation
   *
   * @param pageObject object to verify.
   * @return true if element represented by PageObject class is visible.
   */
  public boolean isCurrentScopeVisible(Object pageObject) {
    webDriver.manage().timeouts().implicitlyWait(Timeouts.MINIMAL, TimeUnit.SECONDS);
    boolean isDisplayed = false;

    try {
      isDisplayed = getCurrentScope(pageObject).isDisplayed();
    } catch (NoSuchElementException | StaleElementReferenceException e) {
      LOG.debug("WebElement is not available at the moment: '{}'", e);
    } finally {
      webDriver.manage().timeouts().implicitlyWait(defaultTimeout, TimeUnit.SECONDS);
    }
    return isDisplayed;
  }

  private WebElement getCurrentScope(Object pageObject) {
    return unwrap(pageObject).flatMap(getDeclaredField(pageObject))
        .orElseThrow(() -> new IllegalArgumentException(getMessage(pageObject)));
  }

  /**
   * Unwraps class wrapped by guice injector.
   *
   * @param pageObject PageObject class.
   * @return unwrapped PageObject class.
   */
  private Optional<Class<?>> unwrap(Object pageObject) {
    Class<?> wrappedClass = pageObject.getClass();
    while (wrappedClass != null && !wrappedClass.isAnnotationPresent(PageObject.class)) {
      wrappedClass = wrappedClass.getSuperclass();
    }
    return Optional.ofNullable(wrappedClass);
  }

  private Function<Class<?>, Optional<WebElement>> getDeclaredField(Object pageObject) {
    return type -> Arrays.stream(type.getDeclaredFields())
        .filter(field -> field.isAnnotationPresent(CurrentScope.class))
        .peek(field -> field.setAccessible(true))
        .findFirst()
        .map(getField(pageObject));
  }

  private Function<Field, WebElement> getField(Object pageObject) {
    return field -> {
      try {
        return (WebElement) field.get(pageObject);
      } catch (IllegalAccessException e) {
        throw new IllegalArgumentException(getMessage(pageObject), e);
      }
    };
  }

  private String getMessage(Object pageObject) {
    return String.format(NO_CURRENT_SCOPE_MESSAGE_FORMAT, pageObject.getClass().getCanonicalName());
  }
}
