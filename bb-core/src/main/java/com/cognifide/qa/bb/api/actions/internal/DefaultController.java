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
package com.cognifide.qa.bb.api.actions.internal;

import java.util.Map;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.ActionData;
import com.cognifide.qa.bb.api.actions.ActionException;
import com.cognifide.qa.bb.api.actions.ActionWithData;
import com.cognifide.qa.bb.api.actions.ActionsController;
import com.google.inject.Inject;

/**
 * Bobcat's default {@link ActionsController}.
 * <p>
 * It's bound in the Core module, so users can simply inject it:
 * {@code
 *
 * @Inject private ActionsController controller;}
 */
public class DefaultController implements ActionsController {

  @Inject(optional = true)
  private Map<String, Action> actions;

  @Inject(optional = true)
  private Map<String, ActionWithData> actionWithData;

  @Override
  public void execute(String action) throws ActionException {
    actions.get(action).execute();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void execute(String action, ActionData data) throws ActionException {
    actionWithData.get(action).execute(data);
  }
}
