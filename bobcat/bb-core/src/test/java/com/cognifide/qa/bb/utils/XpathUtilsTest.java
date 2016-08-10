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
package com.cognifide.qa.bb.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class XpathUtilsTest {

  private final String sentence;

  private final String expected;

  private final Class<? extends Throwable> expectedExc;


  public XpathUtilsTest(String sentence, String expected, Class<? extends Throwable> expectedExc) {
    this.sentence = sentence;
    this.expected = expected;
    this.expectedExc = expectedExc;
  }

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
        //@formatter:off
        // input                      expected output                                           expected exception
        {null,                        null,                                                     NullPointerException.class},
        {"",                          "''",                                                     null},
        {"a",                         "'a'",                                                    null},
        {"1",                         "'1'",                                                    null},
        {"'",                         "\"'\"",                                                  null},
        {"'''",                       "\"'''\"",                                                null},
        {"\"",                        "'\"'",                                                   null},
        {"!@#$%^&*()",                "'!@#$%^&*()'",                                           null},
        {"'Please login' sentence",   "\"'Please login' sentence\"",                            null},
        {"My \" sentence",            "'My \" sentence'",                                       null},
        {"My \" sen'ten'ce ex\"tra",  "concat('My \" sen', \"'\", 'ten', \"'\", 'ce ex\"tra')", null}
        //@formatter:on
    });
  }

  @Test
  public void testQuoteForXPath() {
    try {
      String actual = XpathUtils.quote(sentence);
      assertEquals(expected, actual);
    } catch (Throwable e) {
      assertEquals(expectedExc, e.getClass());
    }
  }
}
