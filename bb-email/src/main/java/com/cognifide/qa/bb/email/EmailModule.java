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

import com.cognifide.qa.bb.email.connector.Connector;
import com.cognifide.qa.bb.email.connector.ConnectorProvider;
import com.google.inject.AbstractModule;

/**
 * Install this module to enable email service. After installation, following email protocols will
 * be available:
 * <ul>
 * <li>IMAP,
 * <li>IMAPS,
 * <li>POP3.
 * </ul>
 */
public class EmailModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Connector.class).toProvider(ConnectorProvider.class);
  }
}
