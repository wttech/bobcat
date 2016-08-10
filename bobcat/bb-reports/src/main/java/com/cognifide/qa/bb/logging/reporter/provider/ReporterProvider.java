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
package com.cognifide.qa.bb.logging.reporter.provider;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.junit.concurrent.ReportingHandler;
import com.cognifide.qa.bb.logging.constants.ReportsConfigKeys;
import com.cognifide.qa.bb.logging.reporter.HtmlReporter;
import com.cognifide.qa.bb.logging.reporter.JsonReporter;
import com.cognifide.qa.bb.logging.reporter.SimpleReporter;
import com.cognifide.qa.bb.logging.reporter.StdoutReporter;
import com.cognifide.qa.bb.reporter.Reporter;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;

/**
 * This is a provider of Reporter instances.
 * It creates and configures the instances as indicated in the property files.
 */
public class ReporterProvider implements Provider<Set<Reporter>> {

  private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReporterProvider.class);

  private static final String REPORTERS_SEPARATOR = ",";

  /**
   * Contains available reporters for {@link ReportingHandler}
   */
  private static final Map<String, Class<? extends Reporter>> REPORTER_MAP = ImmutableMap
      .<String, Class<? extends Reporter>>builder()
      .put("html", HtmlReporter.class)
      .put("simple", SimpleReporter.class)
      .put("json", JsonReporter.class)
      .put("stdout", StdoutReporter.class)
      .build();

  @Inject
  private Properties properties;

  @Inject
  private Injector injector;

  /**
   * Provider method.
   * Creates and returns instances of Reporters that are indicated in
   * {@link ReportsConfigKeys#BOBCAT_REPORT_REPORTERS} property.
   */
  @SuppressWarnings("unchecked")
  @Override
  public Set<Reporter> get() {
    Set<Reporter> reporters = new LinkedHashSet<>();
    String prop = properties.getProperty(ReportsConfigKeys.BOBCAT_REPORT_REPORTERS, "");
    if (prop.isEmpty()) {
      return reporters;
    }

    for (String r : prop.split(REPORTERS_SEPARATOR)) {
      Class<? extends Reporter> clazz = REPORTER_MAP.get(r);
      if (clazz == null) {
        try {
          clazz = (Class<? extends Reporter>) Class.forName(r);
        } catch (ClassNotFoundException e) {
          LOG.error("Can't find reporter class", e);
          continue;
        }
      }
      reporters.add(injector.getInstance(clazz));
    }
    return reporters;
  }
}
