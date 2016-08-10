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
package com.cognifide.bdd.demo.aem.packages;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.aem.content.ContentInstaller;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class ContentInstallerTest {

  private static final String AEM_PACKAGE_MY_PACKAGES =
      "my_packages/installAemPackageTest_my_packages";

  private static final String AEM_PACKAGE_ROOT = "installAemPackageTest";

  private static final String INVALID_PACKAGE = "invalidPackageName";

  private static final String ACTIVATE_AEM_PACKAGE_TEST_ZIP = "activateAemPackageTest.zip";

  @Inject
  private ContentInstaller installer;

  @Test
  public void installAemPackageInGroupTest_positive() throws IOException {
    installer.installAemPackage(AEM_PACKAGE_MY_PACKAGES);
  }

  @Test
  public void installAemPackageTest_positive() throws IOException {
    installer.installAemPackage(AEM_PACKAGE_ROOT);
  }

  @Test(expected = IOException.class)
  public void installAemPackageTest_negative() throws IOException {
    installer.installAemPackage(INVALID_PACKAGE);
  }

  @Test
  public void activateAemPackageTest() throws IOException {
    installer.activateAemPackage(ACTIVATE_AEM_PACKAGE_TEST_ZIP);
  }
}
