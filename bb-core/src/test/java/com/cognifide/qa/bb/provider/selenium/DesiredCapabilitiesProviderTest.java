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
package com.cognifide.qa.bb.provider.selenium;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.Capabilities;

@RunWith(MockitoJUnitRunner.class)
public class DesiredCapabilitiesProviderTest {

  @InjectMocks
  private DesiredCapabilitiesProvider tested;

  @Spy
  private final Properties properties = new Properties();

  @Test
  public void simpleCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.cap.simple1", "value1");
    properties.put("webdriver.cap.simple2", "value2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability("simple1"), is((Object) "value1"));
    assertThat(capabilities.getCapability("simple2"), is((Object) "value2"));
  }

  @Test
  public void mapCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.map.cap.themap.key1", "mvalue1");
    properties.put("webdriver.map.cap.themap.key2", "mvalue2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability("themap"), instanceOf(Map.class));
    assertThat(((Map<?, ?>) capabilities.getCapability("themap")).get("key1"),
        is((Object) "mvalue1"));
    assertThat(((Map<?, ?>) capabilities.getCapability("themap")).get("key2"),
        is((Object) "mvalue2"));
  }

  @Test
  public void nestedMapCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.map.cap.outermap.top", "topvalue");
    properties.put("webdriver.map.cap.outermap.innermap.ikey1", "ivalue1");
    properties.put("webdriver.map.cap.outermap.innermap.ikey2", "ivalue2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability("outermap"), instanceOf(Map.class));
    assertThat(((Map<?, ?>) capabilities.getCapability("outermap")).get("top"),
        is((Object) "topvalue"));
    assertThat(((Map<?, ?>) capabilities.getCapability("outermap")).get("innermap"),
        instanceOf(Map.class));
    Map<?, ?> innerMap =
        (Map<?, ?>) ((Map<?, ?>) capabilities.getCapability("outermap")).get("innermap");
    assertThat(innerMap.get("ikey1"), is((Object) "ivalue1"));
    assertThat(innerMap.get("ikey2"), is((Object) "ivalue2"));
  }

}

