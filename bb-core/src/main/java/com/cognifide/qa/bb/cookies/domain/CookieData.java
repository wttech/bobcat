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
package com.cognifide.qa.bb.cookies.domain;

import java.util.Date;

import org.openqa.selenium.Cookie;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Describes data of a single cookie.
 */
public class CookieData {
  private String name;

  private String value;

  private String path;

  private String domain;

  private Date expiry;

  private boolean secure;

  private boolean httpOnly;

  @JsonCreator
  public CookieData(
      @JsonProperty("name") String name,
      @JsonProperty("value") String value,
      @JsonProperty("path") String path,
      @JsonProperty("domain") String domain,
      @JsonProperty("expiry")
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss") Date expiry,
      @JsonProperty("secure") boolean secure,
      @JsonProperty("httpOnly") boolean httpOnly) {
    this.name = name;
    this.value = value;
    this.path = path;
    this.domain = domain;
    this.expiry = expiry;
    this.secure = secure;
    this.httpOnly = httpOnly;
  }

  /**
   * @return the URL to the domain related to the cookie (appends {@code http://} to the domain)
   */
  public String getUrl() {
    return "http://" + domain;
  }

  /**
   * @return adapter to {@link org.openqa.selenium.Cookie}
   */
  public Cookie convertToSeleniumCookie() {
    return new Cookie(name, value, domain, path, expiry, secure, httpOnly);
  }
}
