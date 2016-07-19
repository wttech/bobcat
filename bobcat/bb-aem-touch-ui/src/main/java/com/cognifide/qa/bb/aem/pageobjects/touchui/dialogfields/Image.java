package com.cognifide.qa.bb.aem.pageobjects.touchui.dialogfields;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.qualifier.Global;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.scope.frame.FramePath;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldType;
import com.cognifide.qa.bb.aem.pageobjects.touchui.SidePanel;
import com.google.inject.Inject;

@PageObject
public class Image implements DialogField {

  @Inject
  private DragAndDropFactory dragAndDropFactory;

  @Global
  @FindBy(css = SidePanel.CSS)
  private SidePanel sidePanel;

  @FindBy(css = "span.coral-FileUpload")
  private WebElement dropArea;

  @Override
  public void setValue(Object value) {
    Draggable draggable = sidePanel.searchForAsset(String.valueOf(value));
    Droppable droppable = dragAndDropFactory.createDroppable(dropArea, FramePath.parsePath("/"));
    draggable.dropTo(droppable);
  }

  @Override
  public FieldType getType() {
    return FieldType.IMAGE;
  }
}
