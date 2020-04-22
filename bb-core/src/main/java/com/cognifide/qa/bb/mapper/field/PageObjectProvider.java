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
package com.cognifide.qa.bb.mapper.field;

import static com.cognifide.qa.bb.utils.AnnotationsHelper.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.nested.ScopedElementLocatorFactory;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * This provider produces values for PageObject's fields. It tracks the context in which the objects
 * are created so that their own child objects can reference their parent's creation context.
 */
public class PageObjectProvider implements FieldProvider {

  @Inject
  private ContextStack contextStack;

  @Inject
  private Injector injector;

  @Inject
  private WebDriver webDriver;

  @Inject
  private FrameMap frameMap;

  /**
   * PageObjectInjectorListener calls this method to check if the provider is able to handle
   * currently injected field.
   * <p>
   * ScopedPageObjectProvider handles fields that:
   * <ul>
   * <li>come from classes that are annotated with PageObject annotation,
   * <li>have one of the Find annotations.
   * </ul>
   */
  @Override
  public boolean accepts(Field field) {
    return (isPageObjectAnnotationPresent(field) || isPageObjectInterfaceAnnotationPresent(field))
        && (isFindByAnnotationPresent(field) || isFindPageObjectAnnotationPresent(field))
        && isNotList(field);
  }

  /**
   * This method produces value for the field. It constructs the context for the creation out of
   * parent's context and the field's own frame info.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    final ElementLocatorFactory elementLocatorFactory = new ScopedElementLocatorFactory(webDriver,
        context.getElementLocatorFactory(), field, injector);
    final FramePath framePath = frameMap.get(pageObject);
    contextStack.push(new PageObjectContext(elementLocatorFactory, framePath));
    Object scopedPageObject;
    try {
      scopedPageObject = injector.getInstance(field.getType());
    } catch (ConfigurationException e) {
      throw new BobcatRuntimeException(
          "Configuration exception: " + e.getErrorMessages().toString(), e);
    } catch (Exception e) {
      throw new BobcatRuntimeException(e.getMessage(), e);
    } finally {
      contextStack.pop();
    }
    return Optional.ofNullable(scopedPageObject);
  }

  private boolean isNotList(Field field) {
    return !field.getType().equals(List.class);
  }
}
