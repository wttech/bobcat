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
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import org.openqa.selenium.Capabilities;
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
import com.google.gson.stream.JsonWriter;
import com.google.inject.Inject;

/**
 * This is a Reporter that creates reports in Json format.
 */
public class JsonReporter extends AbstractReporter {

  private static final String MESSAGE = "message";

  private static final String JSON_WRITER_EXCEPTION = "Json writer exception";

  private static final Logger LOG = LoggerFactory.getLogger(JsonReporter.class);

  @Inject
  private ReportFileCreator fileCreator;

  private JsonWriter jsonWriter;

  private void createFile() {
    try {
      PrintWriter writer =
          new PrintWriter(fileCreator.getReportFile("json", getReportStartingDate()),
              StandardCharsets.UTF_8.name());
      jsonWriter = new JsonWriter(writer);
      jsonWriter.setIndent("  ");
      jsonWriter.beginObject();
    } catch (IOException e) {
      LOG.error("Caught exception when creating file", e);
    }
  }

  @Override
  public void suiteStart() {
    try {
      jsonWriter.name("tests");
      jsonWriter.beginArray();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void suiteEnd() {
    try {
      jsonWriter.endArray();
      jsonWriter.name("total");
      long testCount = getTestCount();
      jsonWriter.value(testCount);
      jsonWriter.name("failed").value(testCount - getPassedTestCount());
      jsonWriter.endObject();
      jsonWriter.flush();
      jsonWriter.close();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void testStart(TestInfo testInfo) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("name").value(testInfo.getTestName());
      jsonWriter.name("result").value(testInfo.getResult().toString());
      jsonWriter.name("entries");
      jsonWriter.beginArray();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void testEnd(TestInfo testInfo) {
    try {
      jsonWriter.endArray();
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void errorEntry(ErrorEntry errorLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(errorLogEntry.getTime().toString());
      jsonWriter.name("type").value("error");
      jsonWriter.name(MESSAGE).value(errorLogEntry.getMessage());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void exceptionEntry(ExceptionEntry exceptionLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(exceptionLogEntry.getTime().toString());
      jsonWriter.name("type").value("exception");
      jsonWriter.name(MESSAGE).value(exceptionLogEntry.getMessage());
      jsonWriter.name("stack");
      jsonWriter.beginArray();
      for (StackTraceElement ste : exceptionLogEntry.getException().getStackTrace()) {
        jsonWriter.value(String.format("%s.%s", ste.getClassName(), ste.getMethodName()));
      }
      jsonWriter.endArray();
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void screenshotEntry(ScreenshotEntry screenshotLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(screenshotLogEntry.getTime().toString());
      jsonWriter.name("type").value("screenshot");
      jsonWriter.name("path").value(screenshotLogEntry.getFilePath());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void infoEntry(InfoEntry infoLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(infoLogEntry.getTime().toString());
      jsonWriter.name("type").value("info");
      jsonWriter.name(MESSAGE).value(infoLogEntry.getMessage());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void eventEntry(EventEntry eventLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(eventLogEntry.getTime().toString());
      jsonWriter.name("type").value("event");
      jsonWriter.name("event").value(eventLogEntry.getEvent());
      jsonWriter.name("parameter").value(eventLogEntry.getParameter());
      jsonWriter.name("duration").value(eventLogEntry.getDuration());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void subreportStart(SubreportStartEntry subreportStartLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(subreportStartLogEntry.getTime().toString());
      jsonWriter.name("type").value("subreport-start");
      jsonWriter.name("name").value(subreportStartLogEntry.getName());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void subreportEnd(SubreportEndEntry subreportEndLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(subreportEndLogEntry.getTime().toString());
      jsonWriter.name("type").value("subreport-end");
      jsonWriter.name("name").value(subreportEndLogEntry.getName());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void browserInfoEntry(BrowserInfoEntry browserInfoEntry) {
    Capabilities capabilities = browserInfoEntry.getCapabilities();
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(browserInfoEntry.getTime().toString());
      jsonWriter.name("type").value("browserInfo");

      jsonWriter.name("capabilities");
      jsonWriter.beginObject();
      jsonWriter.name("browserName").value(capabilities.getBrowserName());
      jsonWriter.name("version").value(capabilities.getVersion());
      jsonWriter.endObject();

      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void assertion(AssertionFailedEntry assertionFailedEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(assertionFailedEntry.getTime().toString());
      jsonWriter.name("type").value("assertion");
      jsonWriter.name(MESSAGE).value(assertionFailedEntry.getError().getMessage());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void softAssertion(SoftAssertionFailedEntry softAssertionFailedEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(softAssertionFailedEntry.getTime().toString());
      jsonWriter.name("type").value("soft-assertion");
      jsonWriter.name(MESSAGE).value(softAssertionFailedEntry.getMessage());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void properties(Properties properties) {
    createFile();
    try {
      jsonWriter.name("properties");
      jsonWriter.beginArray();
      Iterator<Entry<Object, Object>> entryIterator = properties.entrySet().iterator();
      while (entryIterator.hasNext()) {
        Entry<Object, Object> entry = entryIterator.next();
        jsonWriter.beginObject();
        jsonWriter.name("key").value(entry.getKey().toString());
        jsonWriter.name("value").value(entry.getValue().toString());
        jsonWriter.endObject();
      }
      jsonWriter.endArray();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }

  @Override
  public void browserLogEntry(BrowserLogEntry browserLogEntry) {
    try {
      jsonWriter.beginObject();
      jsonWriter.name("time").value(browserLogEntry.getTime().toString());
      jsonWriter.name("type").value("browser log");
      jsonWriter.name(MESSAGE)
          .value(browserLogEntry.getMessage());
      jsonWriter.endObject();
    } catch (IOException e) {
      LOG.error(JSON_WRITER_EXCEPTION, e);
    }
  }
}
