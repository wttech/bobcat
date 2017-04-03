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
package com.cognifide.qa.bb.logging;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.cognifide.qa.bb.logging.entries.InfoEntry;
import com.cognifide.qa.bb.logging.entries.LogEntry;
import com.cognifide.qa.bb.logging.entries.SoftAssertionFailedEntry;
import com.cognifide.qa.bb.logging.entries.SubreportEndEntry;
import com.cognifide.qa.bb.logging.entries.SubreportStartEntry;
import com.cognifide.qa.bb.logging.reporter.ReportFileCreator;
import com.cognifide.qa.bb.proxy.ProxyEventListener;
import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;
import com.google.inject.Inject;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;

/**
 * This logger listens to all proxy events and stores them in a list.
 * After that, events are passed to the current report when dispatch() method is invoked.
 */
@ThreadScoped
public class ProxyLogger implements ProxyEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProxyLogger.class);

  @Inject
  private TestEventCollectorImpl eventCollector;

  @Inject
  private ReportFileCreator fileCreator;

  private ClosestHarEntryElector closestHarEntryElector;

  private final List<LogEntry> entries = new ArrayList<>();

  private boolean timeoutOccurred;

  @Override
  public void listeningStarted() {
    entries.clear();
    entries.add(new SubreportStartEntry("Traffic analysis"));
  }

  @Override
  public void listeningStopped(Har har) {
    try {
      File harFile = fileCreator.getReportFile("har");
      har.writeTo(harFile);
      entries.add(new InfoEntry("Created har file: " + harFile.getPath()));
      if (timeoutOccurred && closestHarEntryElector != null) {
        HarEntry similarEntry = closestHarEntryElector.findSimilarEntry(har.getLog().getEntries());
        logClosestHitEntry(similarEntry);
        timeoutOccurred = false;
      }
    } catch (IOException e) {
      LOG.error("Can't create har file", e);
    }
    entries.add(new SubreportEndEntry("Traffic analysis"));
  }

  private void logClosestHitEntry(HarEntry closestHit) {
    if (closestHit == null) {
      entries.add(new InfoEntry("Closest hit: n/a"));
    } else {
      entries.add(new InfoEntry("Closest hit: " + closestHit.getRequest().getUrl() + ", params: "
          + closestHit.getRequest().getQueryString().toString()));
    }
  }

  @Override
  public void waitingForRequest(RequestPredicate predicate,
      ClosestHarEntryElector closestHarEntryElector) {
    entries.add(new InfoEntry("Proxy is waiting for request " + predicate));
    this.closestHarEntryElector = closestHarEntryElector;
  }

  @Override
  public void requestFound() {
    entries.add(new InfoEntry("Proxy found request"));
  }

  @Override
  public void timeout() {
    entries.add(new SoftAssertionFailedEntry("Timeout occurred"));
    timeoutOccurred = true;
  }

  @Override
  public void dispatch() {
    entries.stream().
        forEach(e ->
            eventCollector.getCurrentTest().addLogEntry(e)
        );
    entries.clear();
  }
}
