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

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collections;

import org.hamcrest.Matcher;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.common.base.Predicate;
import com.google.inject.Inject;

import net.lightbody.bmp.core.har.HarEntry;

/**
 * This class provides hamcrest-style assertions that are checked against network traffic log captured by the
 * {@link TrafficLogProvider}
 */
@ThreadScoped
public class TrafficLogAnalyzer {

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

}
