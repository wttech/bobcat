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
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateBack;
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateRefresh;
import com.cognifide.qa.bb.api.actions.basic.navigate.NavigateToUrl;

/**
 * Factory class that provides a more human-readable syntax for creating Navigate actions.
 */
public class Navigate {

  public static Action to(String url) {
    return new NavigateToUrl(url);
  }

  public static Action back() {
    return new NavigateBack();
  }

  public static Action refresh() {
    return new NavigateRefresh();
  }
}
