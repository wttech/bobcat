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
package com.cognifide.qa.bb.loadable.context;

import com.cognifide.qa.bb.utils.AopUtil;

public abstract class LoadableContext {

  protected final Object subject;

  protected final Class subjectClass;

  public LoadableContext(Class subjectClass) {
    this.subject = null;
    this.subjectClass = AopUtil.getBaseClassForAopObject(subjectClass);
  }

  public LoadableContext(Object subject) {
    this.subject = subject;
    this.subjectClass = AopUtil.getBaseClassForAopObject(subject);
  }

  public LoadableContext(Object subject, Class subjectClass) {
    this.subject = subject;
    this.subjectClass = subjectClass;
  }

  public Object getSubject() {
    return subject;
  }

  /**
   *
   * @return Subject's class
   */
  public Class getSubjectClass() {
    return subjectClass;
  }

}
