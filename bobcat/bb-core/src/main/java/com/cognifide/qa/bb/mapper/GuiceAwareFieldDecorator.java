package com.cognifide.qa.bb.mapper;

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


import java.lang.reflect.Field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import com.cognifide.qa.bb.webelement.BobcatWebElement;
import com.google.inject.Inject;

/**
 * This class extends Selenium's default field decorator so that Selenium will initalize all fields except the
 * ones annotated with Inject. This class should not be used by Bobcat's users.
 */
public class GuiceAwareFieldDecorator extends DefaultFieldDecorator {

  /**
   * Constructor. Initializes decorator with the element locator factory that will be used for producing
   * values for decorated fields.
   *
   * @param factory represents ElementLocatorFactory
   */
  public GuiceAwareFieldDecorator(ElementLocatorFactory factory) {
    super(factory);
  }

  /**
   * This method decorates the field with the generated value. It should not be used directly. Selenium's
   * PageFactory calls this method. If the field has Inject annotation, Selenium will ignore it. Otherwise
   * Selenium will try to generate the value for this field.
   *
   * @param loader ClassLoader
   * @param field  Field to be initialized
   * @return decorated field Object
   */
  @Override
  public Object decorate(ClassLoader loader, Field field) {
    if (field.isAnnotationPresent(Inject.class)) {
      return null;
    } else {
      Object decoratedField = super.decorate(loader, field);
      if (decoratedField instanceof WebElement) {
        WebElement element = (WebElement) decoratedField;
        Locatable locatable = (Locatable) decoratedField;
        return new BobcatWebElement(element, locatable);
      }
      return decoratedField;
    }
  }
}
