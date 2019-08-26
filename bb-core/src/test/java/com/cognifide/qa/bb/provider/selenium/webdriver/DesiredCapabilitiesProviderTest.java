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
package com.cognifide.qa.bb.provider.selenium.webdriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Capabilities;

import com.cognifide.qa.bb.provider.selenium.webdriver.CapabilitiesProvider;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities.CapabilitiesModifiers;

@ExtendWith(MockitoExtension.class)
public class DesiredCapabilitiesProviderTest {

  private static final String OUTERMAP = "outermap";
  private static final String THEMAP = "themap";

  @Mock
  private CapabilitiesModifiers capabilitiesModifiers;

  @InjectMocks
  private CapabilitiesProvider tested;

  @Spy
  private final Properties properties = new Properties();

  @BeforeEach
  void setup() {
    when(capabilitiesModifiers.modifyCapabilities(any())).thenAnswer(i -> i.getArguments()[0]);
  }

  @Test
  public void simpleCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.cap.simple1", "value1");
    properties.put("webdriver.cap.simple2", "value2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability("simple1")).isEqualTo("value1");
    assertThat(capabilities.getCapability("simple2")).isEqualTo("value2");
  }

  @Test
  public void mapCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.map.cap.themap.key1", "mvalue1");
    properties.put("webdriver.map.cap.themap.key2", "mvalue2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability(THEMAP)).isInstanceOf(Map.class);
    assertThat(((Map<?, ?>) capabilities.getCapability(THEMAP)).get("key1")).isEqualTo("mvalue1");
    assertThat(((Map<?, ?>) capabilities.getCapability(THEMAP)).get("key2")).isEqualTo("mvalue2");
  }

  @Test
  public void nestedMapCapabilitiesTest() {
    properties.clear();
    properties.put("webdriver.map.cap.outermap.top", "topvalue");
    properties.put("webdriver.map.cap.outermap.innermap.ikey1", "ivalue1");
    properties.put("webdriver.map.cap.outermap.innermap.ikey2", "ivalue2");

    Capabilities capabilities = tested.get();

    assertThat(capabilities.getCapability(OUTERMAP)).isInstanceOf(Map.class);
    assertThat(((Map<?, ?>) capabilities.getCapability(OUTERMAP)).get("top"))
        .isEqualTo("topvalue");
    assertThat(((Map<?, ?>) capabilities.getCapability(OUTERMAP)).get("innermap"))
        .isInstanceOf(Map.class);
    Map<?, ?> innerMap =
        (Map<?, ?>) ((Map<?, ?>) capabilities.getCapability(OUTERMAP)).get("innermap");
    assertThat(innerMap.get("ikey1")).isEqualTo("ivalue1");
    assertThat(innerMap.get("ikey2")).isEqualTo("ivalue2");
  }
}

