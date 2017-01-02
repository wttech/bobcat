package com.cognifide.qa.bb.modules;

import com.cognifide.qa.bb.dragdrop.DragAndDropFactory;
import com.cognifide.qa.bb.dragdrop.Draggable;
import com.cognifide.qa.bb.dragdrop.DraggableWebElement;
import com.cognifide.qa.bb.dragdrop.Droppable;
import com.cognifide.qa.bb.dragdrop.DroppableWebElement;
import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * This module contains bindings for {@link DragAndDropFactory}
 */
public class DragAndDropModule extends AbstractModule {
  @Override
  protected void configure() {
    install(new FactoryModuleBuilder().implement(Draggable.class, DraggableWebElement.class)
        .implement(Droppable.class, DroppableWebElement.class)
        .build(DragAndDropFactory.class));
  }
}
