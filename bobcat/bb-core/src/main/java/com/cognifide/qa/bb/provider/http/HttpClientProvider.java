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
package com.cognifide.qa.bb.provider.http;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Provider;

/**
 * This class produces CloseableHttpClient instances that are used in ContentInstaller.
 */
public class HttpClientProvider implements Provider<CloseableHttpClient> {

  private static final Logger LOG = LoggerFactory.getLogger(HttpClientProvider.class);

  private final String login;

  private final String password;

  private final String url;

  /**
   * Constructs HttpClientProvider.
   *
   * @param login    Provider will configure the client's credentials with this login.
   * @param password Provider will configure the client's credentials with this password.
   * @param url      Provider will bind login and password to this url.
   */
  public HttpClientProvider(String login, String password, String url) {
    this.login = login;
    this.password = password;
    this.url = url;
  }

  /**
   * Produces http client and configures it with provided credentials.
   */
  @Override
  public CloseableHttpClient get() {
    HttpClientBuilder builder = HttpClientBuilder.create();
    if (StringUtils.isNotBlank(login) && StringUtils.isNotBlank(password)) {
      builder.setDefaultCredentialsProvider(createCredentialsProvider(url, login, password));
    }
    return builder.build();
  }

  private CredentialsProvider createCredentialsProvider(String url, String login, String password) {
    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider
        .setCredentials(getAuthScope(url), new UsernamePasswordCredentials(login, password));
    return credentialsProvider;
  }

  private AuthScope getAuthScope(String urlString) {
    String host = AuthScope.ANY_HOST;
    int port = AuthScope.ANY_PORT;
    try {
      URI uri = new URI(urlString);
      host = StringUtils.defaultString(uri.getHost(), AuthScope.ANY_HOST);
      port = uri.getPort();
    } catch (URISyntaxException e) {
      LOG.error("Could not parse '{}' as a valid URI", urlString, e);
    }
    return new AuthScope(host, port);
  }
}
