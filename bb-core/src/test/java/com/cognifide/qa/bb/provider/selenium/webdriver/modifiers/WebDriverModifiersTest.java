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
package com.cognifide.qa.bb.provider.selenium.webdriver.modifiers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.capabilities.CapabilitiesModifier;
import com.cognifide.qa.bb.provider.selenium.webdriver.modifiers.webdriver.WebDriverModifier;
import com.google.common.collect.Sets;

@ExtendWith(MockitoExtension.class)
public class WebDriverModifiersTest {

  private WebDriverModifiers testedObject;

  @Mock
  private WebDriverModifier mod1;

  @Mock
  private WebDriverModifier mod2;

  @Mock
  private WebDriverModifier mod3;

  @Mock
  private CapabilitiesModifier capMod1;

  @Mock
  private CapabilitiesModifier capMod2;

  @Mock
  private CapabilitiesModifier capMod3;

  @Mock
  private WebDriver webDriver;

  @Mock
  private Capabilities capabilities;

  @Test
  public void shouldSortAndFilterWebDriverModifiers() {
    //when
    setupWebDriverModifiers();
    testedObject.modifyWebDriver(webDriver);

    //then
    InOrder inOrder = inOrder(mod2, mod1);
    inOrder.verify(mod2).modify(webDriver);
    inOrder.verify(mod1).modify(webDriver);

    verify(mod3, never()).modify(webDriver);
  }

  @Test
  public void shouldSortAndFilterCapabilitiesModifiers() {
    //when
    setupCapabilitiesModifiers();
    testedObject.modifyCapabilities(capabilities);

    //then
    InOrder inOrder = inOrder(capMod2, capMod1);
    inOrder.verify(capMod2).modify(capabilities);
    inOrder.verify(capMod1).modify(capabilities);

    verify(capMod3, never()).modify(capabilities);
  }

  public void setupWebDriverModifiers() {
    when(mod1.getOrder()).thenReturn(1);
    when(mod1.shouldModify()).thenReturn(true);
    when(mod1.modify(any())).thenReturn(webDriver);
    when(mod2.getOrder()).thenReturn(-1);
    when(mod2.shouldModify()).thenReturn(true);
    when(mod2.modify(any())).thenReturn(webDriver);
    when(mod3.shouldModify()).thenReturn(false);

    testedObject =
        spy(new WebDriverModifiers(Collections.emptySet(), Sets.newHashSet(mod1, mod2, mod3)));
  }

  public void setupCapabilitiesModifiers() {
    when(capMod1.getOrder()).thenReturn(1);
    when(capMod1.shouldModify()).thenReturn(true);
    when(capMod1.modify(any())).thenReturn(capabilities);
    when(capMod2.getOrder()).thenReturn(-1);
    when(capMod2.shouldModify()).thenReturn(true);
    when(capMod2.modify(any())).thenReturn(capabilities);
    when(capMod3.shouldModify()).thenReturn(false);

    testedObject = spy(new WebDriverModifiers(Sets.newHashSet(capMod1, capMod2, capMod3),
        Collections.emptySet()));
  }
}
