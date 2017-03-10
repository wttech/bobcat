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
package com.cognifide.qa.bb.aem.ui.component;

/**
 * This is a helper class that retrieves component info from classes that are annotated with AemComponent
 * annotation.
 */
public class AemComponentHandler {

  private String cssClassName;

  private String name;

  private String group;

  private String sidekickCssSelector;

  /**
   * Constructs AemComponentHandler for a given component class. Throws ComponentAnnotationMissingException
   * if the class is not annotated with AemComponent annotation. Otherwise reads css class name, component
   * name and component group from the annotation and initializes AemComponentHandler with these values.
   *
   * @param componentClass the class for AEM component
   */
  public AemComponentHandler(Class<?> componentClass) {
    if (!componentClass.isAnnotationPresent(AemComponent.class)) {
      throw new ComponentAnnotationMissingException("Class: " + componentClass.getName()
          + "is not a AemComponent");
    } else {
      AemComponent componentAnnotation = componentClass.getAnnotation(AemComponent.class);
      this.cssClassName = componentAnnotation.cssClassName();
      this.name = componentAnnotation.name();
      this.group = componentAnnotation.group();
      this.sidekickCssSelector = componentAnnotation.sidekickCssSelector();
    }
  }

  /**
   * @return Css class of the component.
   */
  public String getCssClassName() {
    return cssClassName;
  }

  /**
   * @return Name of the component.
   */
  public String getComponentName() {
    return name;
  }

  /**
   * @return Name of the component group.
   */
  public String getComponentGroup() {
    return group;
  }

  /**
   * @return cssSelector used inside the sidekick
   */
  public String getSidekickCssSelector() {
    return sidekickCssSelector;
  }

}
