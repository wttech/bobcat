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
package com.cognifide.qa.bb.dragdrop;

import org.openqa.selenium.WebElement;

import com.cognifide.qa.bb.scope.frame.FramePath;

/**
 * This factory lets the user produce draggable and droppable webelements. See the binding in AemClassicModule.
 */
public interface DragAndDropFactory {

  /**
   * Create draggable element.
   *
   * @param findElement webElement
   * @param framePath   path to frame of webElement
   * @return Draggable
   */
  Draggable createDraggable(WebElement findElement, FramePath framePath);

  /**
   * Create droppable element.
   *
   * @param findElement webElement
   * @param framePath   path to frame of webElement
   * @return Draggable
   */
  Droppable createDroppable(WebElement findElement, FramePath framePath);

}
