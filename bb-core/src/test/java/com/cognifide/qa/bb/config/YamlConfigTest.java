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
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.cognifide.qa.bb.config.yaml.Config;
import com.cognifide.qa.bb.config.yaml.DefaultConfig;
import com.cognifide.qa.bb.utils.SystemPropertiesCleanupExtension;

@ExtendWith(SystemPropertiesCleanupExtension.class)
public class YamlConfigTest {

  private static final String OVERRIDEN = "overriden";

  @Test
  public void shouldLoadAllDefaultProperties() {
    Config defaultYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    defConfig.setProperties(mapOf(
        entry("default.property1", "value1"),
        entry("default.property2", "value2")));
    defaultYaml.setDefaultConfig(defConfig);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readDefaultYaml()).thenReturn(defaultYaml);
    doCallRealMethod().when(tested).loadDefaultConfig();

    Properties actual = tested.loadDefaultConfig();

    assertThat(actual).containsAllEntriesOf(defConfig.getProperties());
  }

  @Test
  public void shouldLoadOnlySelectedContextsWhenDefaultContextsAreSet() {
    Config userYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    defConfig.setContexts(Arrays.asList("context1", "context2"));
    userYaml.setDefaultConfig(defConfig);
    Map<String, Map<String, String>> contexts = new HashMap<>();
    Map.Entry[] context1Entries = {entry("property1", "value1"), entry("property2", "value2")};
    Map.Entry[] context2Entries = {entry("property3", "value3"), entry("property4", "value4")};
    Map.Entry[] context3Entires = {entry("property5", "value5"), entry("property6", "value6")};
    contexts.put("context1", mapOf(context1Entries));
    contexts.put("context2", mapOf(context2Entries));
    contexts.put("context3", mapOf(context3Entires));
    userYaml.setContexts(contexts);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readUserYaml()).thenReturn(userYaml);
    doCallRealMethod().when(tested).loadConfig();

    Properties actual = tested.loadConfig();

    assertThat(actual).containsOnly(ArrayUtils.addAll(context1Entries, context2Entries));
  }

  @Test
  public void lshouldLoadNoContextsWhenNoneAreSelected() {
    Config userYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    Map.Entry[] defaultEntries = {entry("property1", "value1"), entry("property2", "value2")};
    defConfig.setProperties(mapOf(defaultEntries));
    userYaml.setDefaultConfig(defConfig);
    Map<String, Map<String, String>> contexts = new HashMap<>();
    Map.Entry[] context1Entries = {entry("property3", "value3"), entry("property4", "value4")};
    Map.Entry[] context2Entries = {entry("property5", "value5"), entry("property6", "value6")};
    contexts.put("context1", mapOf(context1Entries));
    contexts.put("context2", mapOf(context2Entries));
    userYaml.setContexts(contexts);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readUserYaml()).thenReturn(userYaml);
    doCallRealMethod().when(tested).loadConfig();

    Properties actual = tested.loadConfig();

    assertThat(actual).containsOnly(defaultEntries);
  }

  @Test
  public void shouldOverrideDefaultPropertiesWhenThereAreOverridesLoadedFromContexts() {
    Config userYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    Map.Entry[] defaultEntries = {entry("property1", "value1"), entry("property2", "value2")};
    defConfig.setProperties(mapOf(defaultEntries));
    defConfig.setContexts(Collections.singletonList("context1"));
    userYaml.setDefaultConfig(defConfig);
    Map<String, Map<String, String>> contexts = new HashMap<>();
    Map.Entry[] context1Entries =
        {entry("property1", OVERRIDEN), entry("property2", OVERRIDEN)};
    contexts.put("context1", mapOf(context1Entries));
    userYaml.setContexts(contexts);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readUserYaml()).thenReturn(userYaml);
    doCallRealMethod().when(tested).loadConfig();

    Properties actual = tested.loadConfig();

    assertThat(actual)
        .containsOnly(entry("property1", OVERRIDEN), entry("property2", OVERRIDEN));
  }

  @Test
  public void shouldOverrideDefaultContextsWhenContextsAreProvidedInSysProp() {
    Config userYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    defConfig.setContexts(Arrays.asList("context1", "context2"));
    userYaml.setDefaultConfig(defConfig);
    Map<String, Map<String, String>> contexts = new HashMap<>();
    Map.Entry[] context1Entries = {entry("property1", "value1"), entry("property2", "value2")};
    Map.Entry[] context2Entries = {entry("property3", "value3"), entry("property4", "value4")};
    Map.Entry[] context3Entires = {entry("property5", "value5"), entry("property6", "value6")};
    contexts.put("context1", mapOf(context1Entries));
    contexts.put("context2", mapOf(context2Entries));
    contexts.put("context3", mapOf(context3Entires));
    userYaml.setContexts(contexts);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readUserYaml()).thenReturn(userYaml);
    doCallRealMethod().when(tested).loadConfig();

    System.setProperty(YamlConfig.SYS_PROP_CONFIG_CONTEXTS, "context3");
    Properties actual = tested.loadConfig();

    assertThat(actual).containsOnly(context3Entires);
  }

  @Test
  public void shouldLoadAdditionalContextsWhenTheyAreAvailableInSeparateFilesAndActivated() {
    Config userYaml = new Config();
    DefaultConfig defConfig = new DefaultConfig();
    defConfig.setContexts(Arrays.asList("additional-context1", "additional-context2"));
    userYaml.setDefaultConfig(defConfig);
    Map<String, Map<String, String>> contexts = new HashMap<>();
    Map.Entry[] context1Entries = {entry("property1", "value1"), entry("property2", "value2")};
    Map.Entry[] context2Entries = {entry("property3", "value3"), entry("property4", "value4")};
    Map.Entry[] context3Entires = {entry("property5", "value5"), entry("property6", "value6")};
    contexts.put("context1", mapOf(context1Entries));
    contexts.put("context2", mapOf(context2Entries));
    contexts.put("context3", mapOf(context3Entires));
    userYaml.setContexts(contexts);

    YamlConfig tested = mock(YamlConfig.class);
    when(tested.readUserYaml()).thenReturn(userYaml);
    doCallRealMethod().when(tested).loadConfig();

    System.setProperty(YamlConfig.SYS_PROP_CONFIG_CONTEXTS, "context3");
    Properties actual = tested.loadConfig();

    assertThat(actual).containsOnly(context3Entires);
  }
}
