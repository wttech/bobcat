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
package com.cognifide.bdd.demo.traffic;

import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

abstract class TrafficAbstractTest {

  static final int TIMEOUT = 10;

  static final String PAGE_TO_OPEN =
      "/content/geometrixx/en/products/triangle.html";

  static final String AJAX_CALL_PATH =
      "/etc/clientcontext/default/content/jcr:content/stores.init.js";

  @Inject
  @Named(AemConfigKeys.AUTHOR_URL)
  String url;
}
