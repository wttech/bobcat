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
package com.cognifide.qa.bb.aem.ui;

import java.lang.reflect.Field;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.cognifide.qa.bb.aem.qualifier.DialogField;
import com.cognifide.qa.bb.mapper.field.FieldProvider;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * This is a provider that produces values for PageObject fields that represent dialog fields. Such fields are
 * annotated with DialogField annotation. AemClassicModule binds DialogField annotation to this provider. This
 * class should not be used directly by Bobcat's users.
 */
public class DialogFieldProvider implements FieldProvider {

  @Inject
  private Provider<AemDialogFieldResolver> fieldResolverProvider;

  /**
   * PageObjectInjectorListener calls this method to check if the provider is able to handle currently
   * injected field.
   * <p>
   * DialogFieldProvider handles fields decorated with DialogField annotation.
   */
  @Override
  public boolean accepts(Field field) {
    return field.isAnnotationPresent(DialogField.class);
  }

  /**
   * Looks for DialogField annotation, reads locator data from the annotation instance
   * and asks AemDialogFieldResolver to produce a dialog field instance for the class field.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    AemDialogFieldResolver aemDialogFieldResolver = fieldResolverProvider.get();
    DialogField dialogFieldAnnotation = field.getAnnotation(DialogField.class);

    String searchBy;
    Class<?> type = field.getType();
    Object dialogField = null;
    if (StringUtils.isNotEmpty(dialogFieldAnnotation.label())) {
      searchBy = dialogFieldAnnotation.label();
      dialogField = aemDialogFieldResolver.getField(searchBy, type);
    } else if (StringUtils.isNotEmpty(dialogFieldAnnotation.css())) {
      searchBy = dialogFieldAnnotation.css();
      dialogField = aemDialogFieldResolver.getFieldByCss(searchBy, type);
    } else if (StringUtils.isNotEmpty(dialogFieldAnnotation.name())) {
      searchBy = dialogFieldAnnotation.name();
      dialogField = aemDialogFieldResolver.getFieldByName(searchBy, type);
    } else if (StringUtils.isNotEmpty(dialogFieldAnnotation.xpath())) {
      searchBy = dialogFieldAnnotation.xpath();
      dialogField = aemDialogFieldResolver.getFieldByXpath(searchBy, type);
    }
    return Optional.ofNullable(dialogField);
  }
}
