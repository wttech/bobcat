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
package com.cognifide.qa.bb.scope.frame.type;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.scope.frame.FrameDescriptor;

/**
 * This is a FrameDescriptor that represents the top frame on the page.
 */
public enum DefaultFrame implements FrameDescriptor {

  INSTANCE;

  private static final Logger LOG = LoggerFactory.getLogger(DefaultFrame.class);

  @Override
  public void switchTo(WebDriver webDriver, BobcatWait bobcatWait) {
    LOG.debug("Switching to default content");
    webDriver.switchTo().defaultContent();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[/]";
  }
}
