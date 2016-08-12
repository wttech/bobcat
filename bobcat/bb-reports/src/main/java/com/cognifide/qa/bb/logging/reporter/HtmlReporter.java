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
package com.cognifide.qa.bb.logging.reporter;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.logging.TestInfo;
import com.cognifide.qa.bb.logging.entries.AssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.BrowserInfoEntry;
import com.cognifide.qa.bb.logging.entries.BrowserLogEntry;
import com.cognifide.qa.bb.logging.entries.ErrorEntry;
import com.cognifide.qa.bb.logging.entries.EventEntry;
import com.cognifide.qa.bb.logging.entries.ExceptionEntry;
import com.cognifide.qa.bb.logging.entries.InfoEntry;
import com.cognifide.qa.bb.logging.entries.ScreenshotEntry;
import com.cognifide.qa.bb.logging.entries.SoftAssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.SubreportEndEntry;
import com.cognifide.qa.bb.logging.entries.SubreportStartEntry;
import com.google.inject.Inject;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * This is a Reporter that creates reports in HTML format. Don't create it manually. Use Guice.
 */
public class HtmlReporter extends AbstractReporter {

  private static final String TEMPLATE_FILE = "template.ftl";

  private static final Logger LOG = LoggerFactory.getLogger(HtmlReporter.class);

  private Configuration cfg;

  private Template temp;

  @Inject
  private ReportFileCreator fileCreator;

  /**
   * Overrides default implementation in Reporter, because generation of HTML report is much
   * different than other reports. Our implementation is based on Freemarker. The process is
   * following:
   * <ul>
   * <li>create configuration (currently default settings taken from Freemarker page),
   * <li>attach the template file to the configuration and parse it; location of the file is
   * indicated by TEMPLATE_FILE constant in this class,
   * <li>merge of the data and the template, creation of the report file.
   * </ul>
   */
  @Override
  public void generateReport() {
    this.cfg = new Configuration();
    try {
      configureFreeMarker();
      getTemplate();
      merge();
    } catch (IOException e) {
      LOG.error("Exception when setting template directory", e);
    }
  }

  private void configureFreeMarker() throws IOException {
    cfg.setClassForTemplateLoading(getClass(), "/");
    cfg.setObjectWrapper(new DefaultObjectWrapper());
    cfg.setDefaultEncoding(StandardCharsets.UTF_8.name());
    cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
  }

  private void getTemplate() {
    try {
      temp = cfg.getTemplate(TEMPLATE_FILE);
    } catch (IOException e) {
      LOG.error("Exception when reading template", e);
    }
  }

  private void merge() {
      try (PrintWriter writer = new PrintWriter(fileCreator.getReportFile("html", getReportStartingDate()),
              StandardCharsets.UTF_8.name())) {
        SimpleHash root = new SimpleHash();
        root.put("collector", testEventCollector);
        root.put("date", new SimpleDateFormat("yyyy-MM-dd HH.mm.ss").format(new Date()));
        root.put("total", getTestCount());
        root.put("passed", getPassedTestCount());
        root.put("failed", getFailedCount());
        root.put("passedPercent", getPassedTestPercent());
        root.put("failedPercent", getFailedPercent());
        temp.process(root, writer);
        writer.flush();
      } catch (TemplateException | IOException e) {
        LOG.error("Exception when merging model with template", e);
      }
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void suiteStart() {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void suiteEnd() {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void testStart(TestInfo testInfo) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void testEnd(TestInfo testInfo) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void infoEntry(InfoEntry infoEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void eventEntry(EventEntry eventLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void properties(Properties properties) {
    throw new UnsupportedOperationException();
  }

  /**
   * Unsupported, because HtmlReporter doesn't participate in Visitor implementation. You should
   * call "generateReport".
   */
  @Override
  public void browserLogEntry(BrowserLogEntry browserLogEntry) {
    throw new UnsupportedOperationException();
  }
}
