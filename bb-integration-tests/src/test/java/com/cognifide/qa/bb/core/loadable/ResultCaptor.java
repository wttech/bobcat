/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.core.loadable;

import java.util.LinkedList;

import com.cognifide.qa.bb.loadable.annotation.LoadableComponent;
import com.google.inject.Singleton;

@Singleton
public class ResultCaptor {

  private final LinkedList<Object> subjects;

  private final LinkedList<Object> conditionInfo;

  public ResultCaptor() {
    this.conditionInfo = new LinkedList<>();
    this.subjects = new LinkedList<>();
  }

  public void storeConditionStep(Object subject, LoadableComponent data) {
    subjects.add(subject);
    conditionInfo.add(data);
  }

  public LinkedList<Object> getSubjects() {
    return subjects;
  }

  public LinkedList<Object> getConditionInfo() {
    return conditionInfo;
  }

  void reset() {
    subjects.clear();
    conditionInfo.clear();
  }

}
