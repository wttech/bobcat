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
package com.cognifide.qa.bb.mapper.field;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.internal.LinkedBindingImpl;

/**
 * Helper class for Page Objects
 */
public final class PageObjectProviderHelper {

  private PageObjectProviderHelper() {
    // Empty for helper class
  }

  /**
   * Retrieves the selector from the provided field. It handles cases when the field uses generics or is a {@link PageObjectInterface}
   *
   * @param field    from which we want to retrieve the selector
   * @param injector necessary for retrieving bindings in case the field is a {@link PageObjectInterface}
   * @return an {@link Optional} that:
   * <ul>
   * <li>contains {@link By} selector specified in the underlying {@link PageObject} annotation</li>
   * <li>is empty in case no selector was specified</li>
   * </ul>
   */
  public static Optional<By> getSelector(Field field, Injector injector) {
    Class<?> type = getGenericType(field).orElse(field.getType());
    return getSelectorFromClass(type, injector);
  }

  public static Optional<By> getSelectorFromClass(Class<?> type, Injector injector) {
    if (type.isAnnotationPresent(PageObjectInterface.class)) {
      type = retrieveBindingOfPageObjectInterface(type, injector);
    }
    return getSelectorFromPageObjectClass(type);
  }

  /**
   * Serves as a check if the field is generic. In case it is, retrieves the type from the parameterized field.
   *
   * @param field from which we would like to retrieve
   * @return an {@link Optional} that:
   * <ul>
   * <li>contains the generic type from the provided (if field type is {@link ParameterizedType})</li>
   * <li>is empty otherwise</li>
   * </ul>
   */
  public static Optional<Class<?>> getGenericType(Field field) {
    Optional<Class<?>> genericType = Optional.empty();
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType) {
      Type firstParameter = ((ParameterizedType) type).getActualTypeArguments()[0];
      if (!(firstParameter instanceof WildcardType)) {
        genericType = Optional.of((Class<?>) firstParameter);
      }
    }
    return genericType;
  }

  private static Class<?> retrieveBindingOfPageObjectInterface(Class<?> type, Injector injector) {
    Binding<?> binding = injector.getBinding(type);
    if (binding instanceof LinkedBindingImpl) {
      type = ((LinkedBindingImpl) binding).getLinkedKey().getTypeLiteral().getRawType();
    }
    return type;
  }

  //private to enforce users to handle the case of PageObjectInterface
  private static Optional<By> getSelectorFromPageObjectClass(Class<?> clazz) {
    PageObject pageObject = Objects.requireNonNull(clazz.getAnnotation(PageObject.class));
    return retrieveSelectorFromAnnotation(pageObject);
  }

  private static Optional<By> retrieveSelectorFromAnnotation(PageObject annotation) {
    String cssValue = annotation.css();
    String xpathValue = annotation.xpath();
    if (StringUtils.isNotEmpty(cssValue) && StringUtils.isNotEmpty(xpathValue)) {
      throw new IllegalArgumentException(
          "Please provide only CSS or XPath selector for your PageObject: " + annotation);
    }

    Optional<By> selector = Optional.empty();
    if (StringUtils.isNotEmpty(cssValue)) {
      selector = Optional.of(By.cssSelector(cssValue));
    } else if (StringUtils.isNotEmpty(xpathValue)) {
      selector = Optional.of(By.xpath(xpathValue));
    }
    return selector;
  }
}
