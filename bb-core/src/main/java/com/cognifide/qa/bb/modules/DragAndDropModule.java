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
