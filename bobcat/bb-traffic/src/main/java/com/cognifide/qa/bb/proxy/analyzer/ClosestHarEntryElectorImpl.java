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

import java.util.List;
import java.util.stream.Collectors;

import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicateImpl;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * This is an utility class for find closest hit - the most similar entry.
 */
class ClosestHarEntryElectorImpl implements ClosestHarEntryElector {

  private final RequestPredicateImpl predicate;

  ClosestHarEntryElectorImpl(RequestPredicateImpl predicate) {
    this.predicate = predicate;
  }

  /**
   * This method returns the most similar entry described by predicate.
   * It is using and algorithm, that filters all requests that doesn't have match with url prefix
   * and finds the best parameters match by checking how many parameters have the same value that
   * predicate defines. The most similar request is elected to being the best match. However, if
   * all requests will be filtered, then null is returned.
   *
   * @param harEntries List of entries to be searched
   * @return bestEntry The most similar entry or null if no request's url match url prefix
   */
  @Override
  public HarEntry findSimilarEntry(List<HarEntry> harEntries) {
    HarEntry bestEntry = null;
    int bestScore = 0;

    List<HarEntry> matchingUrl = findEntriesWithUrl(predicate, harEntries);

    for (HarEntry harEntry : matchingUrl) {
      QueryStringParameters parametrizable =
          new QueryStringParameters(harEntry.getRequest());
      int harEntryScore = predicate.calculateScore(parametrizable.retrieve());
      if (harEntryScore > bestScore) {
        bestScore = harEntryScore;
        bestEntry = harEntry;
      }
    }
    return bestEntry;
  }

  private List<HarEntry> findEntriesWithUrl(RequestPredicateImpl predicate,
      List<HarEntry> harEntries) {
    return harEntries.stream()
        .filter(entry -> predicate.startsWithUrlPrefix(entry.getRequest().getUrl()))
        .collect(Collectors.toList());
  }
}
