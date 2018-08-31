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
package com.cognifide.qa.bb.api.actions.basic;

import com.cognifide.qa.bb.api.actions.Action;
import com.cognifide.qa.bb.api.actions.basic.close.CloseClass;
import com.cognifide.qa.bb.api.actions.basic.close.CloseObject;
import com.cognifide.qa.bb.api.traits.Closeable;

/**
 * Factory class that provides a more human-readable syntax for creating Close actions.
 */
public class Close {

  public static Action the(Closeable closeable) {
    return new CloseObject(closeable);
  }

  public static Action the(Class<? extends Closeable> closeable) {
    return new CloseClass(closeable);
  }
}
