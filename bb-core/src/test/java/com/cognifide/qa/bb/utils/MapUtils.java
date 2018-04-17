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

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapUtils {

  public static <K, U> Collector<Map.Entry<K, U>, ?, Map<K, U>> entriesToMap() {
    return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
  }

  public static <K, V> Map.Entry<K, V> entry(K key, V value) {
    return new AbstractMap.SimpleEntry<>(key, value);
  }

  public static <K, V> Map<K, V> mapOf(Map.Entry<K, V>... entries) {
    return Collections.unmodifiableMap(Stream.of(entries).collect(entriesToMap()));
  }
}
