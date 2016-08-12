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
package com.cognifide.bdd.demo.misc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class RegexMatcherTest {

  private static final String REGEX =
      "[A-Z][a-z]{2}\\s[A-Z][a-z]{2}\\s\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}\\s[A-Z]{2,4}\\s[0-9]{4}";

  @Test
  public void regexMatcherTest() {
    String time = Calendar.getInstance().getTime().toString();
    assertThat(time).matches(REGEX);
  }
}
