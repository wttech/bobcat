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
package com.cognifide.qa.bb.mapper.annotations;

import java.lang.reflect.Field;

import org.openqa.selenium.support.pagefactory.AbstractAnnotations;
import org.openqa.selenium.support.pagefactory.Annotations;

import com.cognifide.qa.bb.utils.AnnotationsHelper;
import com.google.inject.Injector;

/**
 * Responsible for providing proper {@link AbstractAnnotations} implementation based on the encountered field annotation.
 */
public final class FieldAnnotationsProvider {

  private FieldAnnotationsProvider() {
    //util
  }

  /**
   * Provides an {@link AbstractAnnotations} implementation based on provided field's annotations.
   *
   * @param field    which annotations are checked
   * @param injector to provide Bobcat-augmented annotations
   * @return <ul>
   * <li>{@link Annotations} for fields decorated with {@link org.openqa.selenium.support.FindBy}, {@link org.openqa.selenium.support.FindAll} or {@link org.openqa.selenium.support.FindBys}</li>
   * <li>{@link BobcatAnnotations} for fields decorated with {@link com.cognifide.qa.bb.qualifier.FindPageObject}</li>
   * </ul>
   * @throws IllegalArgumentException when the field is not decorated with any of the above annotations
   */
  public static AbstractAnnotations create(Field field, Injector injector) {
    if (AnnotationsHelper.isFindByAnnotationPresent(field)) {
      return new Annotations(field);
    }
    if (AnnotationsHelper.isFindPageObjectAnnotationPresent(field)) {
      return new BobcatAnnotations(field, injector);
    }
    throw new IllegalArgumentException(
        "Field is not marked by any supported annotation: " + field.toGenericString());
  }

}
