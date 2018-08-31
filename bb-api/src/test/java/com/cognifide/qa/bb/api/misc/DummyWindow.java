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
package com.cognifide.qa.bb.api.misc;

import com.cognifide.qa.bb.api.traits.Closeable;
import com.cognifide.qa.bb.api.traits.Openable;

public class DummyWindow implements Closeable, Openable {
  @Override
  public void close() {
    System.out.println("Closing window");
  }

  @Override
  public boolean isClosed() {
    System.out.println("Verifying that window is closed");
    return true;
  }

  @Override
  public void open() {
    System.out.println("Opening window");
  }

  @Override
  public boolean isOpened() {
    System.out.println("Verifying that window is opened");
    return true;
  }
}
