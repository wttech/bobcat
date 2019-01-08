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

import com.cognifide.qa.bb.api.actions.ActionWithData;
import com.cognifide.qa.bb.api.actions.ActionsController;
import com.cognifide.qa.bb.api.actions.CoreActions;
import com.cognifide.qa.bb.api.actions.internal.DefaultController;
import com.cognifide.qa.bb.page.actions.CheckDisplayedTitleAction;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

public class ActionsModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ActionsController.class).to(DefaultController.class);

    MapBinder<String, ActionWithData> actionsWithData =
        MapBinder.newMapBinder(binder(), String.class, ActionWithData.class);
    actionsWithData.addBinding(CoreActions.CHECK_DISPLAYED_TITLE).to(CheckDisplayedTitleAction.class);
  }
}
