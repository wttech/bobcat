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
package com.cognifide.qa.bb.scope.frame;

import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.provider.selenium.BobcatWait;

/**
 * Any class that represents a frame must implement this interface.
 */
public interface FrameDescriptor {
  /**
   * Instructs webDriver to switch to the frame represented by this object.
   *
   * @param webDriver  WebDriver instance.
   * @param bobcatWait BobcatWait instance.
   */
  void switchTo(WebDriver webDriver, BobcatWait bobcatWait);
}
