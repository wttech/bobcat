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

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.aem.AemContentFinderTest;
import com.cognifide.bdd.demo.aem.AemBubbleMessageTest;
import com.cognifide.bdd.demo.aem.AemCheckboxTest;
import com.cognifide.bdd.demo.aem.AemContextMenuTest;
import com.cognifide.bdd.demo.aem.AemDropdownTest;
import com.cognifide.bdd.demo.aem.AemImageTest;
import com.cognifide.bdd.demo.aem.AemLookupFieldTest;
import com.cognifide.bdd.demo.aem.AemParsysTest;
import com.cognifide.bdd.demo.aem.AemRadioGroupTest;
import com.cognifide.bdd.demo.aem.AemRichtextTest;
import com.cognifide.bdd.demo.aem.AemSidekickTest;
import com.cognifide.bdd.demo.aem.AemTextAreaTest;
import com.cognifide.bdd.demo.aem.AemTextFieldTest;
import com.cognifide.bdd.demo.aem.ValidationWindowTest;
import com.cognifide.bdd.demo.aem.dialog.TabsTest;
import com.cognifide.bdd.demo.aem.list.AemListTest;
import com.cognifide.bdd.demo.aem.list.AemListItemTest;
import com.cognifide.bdd.demo.aem.tags.AemTagsTest;
import com.cognifide.bdd.demo.aem.wcm.SiteAdminTest;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.concurrent.ConcurrentSuite;

@Modules(GuiceModule.class)
@RunWith(ConcurrentSuite.class)
@Suite.SuiteClasses({
    AemImageTest.class,
    AemTextFieldTest.class,
    AemContentFinderTest.class,
    AemLookupFieldTest.class,
    AemParsysTest.class,
    AemContextMenuTest.class,
    AemTagsTest.class,
    AemRadioGroupTest.class,
    TabsTest.class,
    AemBubbleMessageTest.class,
    AemSidekickTest.class,
    SiteAdminTest.class,
    AemRichtextTest.class,
    AemListItemTest.class,
    AemListTest.class,
    AemCheckboxTest.class,
    AemDropdownTest.class,
    AemTextAreaTest.class,
    ValidationWindowTest.class
})
public class BbAemClassicSuite {
  //Test suite, nothing to add here
}
