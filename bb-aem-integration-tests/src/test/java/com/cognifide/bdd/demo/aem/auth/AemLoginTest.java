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
package com.cognifide.bdd.demo.aem.auth;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.login.ProjectsScreen;
import com.cognifide.bdd.demo.po.login.UserDialog;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemLoginTest {

  public static final String AUTHOR = "author";

  @Inject
  private ProjectsScreen projectScreen;

  @Inject
  private AemLogin aemLogin;

  @Test
  public void loginAsAuthorTest() {
    aemLogin.authorLogin();
    assertTrue(projectScreen.open().projectScreenIsDisplayed());
  }

  @Test
  public void loginLogoutTest() {
    aemLogin.authorLogin();
    assertTrue(projectScreen.open().projectScreenIsDisplayed());
    UserDialog userDialog = projectScreen.openUserDialog();
    assertThat(userDialog.getAccountName().getText(), is("Administrator"));
    userDialog.signOut();
    assertTrue(projectScreen.projectScreenIsNotDisplayed());
    aemLogin.authorLogin(AUTHOR, AUTHOR);
    assertTrue(projectScreen.open().projectScreenIsDisplayed());
    userDialog = projectScreen.openUserDialog();
    assertThat(userDialog.getAccountName().getText(), is("author"));
    userDialog.signOut();
    assertTrue(projectScreen.projectScreenIsNotDisplayed());
  }

  @Test
  public void logoutTest() {
    aemLogin.authorLogin();
    assertTrue(projectScreen.open().projectScreenIsDisplayed());
    aemLogin.authorLogout();
    assertTrue(projectScreen.projectScreenIsNotDisplayed());
  }
}
