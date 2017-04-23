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
package com.cognifide.qa.bb.aem.touch.siteadmin;

import java.time.LocalDateTime;

import com.cognifide.qa.bb.aem.touch.siteadmin.common.SiteadminChildPage;

public interface SiteadminActions {

  /**
   * Activates the page with provided title by selecting it and pressing Activate button on Action Bar.
   *
   * @param title title of the page
   * @return this SiteadminPage
   */
  SiteadminActions publishPage(String title);

  /**
   * Activates the page with provided title by selecting it and pressing Activate Later button in Activate
   * drop down.
   *
   * @param title         title of the page
   * @param localDateTime date and time in the future
   * @return this SiteadminPage
   */
  SiteadminActions publishPageLater(String title, LocalDateTime localDateTime);

  /**
   * Copies the page with provided title by selecting it and pressing Copy button on Action Bar.
   *
   * @param title       title of the page
   * @param destination copy destination
   * @return this SiteAdminPage
   */
  SiteadminActions copyPage(String title, String destination);

  /* Actions on Grid */

  /**
   * Creates a new page based on provided the values. During creation specified template is used in
   * CreatePageWindow.
   *
   * @param title        title of the created page
   * @param name         name of the created page
   * @param templateName template of the created page
   * @return this SiteadminPage
   */
  SiteadminActions createNewPage(String title, String name, String templateName);

  /**
   * Creates a new page based on the provided values. name is omitted (default one is set by AEM). During
   * creation specified template is used in CreatePageWindow
   *
   * @param title        title of the created page
   * @param templateName template of the created page
   * @return this SiteadminPage
   */
  SiteadminActions createNewPage(String title, String templateName);

  /**
   * Waits until there is requested number of subPages on actual page list. During waiting page is
   * reloaded several times.
   *
   * @param pageCount number of pages that is expected to appear
   * @return this SiteadminPage
   */
  SiteadminActions waitForPageCount(int pageCount);

  /**
   * Deactivates the page with provided title by selecting it and pressing Dectivate button on Action Bar.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  SiteadminActions unpublishPage(String title);

  /**
   * Deactivates the page with provided title by selecting it and pressing Deactivate Later button in
   * Deactivate drop down.
   *
   * @param title         title of the page
   * @param localDateTime date and time in the future
   * @return this SiteadminPage
   */
  SiteadminActions unpublishPageLater(String title, LocalDateTime localDateTime);

  /**
   * Deletes the page with provided title by selecting it and pressing Delete button on Action Bar. Verifies
   * that page is deleted.
   *
   * @param title title of the page
   * @return this SiteAdminPage
   */
  SiteadminActions deletePage(String title);

  /**
   * Deletes all sub pages if contains any
   *
   * @return this SiteAdminPage
   */
  SiteadminActions deleteSubPages();

  /**
   * Checks if a page with provided title is displayed in the SiteAdminGrid.
   *
   * @param title title of the page
   * @return true if page is present
   */
  boolean isPagePresent(String title);

  /**
   * Moves the page with provided title to provided destination path by selecting it and pressing Move button
   * on Action Bar
   *
   * @param title           page title
   * @param destinationPath destination page path
   * @return this SiteadminPage
   */
  SiteadminActions movePage(String title, String destinationPath);

  /**
   * Opens Site Admin at specified node: http://domain/siteadmin#/nodePath
   *
   * @param nodePath path to the node
   * @return Site Admin grid
   */
  SiteadminActions open(String nodePath);

  /**
   * Opens Site Admin main page: http://domain/siteadmin#.
   *
   * @return This SiteAdminPage
   */
  SiteadminActions open();

  /**
   * Checks if page contains sub pages
   *
   * @return true if contains sub pages
   */
  boolean hasChildPages();

  /**
   * Returns page
   *
   * @param title
   * @return
   */
  SiteadminChildPage getPageFromList(String title);

}
