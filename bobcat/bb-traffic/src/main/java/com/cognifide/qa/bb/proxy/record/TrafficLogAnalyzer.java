package com.cognifide.qa.bb.proxy.record;

/*-
 * #%L
 * Bobcat Parent
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


import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;

/**
 * This class provides hamcrest-style assertions that are checked against network traffic log captured by the
 * {@link TrafficLogProvider}
 */
@ThreadScoped
public class TrafficLogAnalyzer {

  private static final Logger LOG = LoggerFactory.getLogger(TrafficLogAnalyzer.class);

  @Inject
  private TrafficLogProvider trafficLogProvider;

  /**
   * Verifies that traffic log contains at least one entry matching the TrafficLogPredicate
   *
   * @param query {@link TrafficLogPredicate}
   */
  public void assertContains(TrafficLogPredicate query) {
    assertThat(trafficLogProvider.get(), new TrafficLogContains(query.getPredicates()));
  }

  /**
   * Verifies that traffic log contains at least one entry matching the Predicate
   *
   * @param predicate predicate to match
   */
  public void assertContains(Predicate<HarEntry> predicate) {
    assertThat(trafficLogProvider.get(),
        new TrafficLogContains(Collections.singletonList(predicate)));
  }

  /**
   * Asserts that traffic log is matched by provided matcher
   *
   * @param matcher provided matcher
   */
  public void assertMatches(Matcher<TrafficLog> matcher) {
    assertThat(trafficLogProvider.get(), matcher);
  }

  private static class TrafficLogContains extends TypeSafeMatcher<TrafficLog> {

    List<Predicate<HarEntry>> predicates;

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
      for (Har har : item.getHars()) {
        for (HarEntry entry : har.getLog().getEntries()) {
          if (harEntryMatchesPredicates(entry)) {
            return true;
          }
        }
      }
      return false;
    }

    private boolean harEntryMatchesPredicates(HarEntry entry) {
      for (Predicate<HarEntry> predicate : predicates) {
        if (!predicate.apply(entry)) {
          LOG.debug("HarEntry " + entry.getRequest().getUrl() + " discarded by " + predicate);
          return false;
        }
      }
      return true;
    }

  }
}
