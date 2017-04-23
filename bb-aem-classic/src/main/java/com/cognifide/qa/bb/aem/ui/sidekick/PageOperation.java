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
package com.cognifide.qa.bb.aem.ui.sidekick;

/**
 * This enum encapsulates list of all page operations that are available in the Sidekick.
 */
public enum PageOperation {
  PAGE_PROPERTIES(SidekickTab.PAGE, SidekickSection.NONE, "Page Properties..."),
  CREATE_CHILD_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Create Child Page"),
  COPY_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Copy Page"),
  MOVE_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Move Page"),
  DELETE_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Delete Page"),
  ACTIVATE_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Activate Page"),
  DEACTIVATE_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Deactivate Page"),
  LOCK_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Lock Page"),
  SHOW_REFERENCES(SidekickTab.PAGE, SidekickSection.NONE, "Show References..."),
  ROLLOUT_PAGE(SidekickTab.PAGE, SidekickSection.NONE, "Rollout Page"),
  AUDIT_LOG(SidekickTab.INFORMATION, SidekickSection.NONE, "Audit Log..."),
  PERMISSIONS(SidekickTab.INFORMATION, SidekickSection.NONE, "Permissions..."),
  PROMOTE_LAUNCH(SidekickTab.PAGE, SidekickSection.NONE, "Promote Launch..."),
  CREATE_VERSION(SidekickTab.VERSIONING, SidekickSection.CREATE_VERSION, "Create Version"),
  MORE(SidekickTab.VERSIONING, SidekickSection.CREATE_VERSION, "More »"),
  LESS(SidekickTab.VERSIONING, SidekickSection.CREATE_VERSION, "Less «"),
  RESTORE(SidekickTab.VERSIONING, SidekickSection.RESTORE_VERSION, "Restore"),
  DIFF(SidekickTab.VERSIONING, SidekickSection.RESTORE_VERSION, "Diff"),
  SWITCH_TO_LAUNCH(SidekickTab.VERSIONING, SidekickSection.LAUNCHES, "Switch"),
  GO(SidekickTab.VERSIONING, SidekickSection.TIMEWARP, "Go"),
  SHOW_TIMELINE(SidekickTab.VERSIONING, SidekickSection.TIMEWARP, "Show Timeline"),
  START_WORKFLOW(SidekickTab.WORKFLOW, SidekickSection.WORKFLOW, "Start Workflow"),
  ADD_TO_PACKAGE(SidekickTab.WORKFLOW, SidekickSection.WORKFLOW, "Add to package#Workflow Package"),
  TRANSLATE(SidekickTab.WORKFLOW, SidekickSection.TRANSLATION, "Translate");

  private final String operationName;

  private final SidekickTab tab;

  private final SidekickSection section;

  PageOperation(final SidekickTab tab, final SidekickSection section,
      final String operationName) {
    this.tab = tab;
    this.section = section;
    this.operationName = operationName;
  }

  /**
   * @return Name of the operation.
   */
  public String getOptionName() {
    return operationName;
  }

  /**
   * @return Tab name where the operation is stored.
   */
  public SidekickTab getTab() {
    return tab;
  }

  /**
   * @return Section name where the operation is stored.
   */
  public SidekickSection getSection() {
    return section;
  }
}
