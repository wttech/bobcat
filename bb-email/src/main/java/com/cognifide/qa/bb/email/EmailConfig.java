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
package com.cognifide.qa.bb.email;

import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.cognifide.qa.bb.email.constants.EmailConfigKeys;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.google.inject.name.Names;

public class EmailConfig {

  private final Map<String, String> map;
  private final Injector injector;

  @AssistedInject
  public EmailConfig(@Assisted String id, Injector injector) {
    this.map = Collections.singletonMap(EmailConfigKeys.ID, id);
    this.injector = injector;
  }

  public String getParameter(String name) {
    return getParameter(String.class, name);
  }

  public <T> T getParameter(Class<T> type, String name) {
    String realId = StrSubstitutor.replace(name, map);
    return injector.getInstance(Key.get(type, Names.named(realId)));
  }
}
