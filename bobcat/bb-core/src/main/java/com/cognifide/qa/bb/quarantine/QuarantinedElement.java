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

import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class QuarantinedElement {

  private final String featureName;

  private final String scenarioName;

  private final Date dueDate;

  private final String quarantinedBy;

  private final String description;

  public QuarantinedElement(String featureName, String scenarioName, Date dueDate, String quarantinedBy,
    String description) {
    this.featureName = featureName;
    this.scenarioName = scenarioName;
    this.dueDate = dueDate;
    this.quarantinedBy = quarantinedBy;
    this.description = description;
  }

  public QuarantinedElement(String featureName, String scenarioName) {
    this.featureName = featureName;
    this.scenarioName = scenarioName;
    this.dueDate = null;
    this.quarantinedBy = StringUtils.EMPTY;
    this.description = StringUtils.EMPTY;
  }

  public String getFeatureName() {
    return featureName;
  }

  public String getScenarioName() {
    return scenarioName;
  }

  public Date getDueDate() {
    return dueDate;
  }

  public String getQuarantinedBy() {
    return quarantinedBy;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public int hashCode() {
    int hash = 5;
    hash = 97 * hash + Objects.hashCode(this.featureName);
    hash = 97 * hash + Objects.hashCode(this.scenarioName);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final QuarantinedElement other = (QuarantinedElement) obj;
    if (!Objects.equals(this.featureName, other.featureName)) {
      return false;
    }
    return Objects.equals(this.scenarioName, other.scenarioName);
  }


}
