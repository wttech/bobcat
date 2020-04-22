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

import static com.cognifide.qa.bb.mapper.field.PageObjectProviderHelper.getSelector;

import java.lang.reflect.Field;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

import com.google.inject.Injector;

/**
 * Implementation of {@link AbstractAnnotations} that handles the case of Page Object fields annotated with {@link com.cognifide.qa.bb.qualifier.FindPageObject}
 */
public class BobcatAnnotations extends AbstractAnnotations {

  private Field field;

  private Injector injector;

  public BobcatAnnotations(Field field, Injector injector) {
    this.field = field;
    this.injector = injector;
  }

  @Override
  public By buildBy() {
    return getSelector(field, injector).orElseThrow(
        () -> new IllegalArgumentException("No locator found in PageObject annotation"));
  }

  @Override
  public boolean isLookupCached() {
    return false;
  }

}
