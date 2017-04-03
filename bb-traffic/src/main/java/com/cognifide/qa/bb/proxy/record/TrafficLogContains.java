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

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * This class checks if TrafficLogEntry matches specified predicates.
 */
class TrafficLogContains extends TypeSafeMatcher<TrafficLog> {

  private static final Logger LOG = LoggerFactory.getLogger(TrafficLogContains.class);

  private final List<Predicate<HarEntry>> predicates;

  public TrafficLogContains(List<Predicate<HarEntry>> predicates) {
    super();
    this.predicates = predicates;
  }

  @Override
  public void describeTo(Description description) {
    description.appendText("contains request matching " + predicates);
  }

  @Override
  protected boolean matchesSafely(TrafficLog item) {
    return item.getHars().stream().
            anyMatch(har -> har.getLog().getEntries().stream().
                    anyMatch(this::harEntryMatchesPredicates));
  }

  private boolean harEntryMatchesPredicates(HarEntry entry) {
    for (Predicate<HarEntry> predicate : predicates) {
      if (!predicate.apply(entry)) {
        LOG.debug("HarEntry {} discarded by {}", entry.getRequest().getUrl(), predicate);
        return false;
      }
    }
    return true;
  }

}
