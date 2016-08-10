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
package com.cognifide.qa.bb.logging;

import com.google.inject.Inject;

/**
 * @deprecated
 *
 * This class offers several assertion methods:
 * <ul>
 * <li>simple hard assertion,
 * <li>soft assertion,
 * <li>always-fail assertion.
 * </ul>
 * These methods are connected with the rest of the reporting API so that if your assertion fail,
 * the failure will be logged in the report, without you having to do anything manually.
 * <br>
 * If you want to use the assertions, inject an instance of BobcatAssertions in your test class.
 */
@Deprecated
public class BobcatAssertions {

  @Inject
  private TestEventCollectorImpl testEventCollector;

  /**
   * Check if provided string matches regex
   *
   * @param errorMessage  error message when the text does NOT match the regular expression
   * @param stringToMatch tested string
   * @param regex         regular expression that should be matched
   */
  public void assertMatches(String errorMessage, String stringToMatch, String regex) {
    assertTrue(errorMessage, stringToMatch.matches(regex));
  }

  /**
   * Check if provided string matches regex
   *
   * @param stringToMatch tested string
   * @param regex         regular expression that should be matched
   */
  public void assertMatches(String stringToMatch, String regex) {
    assertMatches("", stringToMatch, regex);
  }

  /**
   * Simple hard assertion that checks the value of the provided condition.
   * If the condition is false, assertion marks whole test as failed.
   *
   * @param errorMessage error message for failed assertion
   * @param condition    tested condition
   */
  public void assertTrue(String errorMessage, boolean condition) {
    if (condition) {
      return;
    }
    fail(errorMessage);
  }

  /**
   * The simplest hard assertion. Marks whole test as failed and throws AssertionError
   * which stops test execution immediately.
   *
   * @param errorMessage Message to attach to the failure log entry.
   */
  public void fail(String errorMessage) {
    testEventCollector.fail(errorMessage);
    throw new AssertionError(errorMessage);
  }

  /**
   * If the condition is true, returns. Otherwise, marks the test as failed,
   * but doesn't stop its execution.
   *
   * @param errorMessage error message for failed assertion
   * @param condition    tested condition
   */
  public void softAssert(String errorMessage, boolean condition) {
    if (condition) {
      return;
    }
    testEventCollector.fail();
    testEventCollector.softAssertion(errorMessage);
  }
}
