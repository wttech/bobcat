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

import com.cognifide.qa.bb.mapper.field.PageObjectProviderHelper;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.internal.LinkedBindingImpl;
import java.lang.reflect.Field;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.AbstractAnnotations;

public class BobcatAnnotations extends AbstractAnnotations {

  private Field field;

  private Injector injector;

  public BobcatAnnotations(Field field, Injector injector) {
    this.field = field;
    this.injector = injector;
  }

  @Override
  public By buildBy() {
    By selector = null;
    if (field.getType().equals(List.class)) {
      selector = PageObjectProviderHelper.getSelectorFromGenericPageObject(field, injector);
    } else {
      selector = getPageObjectSelector(field);
    }
    return selector;
  }

  public boolean isLookupCached() {
    return false;
  }

  private By getPageObjectSelector(Field field) {
    By selector = null;
    if (field.getType().isAnnotationPresent(
        PageObjectInterface.class)) {
      Binding<?> binding = injector.getBinding(field.getType());
      if (binding instanceof LinkedBindingImpl) {
        selector = PageObjectProviderHelper
            .retrieveSelectorFromPageObjectInterface(
                ((LinkedBindingImpl) binding).getLinkedKey().getTypeLiteral().getRawType());
      }
    } else {
      selector = PageObjectProviderHelper.getSelectorFromPageObject(field);
    }
    return selector;
  }
}
