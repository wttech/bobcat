package com.cognifide.qa.bb.constants;

/*-
 * #%L
 * Bobcat Parent
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


public final class Timeouts {

  public static int BIG = 30;

  public static int MEDIUM = 15;

  public static int SMALL = 5;

  public static int MINIMAL = 1;

  private static Timeouts instance;

  private Timeouts() {
  }

  public Timeouts(int big, int medium, int small, int minimal) {
    synchronized (this) {
      if (instance == null) {
        BIG = big;
        SMALL = small;
        MEDIUM = medium;
        MINIMAL = minimal;
        instance = this;
      }
    }
  }

}
