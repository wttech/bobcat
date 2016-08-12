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
package com.cognifide.bdd.demo.frame;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.frame.MainPage;
import com.cognifide.qa.bb.constants.HtmlTags;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class FrameTest {

  @Inject
  private MainPage mainPage;

  @Test
  public void getFrameByName() {
    assertThat(mainPage.open().getFrameWithName().getAttribute(HtmlTags.Attributes.CLASS),
        is("test-css"));
  }

  @Test
  public void getFrameByAbsoluteName() {
    assertThat(mainPage.open().getFrameWithPath().getText(), is("main-frame-2"));
  }

  @Test
  public void getFrameByIndex() {
    assertThat(mainPage.open().getFrameWithIndex().getText(), is("main-frame-3"));
  }

  @Test
  public void nestedFrames() {
    assertThat(mainPage.open().getFrameWithName().getSubFrame11().getText(), is("subframe-1-1"));
  }

  @Test
  public void parentFrame() {
    assertThat(mainPage.open().getFrameWithName().getSubFrame11().getParentText(),
        is("main-frame-1\nmain-frame-1-subtitle"));
    assertThat(mainPage.open().getFrameWithName().getSubFrame11().getGrandParentText(),
        is("index"));
  }

  @Test
  public void interspersedFrames() {
    Assert.assertEquals(
        Arrays.asList("index", "main-frame-1\nmain-frame-1-subtitle", "main-frame-2",
            "main-frame-3", "index"),
        mainPage.open().getInterspersed().index());
  }

  @Test
  public void getFrameSubTitle() {
    assertThat(mainPage.open().getFrameWithName().getSubTitleText(), is("main-frame-1-subtitle"));
  }
}
