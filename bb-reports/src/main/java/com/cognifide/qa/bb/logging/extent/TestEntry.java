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
package com.cognifide.qa.bb.logging.extent;

import com.aventstack.extentreports.ExtentTest;

/**
 * Created by daniel.madejek on 2017-05-26.
 */
public class TestEntry {

  private ExtentTest currentTest;

  private TestEntry parentTest;

  public TestEntry(ExtentTest currentTest, TestEntry parentTest) {
    this.currentTest = currentTest;
    this.parentTest = parentTest;
  }

  public ExtentTest getCurrentTest() {
    return currentTest;
  }

  public void setCurrentTest(ExtentTest currentTest) {
    this.currentTest = currentTest;
  }

  public TestEntry getParentTest() {
    return parentTest;
  }

  public void setParentTest(TestEntry parentTest) {
    this.parentTest = parentTest;
  }
}
