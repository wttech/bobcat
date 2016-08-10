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
package com.cognifide.qa.bb.proxy.analyzer.predicate;

import io.netty.handler.codec.http.HttpRequest;

/**
 * Interface representing a condition that accepts some requests and rejects others.
 */
public interface RequestPredicate {
  /**
   * Method checks if given resource fulfills conditions.
   *
   * @param request Tested resource
   * @return true if the request fulfills conditions, false otherwise
   */
  boolean accepts(HttpRequest request);

  /**
   * String representing given predicate, used in the reports.
   */
  @Override
  String toString();
}
