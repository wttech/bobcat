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
package com.cognifide.qa.bb.proxy.record.har.predicates;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;

import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarNameValuePair;

/**
 * Predicate that matches HarEntries with matching query parameters
 */
public class QueryParametersPredicate implements Predicate<HarEntry> {

  private final Map<String, String> expectedParams;

  /**
   * Constructor. Initializes QueryParametersPredicate
   *
   * @param expectedParams map of expected parameter
   */
  public QueryParametersPredicate(Map<String, String> expectedParams) {
    super();
    this.expectedParams = expectedParams;
  }

  @Override
  public boolean apply(HarEntry input) {
    List<HarNameValuePair> inputParams = input.getRequest().getQueryString();
    for (Entry<String, String> expectedParam : expectedParams.entrySet()) {
      if (!isPresent(expectedParam.getKey(), expectedParam.getValue(), inputParams)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    return String.format("QueryParametersPredicate(query params match %s)", this.expectedParams);
  }

  private boolean isPresent(String key, String value, List<HarNameValuePair> params) {
    for (HarNameValuePair param : params) {
      if (param.getName().equals(key) && param.getValue().equals(value)) {
        return true;
      }
    }
    return false;
  }

}
