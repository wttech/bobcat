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
package com.cognifide.qa.bb.page.actions;

import com.cognifide.qa.bb.api.actions.ActionException;
import com.cognifide.qa.bb.api.actions.ActionWithData;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Action that checks if page title is displayed
 */
@PageObject
public class CheckDisplayedTitleAction implements ActionWithData<DisplayedTitleData> {

  @Inject
  private BobcatWait bobcatWait;

  private BooleanResponse booleanResponse;

  @Override
  public void execute(DisplayedTitleData data) throws ActionException {
    booleanResponse = new BooleanResponse(
        bobcatWait.isConditionMet(ExpectedConditions.titleIs(data.getTitle())));
  }

  @Override
  public BooleanResponse getResponse() {
    return booleanResponse;
  }
}
