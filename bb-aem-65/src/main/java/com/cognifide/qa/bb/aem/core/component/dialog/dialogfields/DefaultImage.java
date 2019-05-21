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

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.aem.core.sidepanel.internal.SidePanel;
import com.cognifide.qa.bb.aem.core.sidepanel.internal.SidePanelTabs;
import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.qualifier.FindPageObject;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.google.inject.Inject;

/**
 * Default implementation of {@link Image}
 */
@PageObject(xpath = "//*[contains(@class,'cq-FileUpload')]/..")
public class DefaultImage implements Image {

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @Global
  @FindPageObject
  private SidePanel sidePanel;

  @FindBy(css = ".cq-FileUpload-thumbnail")
  private WebElement dropArea;

  @FindBy(css = Locators.LABEL_CSS)
  private List<WebElement> label;

  @Override
  public void setValue(Object value) {
    sidePanel.selectTab(SidePanelTabs.ASSETS.getCssClass());
    Draggable draggable = sidePanel.searchForAsset(String.valueOf(value));
    Droppable droppable = dragAndDropFactory.createDroppable(dropArea, FramePath.parsePath("/"));
    draggable.dropTo(droppable);
  }

  @Override
  public String getLabel() {
    return label.isEmpty() ? "" : label.get(0).getText();
  }
}
