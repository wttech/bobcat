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
package com.cognifide.qa.bb.aem.core.login;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.guice.ThreadScoped;
import com.google.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provide authentication cookie for aem instance (default cookie name: login-token)
 */
@ThreadScoped
public class AemAuthCookieFactoryImpl implements AemAuthCookieFactory {

  private static final String WEBDRIVER_PROXY_COOKIE = "webdriver.secure.proxy.cookie";

  private static final String WEBDRIVER_PROXY_COOKIE_NAME = "webdriver.secure.proxy.cookie_name";

  private static final String WEBDRIVER_PROXY_COOKIE_VALUE = "webdriver.secure.proxy.cookie_value";

  private static final String WEBDRIVER_PROXY_COOKIE_DOMAIN = "webdriver.secure.proxy.cookie_domain";

  private static final Logger LOG = LoggerFactory.getLogger(AemAuthCookieFactoryImpl.class);

  private static final String DEFAULT_LOGIN_TOKEN = "login-token";

  private final Map<String, Cookie> cookieJar = new HashMap<>();

  @Inject
  private Properties properties;

  @Inject
  private CloseableHttpClient httpClient;

  /**
   * This method provides browser cookie for authenticating user to AEM instance
   *
   * @param url URL to AEM instance, like http://localhost:4502
   * @param login Username to use
   * @param password Password to use
   * @return Cookie for selenium WebDriver.
   */
  @Override
  public Cookie getCookie(String url, String login, String password) {
    if (!cookieJar.containsKey(url)) {
      HttpPost loginPost = new HttpPost(url
          + "/libs/granite/core/content/login.html/j_security_check");

      List<NameValuePair> nameValuePairs = new ArrayList<>();
      nameValuePairs.add(new BasicNameValuePair("_charset_", "utf-8"));
      nameValuePairs.add(new BasicNameValuePair("j_username", login));
      nameValuePairs.add(new BasicNameValuePair("j_password", password));
      nameValuePairs.add(new BasicNameValuePair("j_validate", "true"));

      CookieStore cookieStore = new BasicCookieStore();
      HttpClientContext context = HttpClientContext.create();

      if ("true".equals(properties.getProperty(WEBDRIVER_PROXY_COOKIE))) {
        addProxyCookie(cookieStore);
      }

      context.setCookieStore(cookieStore);

      try {
        loginPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        CloseableHttpResponse loginResponse = httpClient.execute(loginPost, context);
        loginResponse.close();
      } catch (IOException e) {
        LOG.error("Can't get AEM authentication cookie", e);
      } finally {
        loginPost.reset();
      }
      Cookie cookie = findAuthenticationCookie(cookieStore.getCookies());
      if (cookie != null) {
        cookieJar.put(url, cookie);
      }
    }
    return cookieJar.get(url);
  }

  /**
   * This method allows to remove cached authentication cookie for a given URL
   *
   * @param url URL to AEM instance, like http://localhost:4502
   */
  @Override
  public void removeCookie(String url) {
    cookieJar.remove(url);
  }

  private Cookie findAuthenticationCookie(List<org.apache.http.cookie.Cookie> cookies) {
    for (org.apache.http.cookie.Cookie cookie : cookies) {
      if (properties.getProperty(ConfigKeys.LOGIN_TOKEN, DEFAULT_LOGIN_TOKEN)
          .equals(cookie.getName())) {
        return new Cookie(cookie.getName(), cookie.getValue(), cookie.getDomain(), cookie.getPath(),
            cookie.getExpiryDate());
      }
    }
    return null;
  }

  private void addProxyCookie(CookieStore cookieStore) {
    BasicClientCookie proxyCookie = new BasicClientCookie(
        properties.getProperty(WEBDRIVER_PROXY_COOKIE_NAME),
        properties.getProperty(WEBDRIVER_PROXY_COOKIE_VALUE));
    proxyCookie.setDomain(properties.getProperty(WEBDRIVER_PROXY_COOKIE_DOMAIN));
    proxyCookie.setPath("/");
    cookieStore.addCookie(proxyCookie);
  }
}
