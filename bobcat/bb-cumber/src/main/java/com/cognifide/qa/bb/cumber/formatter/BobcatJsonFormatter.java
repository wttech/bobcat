/*
 * Copyright 2016 Cognifide Ltd..
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.cognifide.qa.bb.cumber.formatter;

import com.cognifide.qa.bb.modules.PropertyModule;
import com.cognifide.qa.bb.quarantine.QuarantineModule;
import com.cognifide.qa.bb.quarantine.QuarantinedElement;
import com.cognifide.qa.bb.quarantine.TestsQuarantine;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cucumber.runtime.formatter.CucumberJSONFormatter;

import gherkin.formatter.model.*;
import gherkin.formatter.model.Scenario;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;

public class BobcatJsonFormatter extends CucumberJSONFormatter {

  private static final Logger LOG = LoggerFactory.getLogger(BobcatJsonFormatter.class);

  private static final String SKIPPED_STATUS = "skipped";

  private static final String FAILED_STATUS = "failed";

  private boolean isFirstStep = true;

  @Inject
  private TestsQuarantine testsQuarantine;

  @Inject
  @Named("quarantine.location")
  private String quarantineLocation;

  @Inject
  @Named("quarantine.location.prefix")
  private String quarantineLocationPrefix;

  @Inject(optional = true)
  @Named("quarantine.enabled")
  private boolean quarantineEnabled;

  private String currentFeatureName;

  private String currentScenarioName;

  public BobcatJsonFormatter(Appendable out) {
    super(out);
    Guice.createInjector(new QuarantineModule(), new PropertyModule()).injectMembers(this);
  }

  @Override
  public void scenario(Scenario scenario) {
    super.scenario(scenario);
    this.currentScenarioName = scenario.getName();
  }

  @Override
  public void feature(Feature feature) {
    super.feature(feature);
    this.currentFeatureName = feature.getName();
  }

  @Override
  public void result(Result result) {
    if (FAILED_STATUS.equals(result.getStatus())) {
      if (testsQuarantine.getQuarantined()
          .contains(new QuarantinedElement(currentFeatureName, currentScenarioName))) {
        result = new Result(SKIPPED_STATUS, result.getDuration(), result.getErrorMessage());
        this.write("This scenario has failed but it was quarantined");
      }
    }
    super.result(result);
  }

  @Override
  public void after(Match match, Result result) {
    super.after(match, result);
    if (isFirstStep) {
      includeQuarantineLogic();
      isFirstStep = false;
    }
  }

  private void includeQuarantineLogic() {
    try {
      includeJsScript(getScriptBody("jquery-1.8.2.min.js"));
      includeCssStylesheet(getScriptBody("quarantine.css"));
      String quarantineJs = getScriptBody("quarantine.js").
              replace("<#serviceUrl>", quarantineLocation + "/" + quarantineLocationPrefix);
      includeJsScript(quarantineJs);
    } catch (IOException ex) {
      LOG.error(ex.getMessage(), ex);
    }
  }

  private void includeJsScript(String scriptBody) throws IOException {
    this.write("<script type='text/javascript'>" + removeLineBreaks(scriptBody) + "</script>");
  }

  private void includeCssStylesheet(String scriptBody) throws IOException {
    this.write("<style>" + removeLineBreaks(scriptBody) + "</style>");
  }

  private String getScriptBody(String filePath) throws IOException {
    String result;
    try (StringWriter writer = new StringWriter()) {
      IOUtils.copy(ClassLoader.getSystemResourceAsStream(filePath), writer, "utf-8");
      result = writer.toString();
    }
    return result;
  }

  private String removeLineBreaks(String text) {
    return text.replace("\r\n", " ").replace("\n", " ");
  }
}
