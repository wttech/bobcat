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
package com.cognifide.qa.bb.aem.ui.wcm.constants;

/**
 * Enum containing possible statuses of a page displayed in the Site Admin, 'Status' column
 */

public enum PageStatus {

  SCHEDULED_ACTIVATION("status-scheduledtask-activation"),
  SCHEDULED_DEACTIVATION("status-scheduledtask-deactivation");

  private final String statusCss;

  PageStatus(String statusCss) {
    this.statusCss = statusCss;
  }

  /**
   * @return CSS class of page status
   */
  public String getStatusCss() {
    return statusCss;
  }

  /**
   * Returns PageStatus based on the provided CSS value
   *
   * @param statusCssValue CSS class of status icon
   * @return PageStatus enum that matches the CSS class
   * @throws java.lang.IllegalArgumentException when specified CSS class could not be matched
   */
  public static PageStatus getStatus(String statusCssValue) {
    for (PageStatus status : PageStatus.values()) {
      if (statusCssValue.contains(status.getStatusCss())) {
        return status;
      }
    }
    throw new IllegalArgumentException("There is no valid state specified in provided CSS value");
  }
}
