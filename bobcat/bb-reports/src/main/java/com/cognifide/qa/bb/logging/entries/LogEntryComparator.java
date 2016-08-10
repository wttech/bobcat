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
package com.cognifide.qa.bb.logging.entries;

import java.io.Serializable;
import java.util.Comparator;

public class LogEntryComparator implements Comparator<LogEntry>, Serializable {

  private static final long serialVersionUID = -4013602729418907338L;

  private static final int FIRST_IS_LOWER = -1;

  private static final int SECOND_IS_LOWER = 1;

  private static final int EQUAL = 0;

  @Override
  public int compare(LogEntry o1, LogEntry o2) {
    final int result;
    boolean bothNull = o1 == null && o2 == null;

    if (bothNull) {
      result = EQUAL;
    } else {
      if (o1 == null) {
        result = FIRST_IS_LOWER;
      } else if (o2 == null) {
        result = SECOND_IS_LOWER;
      } else {
        result = Integer.compare(o1.getIndex(), o2.getIndex());
      }
    }
    return result;
  }
}
