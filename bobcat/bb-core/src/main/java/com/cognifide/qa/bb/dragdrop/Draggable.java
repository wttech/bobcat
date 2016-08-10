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

/**
 * Marks the object that can be dragged and dropped on the page.
 */
public interface Draggable {

  /**
   * Move current element by offset and release mouse button
   *
   * @param x offset value
   * @param y offset value
   */
  void dropByOffset(int x, int y);

  /**
   * Drop element to droppable
   *
   * @param droppable object representing droppable area
   */
  void dropTo(Droppable droppable);

}
