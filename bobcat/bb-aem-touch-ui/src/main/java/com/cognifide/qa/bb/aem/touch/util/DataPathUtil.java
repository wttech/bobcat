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
package com.cognifide.qa.bb.aem.touch.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Data path util class.
 */
public final class DataPathUtil {

  private DataPathUtil() {
    // utility class
  }

  /**
   * Method normalizes data path. Checks if last character of path is digit, if yes, then returns substring
   * before last {@code _} occurrence.
   * Example: for given in parameter string '/title_1234' method will return '/title'
   *
   * @param dataPath data path.
   * @return normalized data path.
   */
  public static String normalize(String dataPath) {
    return Character.isDigit(dataPath.charAt(dataPath.length() - 1)) ?
        StringUtils.substringBeforeLast(dataPath, "_") :
        dataPath;
  }
}
