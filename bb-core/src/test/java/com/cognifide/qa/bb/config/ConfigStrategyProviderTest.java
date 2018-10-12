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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.utils.SystemPropertiesCleanupRule;

public class ConfigStrategyProviderTest {

  @Rule
  public SystemPropertiesCleanupRule sysPropCleanup = new SystemPropertiesCleanupRule();

  @Test
  public void get_returnsYamlConfig_byDefault() {
    assumeTrue(StringUtils.isBlank(System.getProperty(ConfigKeys.CONFIG_STRATEGY)));
    ConfigStrategy configStrategy = ConfigStrategyProvider.get();
    assertThat(configStrategy).isInstanceOf(YamlConfig.class);
  }
}
