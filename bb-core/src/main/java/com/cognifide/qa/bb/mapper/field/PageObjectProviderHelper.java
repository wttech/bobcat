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
   * Gets selector from {@link PageObject} from the provided field.
   * Also handles the case of {@link PageObjectInterface}.
   *
   * @param field    class field
   * @param injector injector in which search for proper bindings will be done in
   * @return an {@link Optional} containing the selector
   */
  public static Optional<By> getSelectorFromPageObjectField(Field field, Injector injector) {
    return field.getType().isAnnotationPresent(PageObjectInterface.class) ?
        getSelectorFromPageObjectInterfaceType(field.getType(), injector)
        : getSelectorFromPageObject(field);
  }

  /**
   * Gets selector from {@link PageObject} if class annotated by this annotation is used in parameterized types, e.g. lists.
   * Also handles the case of {@link PageObjectInterface}.
   *
   * @param field    class field
   * @param injector injector in which search for proper bindings will be done in
   * @return an {@link Optional} containing the selector
   */
  public static Optional<By> getSelectorFromGenericPageObject(Field field, Injector injector) {
    Class<?> genericType = getGenericType(field);
    if (genericType != null && genericType.isAnnotationPresent(PageObjectInterface.class)) {
      return getSelectorFromPageObjectInterfaceType(genericType, injector);
    } else {
      return retrieveSelectorFromPageObjectField(field, true);
    }
  }

  /**
   * Retrieves {@link By} selector from {@link PageObject} annotation when the provided type is a {@link PageObjectInterface}.
   * It searches through the bindings of the provided Guice's injector.
   *
   * @param type     of the PageObject to retrieve the selector from
   * @param injector used to search through its bindings to find the implementation of the provided interface
   * @return
   */
  public static Optional<By> getSelectorFromPageObjectInterfaceType(Class<?> type,
      Injector injector) {
    Binding<?> binding = injector.getBinding(type);
    if (binding instanceof LinkedBindingImpl) {
      return getSelectorFromPageObjectClass(
          ((LinkedBindingImpl) binding).getLinkedKey().getTypeLiteral().getRawType());
    }
    throw new IllegalArgumentException("Could not retrieve selector from the type: " + type);
  }

  /**
   * Gets selector from {@link PageObject}
   *
   * @param field class field
   * @return selector
   */
  public static Optional<By> getSelectorFromPageObject(Field field) {
    return retrieveSelectorFromPageObjectField(field, false);
  }

  /**
   * Gets generic type from field (if field type is {@link ParameterizedType})
   *
   * @param field class field
   * @return generic type
   */
  public static Class<?> getGenericType(Field field) {
    Type type = field.getGenericType();
    if (type instanceof ParameterizedType) {
      Type firstParameter = ((ParameterizedType) type).getActualTypeArguments()[0];
      if (!(firstParameter instanceof WildcardType)) {
        return (Class<?>) firstParameter;
      }
    }
    return null;
  }

  /**
   * Retrieves {@link By} selector from {@link PageObject} annotation of the provided class.
   *
   * @param clazz type of the object annotated with {@link PageObject}
   * @return an {@link Optional} that can contain following values:
   * <ul>
   * <li>{@link By#cssSelector}</li>
   * <li>{@link By#xpath}</li>
   * <li>empty if no selector was provided</li>
   * </ul>
   */
  public static Optional<By> getSelectorFromPageObjectClass(Class<?> clazz) {
    PageObject pageObject = Objects.requireNonNull(clazz.getAnnotation(PageObject.class));
    return retrieveSelectorFromAnnotation(pageObject);
  }

  private static Optional<By> retrieveSelectorFromPageObjectField(Field field, boolean useGeneric) {
    PageObject pageObject = useGeneric
        ? Objects.requireNonNull(getGenericType(field)).getAnnotation(PageObject.class)
        : field.getType().getAnnotation(PageObject.class);

    return retrieveSelectorFromAnnotation(pageObject);
  }

  private static Optional<By> retrieveSelectorFromAnnotation(PageObject annotation) {
    String cssValue = annotation.css();
    String xpathValue = annotation.xpath();
    if (StringUtils.isNotEmpty(cssValue) && StringUtils.isNotEmpty(xpathValue)) {
      throw new IllegalArgumentException(
          "Please provide only CSS or XPath selector for your PageObjectL: " + annotation);
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
