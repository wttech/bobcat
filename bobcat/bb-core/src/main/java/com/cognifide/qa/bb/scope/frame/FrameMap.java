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
package com.cognifide.qa.bb.scope.frame;

import java.util.Map;

import com.google.common.collect.MapMaker;
import com.google.inject.Singleton;

/**
 * This class stores the mapping between PageObjects and FramePaths.
 */
@Singleton
public class FrameMap {
  private final Map<Object, FramePath> frameMapping;

  /**
   * Constructs the FrameMap. Initializes its internal map with an empty concurrent map with weak keys. Weak
   * keys mean that keys will be compared by ==, not by equals().
   */
  public FrameMap() {
    this.frameMapping = new MapMaker().weakKeys().makeMap();
  }

  /**
   * Puts another entry in the frame dictionary.
   *
   * @param pageObject PageObject that has an associated frame.
   * @param framePath  Frame path associated with the PageObject.
   */
  public void put(Object pageObject, FramePath framePath) {
    frameMapping.put(pageObject, framePath);
  }

  /**
   * @param pageObject PageObjects for which to search for associated FramePath.
   * @return Frame info associated with the PageObject fetched from the frame dictionary. If the entry is
   * currently missing from the dictionary, get returns the default FramePath.
   */
  public FramePath get(Object pageObject) {
    if (frameMapping.containsKey(pageObject)) {
      return frameMapping.get(pageObject);
    } else {
      FramePath path = new FramePath();
      put(pageObject, path);
      return path;
    }
  }
}
