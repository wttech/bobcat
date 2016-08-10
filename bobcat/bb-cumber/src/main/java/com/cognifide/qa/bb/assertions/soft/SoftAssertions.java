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
package com.cognifide.qa.bb.assertions.soft;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

/**
 * Provides soft assertions that can be used within methods intercepted by
 * {@link SoftAssertionsAspect}
 */
public final class SoftAssertions {

  private SoftAssertions() {
  }

  /**
   * Asserts that actual value is accepted by the matcher. If it isn't {@link SoftAssertionError}
   * will be registered using {@link SoftAssertionsAspect}
   *
   * @param actual value to be checked
   * @param <T> actual value type
   * @param matcher will be used to test the actual value
   */
  public static <T> void assertSoftlyThat(T actual, Matcher<? super T> matcher) {
    assertSoftlyThat("", actual, matcher);
  }

  /**
   * Asserts that actual value is accepted by the matcher. If it isn't {@link SoftAssertionError}
   * will be registered using {@link SoftAssertionsAspect}
   *
   * @param reason the identifying message for the {@link SoftAssertionError}
   * @param actual value to be checked *
   * @param <T> actual value type
   * @param matcher will be used to test the actual value
   */
  public static <T> void assertSoftlyThat(String reason, T actual, Matcher<? super T> matcher) {
    if (!matcher.matches(actual)) {
      Description description = new StringDescription();
      description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher)
          .appendText("\n     but: ");
      matcher.describeMismatch(actual, description);

      SoftAssertionsAspect.addFailure(new SoftAssertionError(description.toString()));

    }
  }

  /**
   * Asserts that a condition is true. If it isn't {@link SoftAssertionError} will be registered
   * using {@link SoftAssertionsAspect}
   *
   * @param reason the identifying message for the {@link SoftAssertionError} (<code>null</code>
   *        okay)
   * @param assertion condition to be checked
   */
  public static void assertSoftlyThat(String reason, boolean assertion) {
    if (!assertion) {
      SoftAssertionsAspect.addFailure(new SoftAssertionError(reason));
    }
  }

}
