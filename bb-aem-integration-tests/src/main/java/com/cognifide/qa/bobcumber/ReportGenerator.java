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
package com.cognifide.qa.bobcumber;

import com.cognifide.qa.bb.modules.PropertyModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Created by daniel.madejek on 2016-08-29.
 */
public class ReportGenerator {
  public static void main(String... args) {
    Injector injector = Guice.createInjector(new PropertyModule());
    com.cognifide.qa.bb.cucumber.reports.ReportGenerator generator =
        injector.getInstance(com.cognifide.qa.bb.cucumber.reports.ReportGenerator.class);
    generator.generateReport();
  }
}
