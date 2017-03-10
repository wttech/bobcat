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
package com.cognifide.qa.bb.aem.touch.util.context;

import com.cognifide.qa.bb.aem.touch.data.componentconfigs.ComponentConfiguration;
import javax.inject.Inject;

import com.cognifide.qa.bb.scenario.ScenarioContext;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.touch.pageobjects.pages.PublishPage;

/**
 * Class represents context of test.
 */
public class Context {

  @Inject
  private ScenarioContext scenarioContext;

  /**
   * @return instance of current page.
   */
  public AuthorPage getCurrentPage() {
    return scenarioContext.get(ContextKeys.CURRENT_PAGE);
  }

  /**
   * Replaces current page with given page object.
   *
   * @param page page object.
   */
  public void updateCurrentPage(Object page) {
    scenarioContext.add(ContextKeys.CURRENT_PAGE, page);
  }

  //TODO remove after refactoring Pages
  public PublishPage getCurrentPublishPage() {
    return scenarioContext.get(ContextKeys.CURRENT_PAGE);
  }

  /**
   * @return current data path.
   */
  public String getDataPath() {
    return scenarioContext.getString(ContextKeys.CURRENT_DATAPATH);
  }

  /**
   * Replaces current data path with given dataPath parameter.
   *
   * @param dataPath data path that will replace an old value.
   */
  public void updateDataPath(String dataPath) {
    scenarioContext.add(ContextKeys.CURRENT_DATAPATH, dataPath);
  }

  /**
   * @return contexts current parsys.
   */
  public String getCurrentParsys() {
    return scenarioContext.getString(ContextKeys.CURRENT_PARSYS);
  }

  /**
   * Replaces current parsys with one given in parameter.
   *
   * @param parsys parsys path.
   */
  public void updateCurrentParsys(String parsys) {
    scenarioContext.add(ContextKeys.CURRENT_PARSYS, parsys);
  }

  /**
   * Replaces current config map with one given in parameter.
   *
   * @param data configuration map.
   */
  public void updateCurrentConfig(ComponentConfiguration data) {
    scenarioContext.add(ContextKeys.CURRENT_CONFIG, data);
  }

  /**
   * @return contexts current configuration map.
   */
  @SuppressWarnings("unchecked")
  public ComponentConfiguration getCurrentConfig() {
    return scenarioContext.get(ContextKeys.CURRENT_CONFIG);
  }

  /**
   * Replaces current tag with one given in parameter.
   *
   * @param tagPath path to tag.
   */
  public void updateCurrentTag(String tagPath) {
    scenarioContext.add(ContextKeys.CURRENT_TAG, tagPath);
  }

  /**
   * @return path to contexts current tag.
   */
  public String getCurrentTag() {
    return scenarioContext.getString(ContextKeys.CURRENT_TAG);
  }

  /**
   * Sets article tile description.
   *
   * @param description article tile description.
   */
  public void setArticleTileDescription(String description) {
    scenarioContext.add(ContextKeys.ARTICLE_TILE_DESCRIPTION, description);
  }

  /**
   * @return current article tile description.
   */
  public String getArticleTileDescription() {
    return scenarioContext.getString(ContextKeys.ARTICLE_TILE_DESCRIPTION);
  }

  /**
   * Sets ugc title.
   *
   * @param ugcTitle ugc title.
   */
  public void setUgcTitle(String ugcTitle) {
    scenarioContext.add(ContextKeys.UGC_TITLE, ugcTitle);
  }

  /**
   * @return current ugc title.
   */
  public String getUgcTitle() {
    return scenarioContext.getString(ContextKeys.UGC_TITLE);
  }

  /**
   * Sets current path.
   *
   * @param currentPath current path.
   */
  public void setCurrentPath(String currentPath) {
    scenarioContext.add(ContextKeys.CURRENT_PATH, currentPath);
  }

  /**
   * @return current path.
   */
  public String getCurrentPath() {
    return scenarioContext.getString(ContextKeys.CURRENT_PATH);
  }
}
