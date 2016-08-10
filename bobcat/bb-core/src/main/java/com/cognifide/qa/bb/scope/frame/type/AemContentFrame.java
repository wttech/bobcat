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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.scope.frame.FrameDescriptor;

/**
 * This is a FrameDescriptor that represents the AEM's content frame ($cq).
 */
public enum AemContentFrame implements FrameDescriptor {

  INSTANCE();

  private static final String CF_URL_PART = "/cf#";
  private static final String AEM_CF_FRAME = "cq-cf-frame";
  private static final Logger LOG = LoggerFactory.getLogger(AemContentFrame.class);

  /**
   * Switches to cq-cf-frame, if the address of the page contains "/cf#".
   */
  @Override
  public void switchTo(WebDriver webDriver, BobcatWait bobcatWait) {
    if (webDriver.getCurrentUrl().contains(CF_URL_PART)) {
      LOG.debug("switching to AEM content frame..");
      bobcatWait.withTimeout(Timeouts.MEDIUM).until(
          ExpectedConditions.frameToBeAvailableAndSwitchToIt(AEM_CF_FRAME));
      LOG.debug("..switched");
    } else {
      LOG.info("current URL does not contain '{}' fragment", CF_URL_PART);
    }
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "[cq]";
  }
}
