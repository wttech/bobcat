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
package com.cognifide.qa.bb.mapper;

import com.cognifide.qa.bb.qualifier.Frame;
import com.cognifide.qa.bb.scope.ContextStack;
import com.cognifide.qa.bb.scope.PageObjectContext;
import com.cognifide.qa.bb.scope.frame.FrameMap;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.utils.AopUtil;
import com.cognifide.qa.bb.webelement.BobcatWebElementFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages injection of page objects. It is registered as an injection handler in
 * CoreModule. Users should not use this class directly.
 */
public class PageObjectInjectorListener implements InjectionListener<Object> {

  private static final Logger LOG = LoggerFactory.getLogger(PageObjectInjectorListener.class);

  private final Provider<BobcatWebElementFactory> bobcatWebElementFactoryProvider;

  private final Provider<WebDriver> webDriverProvider;

  private final Provider<ContextStack> locatorStackProvider;

  private final Provider<FieldProviderRegistry> registry;

  private final Provider<FrameMap> frameMap;

  /**
   * Constructor of the listener. Initializes all the fields.
   *
   * @param typeEncounter Type encounter instance, provided by Guice.
   */
  public PageObjectInjectorListener(TypeEncounter<?> typeEncounter) {
    this.webDriverProvider = typeEncounter.getProvider(WebDriver.class);
    this.locatorStackProvider = typeEncounter.getProvider(ContextStack.class);
    this.registry = typeEncounter.getProvider(FieldProviderRegistry.class);
    this.frameMap = typeEncounter.getProvider(FrameMap.class);
    this.bobcatWebElementFactoryProvider = typeEncounter.getProvider(BobcatWebElementFactory.class);
  }

  /**
   * Guice will call this method automatically when it is done injecting the object. Don't call this
   * method manually.
   */
  @Override
  public void afterInjection(Object injectee) {
    setFramePath(injectee);
    initFindByFields(injectee);
    initPageObjectFields(injectee);
    invokePostConstruct(injectee);
  }

  private void setFramePath(Object injectee) {
    final FramePath framePath = getFramePath(injectee);
    frameMap.get().put(injectee, framePath);
  }

  private FramePath getFramePath(Object injectee) {
    Frame frame = injectee.getClass().getAnnotation(Frame.class);
    if (frame == null) {
      return getCurrentContext().getFramePath();
    } else {
      return getCurrentContext().getFramePath().addFrame(frame);
    }
  }

  private void initFindByFields(Object object) {
    PageFactory.initElements(
        new GuiceAwareFieldDecorator(getCurrentContext().getElementLocatorFactory(),
            bobcatWebElementFactoryProvider.get()), object);
  }

  private void initPageObjectFields(Object object) {
    PageObjectContext context = getCurrentContext();
    Arrays.stream(AopUtil.getBaseClassForAopObject(object).getDeclaredFields())
        .filter(field -> !field.isAnnotationPresent(Inject.class)).forEach(
        field -> provideValue(object, context, field)
    );

  }

  private void provideValue(Object object, PageObjectContext context, Field field) {
    registry.get().getProviders().stream().filter(provider -> provider.accepts(field)).findFirst()
        .ifPresent(
            provider -> provider.provideValue(object, field, context)
                .ifPresent(value -> setFieldValue(field, object, value))
        );
  }

  private void setFieldValue(Field f, Object object, Object value) {
    try {
      f.setAccessible(true);
      f.set(object, value);
    } catch (IllegalArgumentException | IllegalAccessException e) {
      LOG.error("Can't set field", e);
    }
  }

  private void invokePostConstruct(Object object) {
    Arrays.stream(object.getClass().getMethods())
        .filter(method -> method.isAnnotationPresent(PostConstruct.class))
        .forEach(method -> safeMethodInvoke(method, object));
  }

  private void safeMethodInvoke(Method method, Object object) {
    try {
      method.invoke(object);
    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      LOG.error("Can't invoke PostConstruct method: {}", method.getName(), e);
    }
  }

  private PageObjectContext getCurrentContext() {
    return locatorStackProvider.get().getCurrentContext(webDriverProvider.get());
  }
}
