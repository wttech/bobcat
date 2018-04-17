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
package com.cognifide.qa.bb.config;

import static com.cognifide.qa.bb.utils.MapUtils.entry;
import static com.cognifide.qa.bb.utils.MapUtils.mapOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.cognifide.qa.bb.utils.SystemPropertiesCleanupRule;
import com.google.common.collect.Maps;

public class ConfigStrategyTest {

  private ConfigStrategy strategy = new DefaultConfigStrategy();

  @Rule
  public SystemPropertiesCleanupRule cleanupSysProps = new SystemPropertiesCleanupRule();

  @Test
  public void gatherProperties_loadsPropertiesInCorrectOrder() {
    ConfigStrategy tested = mock(ConfigStrategy.class);
    InOrder inOrder = inOrder(tested);

    when(tested.loadDefaultConfig()).thenReturn(new Properties());
    when(tested.loadConfig()).thenReturn(new Properties());

    doCallRealMethod().when(tested).gatherProperties();

    tested.gatherProperties();

    inOrder.verify(tested).loadDefaultConfig();
    inOrder.verify(tested).loadConfig();
    inOrder.verify(tested).overrideFromSystemProperties(any());
    inOrder.verify(tested).setSystemProperties(any());
  }

  @Test
  public void gatherProperties_resolvesConfigCorrectly() {
    Properties expected = new Properties();
    expected.putAll(mapOf(
        entry("webdriver.reusable", "true"),
        entry("webdriver.maximize", "true"),
        entry("parsys.locator.format", "%s"),
        entry("proxy.enabled", "false")));

    System.setProperty("webdriver.reusable", "true");

    Properties actual = strategy.gatherProperties();

    assertThat(actual).containsAllEntriesOf(expected);
  }

  @Test
  public void overrideFromSystemProperties_overridesConfigProperties_whenSysPropsAreNotBlank() {
    Properties configProperties = new Properties();
    configProperties.putAll(mapOf(
        entry("override.prop", "should-be-overriden"),
        entry("do.not.override", "not-overriden")));

    System.setProperty("override.prop", "overriden");
    System.setProperty("do.not.override", "");

    Properties actual = strategy.overrideFromSystemProperties(configProperties);

    assertThat(actual)
        .containsEntry("override.prop", "overriden")
        .containsEntry("do.not.override", "not-overriden");
  }

  @Test
  public void setSystemProperties_setsSystemProperties_onlyForWebdriverPropertiesInConfig() {
    Map<String, String> expected = mapOf(
        entry("webdriver.maximize", ""),
        entry("webdriver.reusable", ""));

    Properties configProperties = new Properties();
    configProperties.putAll(mapOf(
        entry("webdriver.maximize", ""),
        entry("webdriver.reusable", ""),
        entry("parsys.locator.format", "%s"),
        entry("proxy.enabled", "false")));

    strategy.setSystemProperties(configProperties);
    Properties actual = System.getProperties();

    assertThat(actual)
        .containsAllEntriesOf(expected)
        .doesNotContainKeys(Maps.difference(configProperties, expected).entriesDiffering());
  }

  @Test
  public void setSystemProperties_doesNotSetSystemProperties_whenNoWebdriverPropertiesArePresentInConfig() {
    Properties configProperties = new Properties();
    configProperties.putAll(mapOf(
        entry("custom.type", "firefox"),
        entry("additional.property", ""),
        entry("parsys.locator.format", "%s"),
        entry("proxy.enabled", "false")));

    strategy.setSystemProperties(configProperties);

    Properties actual = System.getProperties();

    assertThat(actual).doesNotContainKeys(configProperties.keys());
  }

  class DefaultConfigStrategy implements ConfigStrategy {

    @Override
    public Properties loadDefaultConfig() {
      Properties properties = new Properties();
      properties.putAll(mapOf(
          entry("webdriver.reusable", "false"),
          entry("parsys.locator.format", "%s"),
          entry("proxy.enabled", "false")));
      return properties;
    }

    @Override
    public Properties loadConfig() {
      Properties properties = new Properties();
      properties.putAll(mapOf(
          entry("webdriver.maximize", "true"),
          entry("proxy.enabled", "false")));
      return properties;
    }
  }
}
