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

import java.util.List;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * Interface that defines object which can elect one HarEntry from collection regarding to predicate.
 */
public interface ClosestHarEntryElector {
  /**
   * Finds entry that matches predicate most
   *
   * @param harEntries collection of entries that would be searched
   * @return elected entry or null if nothing is found
   */
  HarEntry findSimilarEntry(List<HarEntry> harEntries);
}
