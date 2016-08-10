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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A handy set of tools to work with XPATH locators
 */
public final class XpathUtils {

  private XpathUtils() {}

  /**
   * Method perform intelligent escaping of quotes in xpath expressions, if necessary it constructs concat
   * functions otherwise it simply add single or double quotes.
   *
   * @param sentence - e.g. "'My Login' te"xt" then result: concat("'", 'My Login', "'", ' te"xt')
   * @return sentence with quotes or concat function
   * @throws NullPointerException if sentence is null
   */
  public static String quote(String sentence) {
    if (!sentence.contains("'")) {
      return addSingleQuotation(sentence);
    } else if (sentence.contains("'") && !sentence.contains("\"")) {
      return addDoubleQuotation(sentence);
    } else {
      return changeToConcat(sentence);
    }
  }

  private static String addDoubleQuotation(String sentence) {
    return "\"" + sentence + "\"";
  }

  private static String addSingleQuotation(String sentence) {
    return "'" + sentence + "'";
  }

  private static String changeToConcat(String sentenceTosearch) {
    StringBuilder builder = new StringBuilder();
    builder.append("concat(");

    List<String> tokens = splitTokens(sentenceTosearch);
    quoteTokens(tokens);

    for (int i = 0; i < tokens.size(); i++) {
      builder.append(tokens.get(i));
      if (i < tokens.size() - 1) {
        builder.append(", ");
      }
    }

    builder.append(")");

    return builder.toString();
  }

  private static void quoteTokens(List<String> tokens) {
    for (int i = 0; i < tokens.size(); i++) {
      String token = tokens.get(i);
      if ("'".equals(token)) {
        token = addDoubleQuotation(token);
      } else {
        token = addSingleQuotation(token);
      }
      tokens.set(i, token);
    }
  }

  private static List<String> splitTokens(String sentenceTosearch) {
    StringTokenizer tokenizer = new StringTokenizer(sentenceTosearch, "'", true);
    List<String> tokens = new ArrayList<>();
    while (tokenizer.hasMoreElements()) {
      tokens.add(tokenizer.nextToken());
    }
    return tokens;
  }

}
