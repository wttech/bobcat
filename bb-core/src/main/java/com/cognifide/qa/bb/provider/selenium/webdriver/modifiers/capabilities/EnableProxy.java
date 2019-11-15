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
package com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class EnableProxy implements CapabilitiesModifier {

  @Inject
  @Named(ConfigKeys.PROXY_ENABLED)
  private boolean proxyEnabled;

  @Inject
  private Proxy proxy;

  @Override
  public boolean shouldModify() {
    return proxyEnabled;
  }

  @Override
  public Capabilities modify(Capabilities capabilities) {
    return enableProxy(capabilities);
  }

  private DesiredCapabilities enableProxy(Capabilities capabilities) {
    DesiredCapabilities caps = new DesiredCapabilities(capabilities);
    caps.setCapability(CapabilityType.PROXY, proxy);
    return caps;
  }
}
