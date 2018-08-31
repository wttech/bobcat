/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.api.actions;

import com.cognifide.qa.bb.aem.api.actions.edit.EditComponent;
import com.cognifide.qa.bb.api.actions.Action;

/**
 * Factory class that exposes a more human-readable API to provide Edit actions.
 */
public class Edit {

  public static Action component(Class component) {
    return new EditComponent(component);
  }
}
