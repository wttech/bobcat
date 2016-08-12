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
package com.cognifide.bdd.demo.subreports;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.logging.ReportEntryLogger;
import com.cognifide.qa.bb.logging.subreport.Subreport;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class SubreportTest {

  @Inject
  private ReportEntryLogger reportEntryLogger;

  @Test
  public void test() {
    reportEntryLogger.info("1");
    reportEntryLogger.startSubreport("a");
    reportEntryLogger.info("2");
    someMethod();
    reportEntryLogger.info("3");
    reportEntryLogger.endSubreport("a");
    reportEntryLogger.info("4");
    reportEntryLogger.startSubreport("b");
    reportEntryLogger.info("5");
    reportEntryLogger.startSubreport("c");
    reportEntryLogger.info("6");
  }

  @Subreport("someMethodSubreport")
  public void someMethod() {}
}
