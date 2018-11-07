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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.scope.frame.FrameDescriptor;
import com.cognifide.qa.bb.wait.BobcatWait;

/**
 * This is a FrameDescriptor that represents an n-th frame within current frame context.
 */
public class IndexedFrame implements FrameDescriptor {

  private static final Logger LOG = LoggerFactory.getLogger(IndexedFrame.class);

  private final int index;

  /**
   * Constructs IndexedFrame.
   *
   * @param index of the frame.
   */
  public IndexedFrame(int index) {
    this.index = index;
  }

  @Override
  public void switchTo(WebDriver webDriver, BobcatWait bobcatWait) {
    LOG.debug("Switching to {}", index);
    webDriver.switchTo().frame(index);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(index);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (this.getClass() != obj.getClass()) {
      return false;
    }
    return Objects.equals(this.index, ((IndexedFrame) obj).index);
  }

  @Override
  public String toString() {
    return String.format("%s[%d]", getClass().getSimpleName(), index);
  }
}
