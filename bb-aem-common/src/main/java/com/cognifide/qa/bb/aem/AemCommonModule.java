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
package com.cognifide.qa.bb.aem;

import java.util.Properties;

import org.apache.http.impl.client.CloseableHttpClient;

import com.cognifide.qa.bb.constants.AemConfigFilenames;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.provider.http.HttpClientProvider;
import com.cognifide.qa.bb.provider.jcr.properties.AuthorProperties;
import com.cognifide.qa.bb.provider.jcr.properties.InstanceProperties;
import com.cognifide.qa.bb.provider.jcr.properties.PublishProperties;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * Install this module to enable aem-commons capabilities. Required to use the Jcr interface.
 */
public class AemCommonModule extends AbstractModule {
  @Override
  protected void configure() {
    // jcr session
    bind(InstanceProperties.class).annotatedWith(Names.named(AemConfigFilenames.PUBLISH_PROPERTIES))
        .to(PublishProperties.class);
    bind(InstanceProperties.class).annotatedWith(Names.named(AemConfigFilenames.AUTHOR_PROPERTIES))
        .to(AuthorProperties.class);

    // copy "publish.url" value to "base.url" fields used in bb-core
    requestInjection(new Object() {
      @Inject
      public void init(Properties properties) {
        String publishUrl = properties.getProperty(AemConfigKeys.PUBLISH_URL);
        properties.put(ConfigKeys.BASE_URL, publishUrl);
      }
    });
  }

  /**
   * Provider method that produces a CloseableHttpClient instances configured with URL, login and password
   * taken from property files.
   *
   * @param url      IP address of Author instance, automatically set to author.ip property.
   * @param login    Author login, automatically set to author.login property.
   * @param password Author password, automatically set to author.password property.
   * @return New instance of CloseableHttpClient.
   */
  @Provides
  public CloseableHttpClient getAuthorHttpClient(@Named(AemConfigKeys.AUTHOR_IP) String url,
      @Named(AemConfigKeys.AUTHOR_LOGIN) String login,
      @Named(AemConfigKeys.AUTHOR_PASSWORD) String password) {
    return new HttpClientProvider(login, password, url).get();
  }

}
