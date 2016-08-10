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
package com.cognifide.qa.bb.proxy.record;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.cognifide.qa.bb.proxy.record.har.predicates.DomainPredicate;
import com.cognifide.qa.bb.proxy.record.har.predicates.PathPrefixPredicate;
import com.cognifide.qa.bb.proxy.record.har.predicates.QueryParametersPredicate;
import com.cognifide.qa.bb.proxy.record.har.predicates.UrlPrefixPredicate;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * Class provides convenient methods for building predicates that can be used by assertions run against
 * {@link TrafficLog}*
 */
public class TrafficLogPredicate {

  private final List<Predicate<HarEntry>> predicates = Lists.newArrayList();

  // TODO: should it be removed or should the visibility be changed?
  public TrafficLogPredicate() {
    // empty
  }

  /**
   * Creates a TrafficLogPredicate instance containing single HarEntry predicate
   *
   * @param entry the HarEntry
   */
  public TrafficLogPredicate(Predicate<HarEntry> entry) {
    this.predicates.add(entry);
  }

  /**
   * @return list of entries held by this object
   */
  public List<Predicate<HarEntry>> getPredicates() {
    return predicates;
  }

  /**
   * Adds a new HarEntry predicate to this object.
   *
   * @param entry the HarEntry
   * @return TrafficLogPredicate
   */
  public TrafficLogPredicate add(Predicate<HarEntry> entry) {
    predicates.add(entry);
    return this;
  }

  /**
   * Adds all predicates defined by query instance to this object
   *
   * @param query defined
   * @return TrafficLogPredicate
   */
  public TrafficLogPredicate add(TrafficLogPredicate query) {
    predicates.addAll(query.getPredicates());
    return this;
  }

  /**
   * @param urlPrefix required url prefix
   * @return a TrafficLogPredicate object that will match HarEntry with URL starting with urlPrefix
   */
  public static TrafficLogPredicate urlStartsWith(String urlPrefix) {
    return new TrafficLogPredicate(new UrlPrefixPredicate(urlPrefix));
  }

  /**
   * @param pathPrefix required path prefix
   * @return a TrafficLogPredicate object that will match HarEntry with URL path starting with pathPrefix.
   */
  public static TrafficLogPredicate pathStartsWith(String pathPrefix) {
    return new TrafficLogPredicate(new PathPrefixPredicate(pathPrefix));
  }

  /**
   * @param domainPrefix required domain prefix
   * @return a TrafficLogPredicate object that will match HarEntry with domainStartsWith
   */
  public static TrafficLogPredicate domainStartsWith(String domainPrefix) {
    return new TrafficLogPredicate(new DomainPredicate(domainPrefix));
  }

  /**
   * @param expectedParams required parameters
   * @return a TrafficLogPredicate object that will match HarEntry with all expected params passed within
   * query string
   */
  public static TrafficLogPredicate queryParamsMatch(Map<String, String> expectedParams) {
    return new TrafficLogPredicate(new QueryParametersPredicate(expectedParams));
  }

  /**
   * @param key   parameter key
   * @param value parameter value
   * @return a TrafficLogPredicate object that will match HarEntry with the param 'key' set to 'value'
   * contained in queryString
   */
  public static Predicate<HarEntry> queryParamWithValueExists(String key, String value) {
    return new QueryParametersPredicate(Collections.singletonMap(key, value));
  }

}
