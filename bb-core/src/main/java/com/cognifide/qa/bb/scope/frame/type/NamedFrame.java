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

import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.cognifide.qa.bb.scope.frame.FrameDescriptor;

/**
 * This FrameDescriptor represent a named frame.
 */
public class NamedFrame implements FrameDescriptor {

  private static final Logger LOG = LoggerFactory.getLogger(NamedFrame.class);

  private final String name;

  /**
   * Constructs NamedFrame. See also {@link WebDriver.TargetLocator#frame(java.lang.String)}
   *
   * @param frameLocator Name or id of the frame.
   */
  public NamedFrame(String frameLocator) {
    this.name = frameLocator;
  }

  @Override
  public void switchTo(WebDriver webDriver, BobcatWait bobcatWait) {
    LOG.debug("Switching to {}", name);
    bobcatWait.withTimeout(Timeouts.SMALL)
        .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(name));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(this.name, ((NamedFrame) obj).name);
  }

  @Override
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), name);
  }
}
