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
package com.cognifide.qa.bb.api.actions;

/**
 * Represents an action with additional data that can be executed by Bobcat's {@link ActionsController}.
 *
 * @param <T> type of {@link ActionData} that this action handles
 */
public interface ActionWithData<T extends ActionData> {
  /**
   * Executes the action with provided data.
   *
   * @param data the provided data object
   * @throws ActionException when action fails
   */
  void execute(T data) throws ActionException;
}
