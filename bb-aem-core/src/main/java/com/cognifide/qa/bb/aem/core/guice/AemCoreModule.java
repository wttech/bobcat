/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2018 Cognifide Ltd.
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
package com.cognifide.qa.bb.aem.core.guice;

import com.cognifide.qa.bb.aem.core.constants.AemConfigKeys;
import com.cognifide.qa.bb.provider.http.HttpClientProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Main module that need to be installed to use AEM functions
 */
public class AemCoreModule extends AbstractModule {

  @Provides
  public CloseableHttpClient getAuthorHttpClient(@Named(AemConfigKeys.AUTHOR_IP) String url,
      @Named(AemConfigKeys.AUTHOR_LOGIN) String login,
      @Named(AemConfigKeys.AUTHOR_PASSWORD) String password) {
    return new HttpClientProvider(login, password, url).get();
  }

  @Override
  protected void configure() {
    //nothing here
  }
}
