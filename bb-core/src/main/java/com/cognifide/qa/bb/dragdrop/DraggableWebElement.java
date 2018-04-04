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
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Locatable;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.frame.FrameSwitcher;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * This class wraps WebElement into Draggable interface.
 */
public class DraggableWebElement implements Draggable {

  private enum Orientantion {
    HORIZONTAL, VERTICAL
  }

  private static final int MOVEMENT_STEP = 10;

  private final WebElement webElement;

  private final FramePath framePath;

  private final FrameSwitcher switcher;

  @Inject
  private Actions actions;

  /**
   * Constructs DraggableWebElement. Initializes its fields.
   *
   * @param dragElement represents WebElement
   * @param framePath FramePath to the WebElement
   * @param switcher FrameSwitcher object
   */
  @Inject
  public DraggableWebElement(@Assisted WebElement dragElement, @Assisted FramePath framePath,
      FrameSwitcher switcher) {
    this.webElement = dragElement;
    this.framePath = framePath;
    this.switcher = switcher;
  }

  /**
   * Performs the drag and drop:
   * <ul>
   * <li>switches to the frame that contains source element,
   * <li>grabs the source element by clicking on it,
   * <li>slowly moves towards the drop area,
   * <li>release the mouse button when reaches the center of the drop area.
   * </ul>
   */
  @Override
  public void dropByOffset(int x, int y) {
    switcher.switchTo(getFramePath());
    try {
      actions.clickAndHold(webElement).perform();
      performMovement(x, Orientantion.HORIZONTAL, actions);
      performMovement(y, Orientantion.VERTICAL, actions);
      waitForElementsToBeReady();
      actions.release().perform();
    } finally {
      switcher.switchBack();
    }
  }

  private void waitForElementsToBeReady() {
    // waiting for JS
    BobcatWait.sleep(Timeouts.MINIMAL);
  }

  /**
   * Performs the drag and drop with additional click.
   *
   * @param droppable - droppable element
   */
  @Override
  public void dropTo(Droppable droppable) {
    Offset offset = new Offset(getCurrentLocation(), droppable.getCurrentLocation());
    dropByOffset(offset.getX(), offset.getY());
  }

  private void performMovement(int movement, Orientantion orientantion, Actions builder) {
    int movementRemains = Math.abs(movement);
    int movementDirection = Integer.signum(movement);
    while (movementRemains > 0) {
      int movementStep = movementRemains >= MOVEMENT_STEP ? MOVEMENT_STEP : 1;
      if (orientantion == Orientantion.VERTICAL) {
        builder.moveByOffset(0, movementDirection * movementStep);
      } else {
        builder.moveByOffset(movementDirection * movementStep, 0);
      }
      movementRemains -= movementStep;
    }

  }

  private Point getCurrentLocation() {
    Point inViewPort = null;
    switcher.switchTo(getFramePath());
    try {
      Dimension size = webElement.getSize();
      inViewPort = ((Locatable) webElement).getCoordinates().inViewPort()
          .moveBy(size.getWidth() / 2, size.getHeight() / 2);
    } finally {
      switcher.switchBack();
    }
    return inViewPort;
  }

  private FramePath getFramePath() {
    return framePath;
  }

  private static class Offset {

    private final int x;

    private final int y;

    /**
     * Distance and direction from point drag to point drop in two-dimensional plane
     *
     * @param drag start point of drag
     * @param drop end point
     */
    Offset(Point drag, Point drop) {
      this.x = drop.getX() - drag.getX();
      this.y = drop.getY() - drag.getY();
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
    }
  }

}
