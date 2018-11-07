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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class XpathUtilsTest {

  private static Stream<Arguments> parameters() {
    return Stream.of(
        //@formatter:off
        //        input                        expected output
        arguments("",                          "''"),
        arguments("a",                         "'a'"),
        arguments("1",                         "'1'"),
        arguments("'",                         "\"'\""),
        arguments("'''",                       "\"'''\""),
        arguments("\"",                        "'\"'"),
        arguments("!@#$%^&*()",                "'!@#$%^&*()'"),
        arguments("'Please login' sentence",   "\"'Please login' sentence\""),
        arguments("My \" sentence",            "'My \" sentence'"),
        arguments("My \" sen'ten'ce ex\"tra",  "concat('My \" sen', \"'\", 'ten', \"'\", 'ce ex\"tra')")
        //@formatter:on
    );
  }

  @Test
  public void quoteShouldThrowNpeWhenNoArgumentsAreProvided() {
    assertThrows(NullPointerException.class, () -> XpathUtils.quote(null));
  }

  @ParameterizedTest
  @MethodSource("parameters")
  public void testQuoteForXPath(String sentence, String expected) {
    String actual = XpathUtils.quote(sentence);
    assertEquals(expected, actual);
  }
}
