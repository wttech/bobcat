/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.component.dialog.dialogfields.rte;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

class HyperlinkTest {

  @ParameterizedTest(name = "String from YAML is parsed correctly into a map: {0}")
  @ValueSource(strings = {
      "KEY1: VALUE1\nKEY2: VALUE2\nKEY3: VALUE3",
      "KEY1: VALUE1\nKEY2:VALUE2\nKEY3: VALUE3",
      "KEY1: VALUE1\nKEY2 : VALUE2\nKEY3: VALUE3",
      "KEY1: VALUE1\nKEY2 :VALUE2\nKEY3: VALUE3",
      "KEY1:VALUE1\nKEY2:VALUE2\nKEY3:VALUE3"})
  void stringFromYamlIsParsedCorrectlyIntoMap(String tested) {
    Map<String, String> expected = Maps.newHashMap();
    expected.put("KEY1", "VALUE1");
    expected.put("KEY2", "VALUE2");
    expected.put("KEY3", "VALUE3");

    assertThat(new Hyperlink().parseValue(tested)).containsAllEntriesOf(expected);
  }

  @ParameterizedTest
  @MethodSource("invalidMaps")
  void whenParsedMapContainsInvalidKeysThrowException(Map<String, String> tested) {

    assertThatThrownBy(() -> new Hyperlink().validateParsedMap(tested))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Parsed map contained keys that did not match the allowed prefixes.");
  }

  @ParameterizedTest
  @MethodSource("validMaps")
  void whenParsedMapContainsValidKeysDoNotThrowException(Map<String, String> tested) {
    assertThatCode(() -> new Hyperlink().validateParsedMap(tested))
        .doesNotThrowAnyException();
  }

  static Stream<Map<String, String>> invalidMaps() {
    return Stream.of(
        ImmutableMap.of(Hyperlink.ALT_PREFIX, "nop", "INVALID KEY", "nop"),
        ImmutableMap
            .of(Hyperlink.PATH_PREFIX, "values", Hyperlink.ALT_PREFIX, "dont",
                Hyperlink.TARGET_PREFIX, "matter", "INVALID KEY", ".")
    );
  }

  static Stream<Map<String, String>> validMaps() {
    return Stream.of(
        Collections.emptyMap(),
        ImmutableMap.of(Hyperlink.ALT_PREFIX, "nop"),
        ImmutableMap
            .of(Hyperlink.PATH_PREFIX, "values", Hyperlink.ALT_PREFIX, "dont",
                Hyperlink.TARGET_PREFIX, "matter")
    );
  }
}
