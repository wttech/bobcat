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

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Optional;

import com.cognifide.qa.bb.qualifier.Cached;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.AnnotationsHelper;
import com.cognifide.qa.bb.utils.PageObjectInjector;
import com.google.inject.Inject;

/**
 * This class is a provider of Java proxies that will intercept access to PageObject fields that are
 * lists of PageObjects.
 */
public class PageObjectListProxyProvider implements FieldProvider {

  @Inject
  private PageObjectInjector injector;

  @Inject
  private FrameMap frameMap;

  /**
   * PageObjectInjectorListener calls this method to check if the provider is able to handle
   * currently injected field.
   * <p>
   * PageObjectListProxyProvider handles fields that are:
   * <ul>
   * <li>lists of PageObjects,
   * <li>annotated with one of FindBy annotations.
   * </ul>
   */
  @Override
  public boolean accepts(Field field) {
    return isList(field) && AnnotationsHelper.isFindByAnnotationPresent(field)
        && AnnotationsHelper.isGenericTypeAnnotedWithPageObjectOrInterface(field);
  }

  /**
   * Produces a proxy that will provide value for the list of PageObjects. Handler of this proxy is
   * an instance of PageObjectListInvocationHandler.
   */
  @Override
  public Optional<Object> provideValue(Object pageObject, Field field, PageObjectContext context) {
    FramePath framePath = frameMap.get(pageObject);
    PageObjectListInvocationHandler handler =
        new PageObjectListInvocationHandler(PageObjectProviderHelper.getGenericType(field),
            context.getElementLocatorFactory().createLocator(field), injector,
            shouldCacheResults(field),
            framePath);

    ClassLoader classLoader = PageObjectProviderHelper.getGenericType(field).getClassLoader();
    Object proxyInstance = Proxy.newProxyInstance(classLoader, new Class[] {List.class}, handler);
    return Optional.of(proxyInstance);
  }

  protected boolean isList(Field field) {
    return field.getType().equals(List.class);
  }

  protected boolean shouldCacheResults(Field field) {
    return field.isAnnotationPresent(Cached.class);
  }
}
