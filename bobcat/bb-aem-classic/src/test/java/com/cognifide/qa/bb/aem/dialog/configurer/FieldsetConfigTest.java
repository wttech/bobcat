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
package com.cognifide.qa.bb.aem.dialog.configurer;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class FieldsetConfigTest {

  @Mock
  private ConfigurationEntry entry;

  @Before
  public void prepareMockConfigurationEntry() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldCreateConfig() throws Exception {
    when(entry.getType()).thenReturn("fieldset#radio group");
    when(entry.getLabel()).thenReturn("Fieldset#Radio Group");

    FieldsetConfig fieldsetConfig = new FieldsetConfig(entry, mock(DialogFieldMap.class));
    assertNotNull("fieldset config should be created", fieldsetConfig);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForMissingType() throws Exception {
    when(entry.getType()).thenReturn("fieldset#");
    when(entry.getLabel()).thenReturn("Fieldset#Radio Group");
    FieldsetConfig fieldsetConfig = new FieldsetConfig(entry, mock(DialogFieldMap.class));
    assertNotNull("fieldset config should be created", fieldsetConfig);
  }

  @Test(expected = IllegalArgumentException.class)
  public void shouldThrowExceptionForMissingLabel() throws Exception {
    when(entry.getType()).thenReturn("fieldset#radio group");
    when(entry.getLabel()).thenReturn("Fieldset#");
    FieldsetConfig fieldsetConfig = new FieldsetConfig(entry, mock(DialogFieldMap.class));
    assertNotNull("fieldset config should be created", fieldsetConfig);
  }
}
