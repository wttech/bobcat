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

import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.internal.LinkedBindingImpl;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

/**
 * Helper class for Page Objects
 */
public final class PageObjectProviderHelper {

  private PageObjectProviderHelper() {
    // Empty for helper class
  }

  /**
   * Gets selector from {@link PageObject} if class annotated by this annotation is used in list
   *
   * @param field class field
   * @param originalInjector
   * @return selector
   */
  public static By getSelectorFromGenericPageObject(Field field,
      Injector originalInjector) {
    Class<?> genericType = getGenericType(field);
    if(genericType.isAnnotationPresent(
        PageObjectInterface.class)){
      Binding<?> binding = originalInjector.getBinding(genericType);
      if(binding instanceof LinkedBindingImpl){
        return  PageObjectProviderHelper.retrieveSelectorFromPageObjectInterface(((LinkedBindingImpl) binding).getLinkedKey().getTypeLiteral().getRawType());
      };
    } else {
      return retrieveSelectorFromPageObject(field, true);
    }
    throw new IllegalArgumentException(
        "PageObject has to have defined selector when used with FindPageObject annotation");
  }

  /**
   * Gets selector from {@link PageObject}
   *
   * @param field class field
   * @return selector
   */
  public static By getSelectorFromPageObject(Field field) {
    return retrieveSelectorFromPageObject(field, false);
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

  public static By retrieveSelectorFromPageObjectInterface(Class<?> field) {
    String cssValue = field.getAnnotation(PageObject.class).css();
    if (StringUtils.isNotEmpty(cssValue)) {
      return By.cssSelector(cssValue);
    }
    String xpathValue = field.getAnnotation(PageObject.class).xpath();
    if (StringUtils.isNotEmpty(xpathValue)) {
      return By.xpath(xpathValue);
    }
    throw new IllegalArgumentException(
        "PageObject has to have defined selector when used with FindPageObject annotation");
  }

  private static By retrieveSelectorFromPageObject(Field field, boolean useGeneric) {
    String cssValue = useGeneric
        ? Objects.requireNonNull(PageObjectProviderHelper.getGenericType(field))
        .getAnnotation(PageObject.class).css()
        : field.getType().getAnnotation(PageObject.class).css();
    if (StringUtils.isNotEmpty(cssValue)) {
      return By.cssSelector(cssValue);
    }
    String xpathValue = useGeneric
        ? Objects.requireNonNull(PageObjectProviderHelper.getGenericType(field))
        .getAnnotation(PageObject.class).xpath()
        : field.getType().getAnnotation(PageObject.class).xpath();
    if (StringUtils.isNotEmpty(xpathValue)) {
      return By.xpath(xpathValue);
    }
    throw new IllegalArgumentException(
        "PageObject has to have defined selector when used with FindPageObject annotation");
  }

}
