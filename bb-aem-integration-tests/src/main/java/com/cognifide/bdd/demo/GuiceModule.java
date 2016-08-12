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

import com.cognifide.qa.bb.AemClassicModule;
import com.cognifide.qa.bb.aem.AemCommonModule;
import com.cognifide.qa.bb.aem.touch.modules.AemTouchUiModule;
import com.cognifide.qa.bb.cumber.guice.BobcumberModule;
import com.cognifide.qa.bb.logging.ReporterModule;
import com.cognifide.qa.bb.modules.CoreModule;
import com.cognifide.qa.bb.traffic.TrafficModule;
import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {
  @Override
  protected void configure() {
    if (System.getProperty("configuration.paths") == null) {
      System.setProperty("configuration.paths", "src/main/config/dev;src/main/config/common");
    }
    install(new CoreModule());
    install(new AemCommonModule());
    install(new AemClassicModule());
    install(new ReporterModule());
    install(new BobcumberModule());
    install(new TrafficModule());
    install(new AemTouchUiModule());
  }
}
