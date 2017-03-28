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
package com.cognifide.qa.bb.proxy.analyzer;

import java.util.HashMap;
import java.util.Map;

import net.lightbody.bmp.core.har.HarRequest;

class QueryStringParameters {

  private final HarRequest harRequest;

  QueryStringParameters(HarRequest harRequest) {
    this.harRequest = harRequest;
  }

  Map<String, String> retrieve() {
    HashMap<String, String> queryStringItems = new HashMap<>();
    harRequest.getQueryString().stream()
        .forEach(item -> queryStringItems.put(item.getName(), item.getValue()));
    return queryStringItems;
  }
}
