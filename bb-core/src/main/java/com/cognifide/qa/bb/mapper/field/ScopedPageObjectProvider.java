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

import com.cognifide.qa.bb.exceptions.BobcatRuntimeException;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;
import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.scope.nested.ScopedElementLocatorFactory;
import com.cognifide.qa.bb.utils.AnnotationsHelper;
import com.google.inject.ConfigurationException;
import com.google.inject.Inject;
import com.google.inject.Injector;
import java.lang.reflect.Field;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

/**
 * This provider produces values for PageObject's fields that are annotated with one of the Find
 * annotations. It tracks the context in which the objects are created so that their own child
 * objects can reference their parent's creation context.
 */
public class ScopedPageObjectProvider implements FieldProvider {

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
    return (field.getType().isAnnotationPresent(PageObject.class) || field.getType()
        .isAnnotationPresent(
            PageObjectInterface.class))
        && AnnotationsHelper.isFindByAnnotationPresent(field);
  }

  /**
   * This method produces value for the field. It constructs the context for the creation out of
   * parent's context and the field's own frame info.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    final ElementLocatorFactory elementLocatorFactory = new ScopedElementLocatorFactory(webDriver,
        context.getElementLocatorFactory(), field);
    final FramePath framePath = frameMap.get(pageObject);
    contextStack.push(new PageObjectContext(elementLocatorFactory, framePath));
    Object scopedPageObject = null;
    try {
      scopedPageObject = injector.getInstance(field.getType());
    } catch (Exception e) {
      if (e instanceof ConfigurationException) {
        ConfigurationException ce = (ConfigurationException) e;
        throw new BobcatRuntimeException(
            "Configuration exception: " + ce.getErrorMessages().toString(), e);
      }
      throw new BobcatRuntimeException(e.getMessage(), e);
    } finally {
      contextStack.pop();
    }
    return Optional.ofNullable(scopedPageObject);
  }
}
