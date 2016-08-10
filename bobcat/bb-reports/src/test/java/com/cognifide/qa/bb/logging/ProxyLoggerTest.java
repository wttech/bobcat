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

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.cognifide.qa.bb.logging.reporter.GuiceModulesInstaller;
import com.cognifide.qa.bb.proxy.analyzer.predicate.ClosestHarEntryElector;
import com.cognifide.qa.bb.proxy.analyzer.predicate.RequestPredicate;
import com.google.inject.Guice;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;

public class ProxyLoggerTest {

  private final ClosestHarEntryElector elector = mock(ClosestHarEntryElector.class);

  private final RequestPredicate matchingPredicate = mock(RequestPredicate.class);

  private final Har har = mock(Har.class, RETURNS_DEEP_STUBS);

  private final HarEntry closestHarEntry = mock(HarEntry.class, RETURNS_DEEP_STUBS);

  @Before
  public void setUp() throws Exception {
    when(elector.findSimilarEntry(any(List.class)))
            .thenReturn(closestHarEntry);
  }

  @Test
  public void testTimeoutBehaviourWhenNoElectorInjected() throws Exception {
    // given
    GuiceModulesInstaller guiceModulesInstaller = new GuiceModulesInstaller();
    ProxyLogger proxyLogger
            = Guice.createInjector(guiceModulesInstaller).getInstance(ProxyLogger.class);

    // when
    proxyLogger.listeningStarted();
    proxyLogger.waitingForRequest(matchingPredicate, elector);
    proxyLogger.timeout();
    proxyLogger.listeningStopped(har);

    // then
    verifyNoMoreInteractions(matchingPredicate);
  }

  @Test
  public void testTimeoutBehaviourWhenElectorInjected() throws Exception {
    // given
    GuiceModulesInstaller guiceModulesInstaller = new GuiceModulesInstaller();
    ProxyLogger proxyLogger = Guice.createInjector(guiceModulesInstaller)
            .getInstance(ProxyLogger.class);

    // when
    proxyLogger.listeningStarted();
    proxyLogger.waitingForRequest(matchingPredicate, elector);
    proxyLogger.timeout();
    proxyLogger.listeningStopped(har);

    // then
    verify(elector).findSimilarEntry(any(List.class));
  }
}
