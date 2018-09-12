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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields;

import com.cognifide.qa.bb.aem.core.component.SidePanel;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * This class represents Image dialog field with drag'n'drop functionality.
 */
@PageObject
public class Image implements DialogField {

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @Global
  @FindPageObject
  private SidePanel sidePanel;

  @FindBy(css = "span.coral-FileUpload")
  private WebElement dropArea;

  /**
   * Sets image assets in image component.
   *
   * @param value string data path to desired asset.
   */
  @Override
  public void setValue(Object value) {
    Draggable draggable = sidePanel.searchForAsset(String.valueOf(value));
    Droppable droppable = dragAndDropFactory.createDroppable(dropArea, FramePath.parsePath("/"));
    draggable.dropTo(droppable);
  }

  /**
   * @return dialog field type.
   */
  @Override
  public String getType() {
    return FieldType.IMAGE.name();
  }
}
