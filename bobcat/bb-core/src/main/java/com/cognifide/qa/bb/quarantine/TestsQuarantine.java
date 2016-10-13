/*
 * Copyright 2016 Cognifide Ltd..
 *
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
 */
package com.cognifide.qa.bb.quarantine;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

@Singleton
public class TestsQuarantine {

  private static final Logger LOG = LoggerFactory.getLogger(TestsQuarantine.class);

  private static String quarantineUrl;

  private final QuarantineManager remoteQuarantineManager;

  @Inject
  public TestsQuarantine(@Named("quarantine.location.prefix") String quarantineId,
    @Named("quarantine.location") String quarantineLocation, QuarantineManager quarantineManager) {
    this.remoteQuarantineManager = quarantineManager;
    if (StringUtils.isBlank(quarantineLocation) || !quarantineLocation.matches("^(http|https)://.*$")) {
      throw new RuntimeException("Invalid URL for quarantine location !");
    }
    quarantineUrl = quarantineLocation + "/" + quarantineId;
  }

  public List<QuarantinedElement> getQuarantined() {
    List<QuarantinedElement> result = new ArrayList<>();
    try {
      result = remoteQuarantineManager.getQuarantinedElements(quarantineUrl);
    } catch (TestQuarantineEception ex) {
      LOG.warn(ex.getMessage(), ex);
    }
    return result;
  }

  public void addTestToQuarantine(QuarantinedElement element) throws TestQuarantineEception {
    remoteQuarantineManager.putElementToQuarantine(quarantineUrl, element);
  }

  public void removeTestFromQuarantine(QuarantinedElement element) throws
    TestQuarantineEception {
    remoteQuarantineManager.putElementToQuarantine(quarantineUrl, element);
  }
}
