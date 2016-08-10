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
package com.cognifide.bdd.demo.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.cognifide.bdd.demo.BobcatWaitTest;
import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.PropertiesTest;
import com.cognifide.bdd.demo.aem.auth.ManualLoginTest;
import com.cognifide.bdd.demo.expectedConditions.CommonExpectedConditionTest;
import com.cognifide.bdd.demo.expectedConditions.UrlExpectedConditionsTest;
import com.cognifide.bdd.demo.frame.FrameTest;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.concurrent.ConcurrentSuite;

@Modules(GuiceModule.class)
@RunWith(ConcurrentSuite.class)
@Suite.SuiteClasses({
    FrameTest.class,
    PropertiesTest.class,
    ManualLoginTest.class,
    BobcatWaitTest.class,
    UrlExpectedConditionsTest.class,
    CommonExpectedConditionTest.class
})
public class BbCoreSuite {
  //Test suite, nothing to add here
}
