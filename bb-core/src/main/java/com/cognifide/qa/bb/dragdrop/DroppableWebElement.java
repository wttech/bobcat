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

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Locatable;

import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * This class wraps WebElement into Droppable interface so that the WebElement can be the target of Bobcat's
 * drag and drop functionality.
 */
public class DroppableWebElement implements Droppable {

  private final WebElement dropArea;

  private final FramePath framePath;

  private final FrameSwitcher switcher;

  /**
   * Constructs DroppableWebElement. Initializes its fields.
   *
   * @param dropArea  represents WebElement
   * @param framePath FramePath to the WebElement
   * @param switcher  FrameSwitcher object
   */
  @Inject
  public DroppableWebElement(@Assisted WebElement dropArea, @Assisted FramePath framePath,
      FrameSwitcher switcher) {
    this.dropArea = dropArea;
    this.framePath = framePath;
    this.switcher = switcher;
  }

  /**
   * @return Point in the middle of the drop area.
   */
  @Override
  public Point getCurrentLocation() {
    Point inViewPort = null;
    switcher.switchTo(getFramePath());
    try {
      Dimension size = dropArea.getSize();
      inViewPort = ((Locatable) dropArea).getCoordinates().inViewPort()
          .moveBy(size.getWidth() / 2, size.getHeight() / 2);
    } finally {
      switcher.switchBack();
    }
    return inViewPort;
  }

  private FramePath getFramePath() {
    return framePath;
  }

}
