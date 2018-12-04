/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.sidepanel.internal;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.qualifier.PageObjectInterface;

/**
 * Represents the side panel in AEM authoring mode.
 */
@PageObjectInterface
public interface SidePanel {

  /**
   * Selects the provided tab.
   *
   * @param tab name of the tab to be selected
   */
  void selectTab(String tab);

  /**
   * Searches for assets for given asset name and return it as draggable.
   *
   * @param asset name
   * @return a {@link Draggable} instance of the asset
   */
  Draggable searchForAsset(String asset);

  /**
   * Selects the provided component for edition
   *
   * @param path          path of the component to be selected
   * @param component     component name
   * @param elementNumber which element should be selected in case there are more than 1 of given type
   * @return a {@link WebElement} representing the selected component
   */
  WebElement selectComponentToEdit(String path, String component, int elementNumber);

}
