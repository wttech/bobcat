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
package com.cognifide.qa.bb.aem.core.component.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class ComponentConfigResourceFileReaderTest {

  private ComponentConfigResourceFileReader testObject = new ComponentConfigResourceFileReader();

  @Test
  public void readConfigurationTest() {
    ComponentConfiguration componentConfiguration = testObject
        .readConfiguration(new ResourceFileLocation("componentConfig.yaml"));
    assertThat(componentConfiguration.getTabs().size()).isEqualTo(2);
  }
}
