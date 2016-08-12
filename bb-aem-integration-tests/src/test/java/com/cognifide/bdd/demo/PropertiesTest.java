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
package com.cognifide.bdd.demo;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Properties;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.qa.bb.constants.ConfigKeys;
import com.cognifide.qa.bb.constants.AemConfigKeys;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;
import com.google.inject.name.Named;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class PropertiesTest {

  @Inject
  private Properties properties;

  @Inject
  @Named(AemConfigKeys.AUTHOR_LOGIN)
  private String authorLogin;

  @Test
  public void testInjectedProperty() {
    assertThat(authorLogin, is("admin"));
  }

  @Test
  public void testInjectedProperties() {
    assertThat(properties.get(AemConfigKeys.AUTHOR_LOGIN), is("admin"));
  }

  @Test
  public void testSystemProperty() {
    assertThat(System.getProperty("webdriver.type"), is(properties.get(ConfigKeys.WEBDRIVER_TYPE)));
  }
}
