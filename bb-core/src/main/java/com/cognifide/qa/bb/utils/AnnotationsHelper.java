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

package com.cognifide.qa.bb.utils;

import com.cognifide.qa.bb.mapper.field.PageObjectProviderHelper;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

/**
 * Helper class with methods for managing annotations.
 */
public final class AnnotationsHelper {

  private static final Class<?>[] FIND_ANNOTATIONS =
      new Class<?>[]{FindAll.class, FindBy.class, FindBys.class};

  private AnnotationsHelper() {
    // class with only static methods
  }

  /**
   * Checks if field contains {@link FindPageObject} annotation
   *
   * @param field field to check
   * @return if annotation is present
   */
  public static boolean isFindPageObjectAnnotationPresent(Field field) {
    return field.isAnnotationPresent(FindPageObject.class);
  }

  /**
   * Checks if field contains one of {@link FindAll} {@link FindBy} {@link FindBys} annotation
   *
   * @param field field to check
   * @return if one pf annotations is present
   */
  public static boolean isFindByAnnotationPresent(Field field) {
    for (Class<?> annotation : FIND_ANNOTATIONS) {
      if (field.isAnnotationPresent((Class<? extends Annotation>) annotation)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if field contains {@link Global} annotation
   *
   * @param field field to check
   * @return if annotation is present
   */
  public static boolean isGlobal(Field field) {
    return field.isAnnotationPresent(Global.class);
  }

  /**
   * Checks if Generic Type of field is annoted with PageObject Annotation
   *
   * @param field field to check
   * @return if annotation is present
   */
  public static boolean isGenericTypeAnnotedWithPageObjectOrInterface(Field field) {
    Class<?> genericType = PageObjectProviderHelper.getGenericType(field);
    return genericType != null && (genericType.isAnnotationPresent(PageObject.class) || genericType
        .isAnnotationPresent(
            PageObjectInterface.class));
  }
}
