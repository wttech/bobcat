/*-
 * #%L
 * Bobcat Parent
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
package com.cognifide.qa.bb.aem.util.context;

import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import com.cognifide.qa.bb.aem.dialog.configurer.ScenarioContext;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.pageobjects.pages.PublishPage;

public class Context {

  @Inject
  private ScenarioContext scenarioContext;

  public AuthorPage getCurrentPage() {
    return scenarioContext.get(ContextKeys.CURRENT_PAGE, AuthorPage.class);
  }

  public void updateCurrentPage(Object page) {
    scenarioContext.add(ContextKeys.CURRENT_PAGE, page);
  }

  //TODO remove after refactoring Pages
  public PublishPage getCurrentPublishPage() {
    return scenarioContext.get(ContextKeys.CURRENT_PAGE, PublishPage.class);
  }

  public String getDataPath() {
    return scenarioContext.getString(ContextKeys.CURRENT_DATAPATH);
  }

  public void updateDataPath(String dataPath) {
    scenarioContext.add(ContextKeys.CURRENT_DATAPATH, dataPath);
  }

  public String getCurrentParsys() {
    return scenarioContext.getString(ContextKeys.CURRENT_PARSYS);
  }

  public void updateCurrentParsys(String parsys) {
    scenarioContext.add(ContextKeys.CURRENT_PARSYS, parsys);
  }

  public void updateCurrentConfig(Map<String, List<FieldConfig>> data) {
    scenarioContext.add(ContextKeys.CURRENT_CONFIG, data);
  }

  @SuppressWarnings("unchecked")
  public Map<String, List<FieldConfig>> getCurrentConfig() {
    return scenarioContext.get(ContextKeys.CURRENT_CONFIG, Map.class);
  }

  public void updateCurrentTag(String tagPath) {
    scenarioContext.add(ContextKeys.CURRENT_TAG, tagPath);
  }

  public String getCurrentTag() {
    return scenarioContext.getString(ContextKeys.CURRENT_TAG);
  }

  public void setArticleTileDescription(String description) {
    scenarioContext.add(ContextKeys.ARTICLE_TILE_DESCRIPTION, description);
  }

  public String getArticleTileDescription() {
    return scenarioContext.getString(ContextKeys.ARTICLE_TILE_DESCRIPTION);
  }

  public void setUgcTitle(String ugcTitle) {
    scenarioContext.add(ContextKeys.UGC_TITLE, ugcTitle);
  }

  public String getUgcTitle() {
    return scenarioContext.getString(ContextKeys.UGC_TITLE);
  }

  public void setCurrentPath(String currentPath) {
    scenarioContext.add(ContextKeys.CURRENT_PATH, currentPath);
  }

  public String getCurrentPath() {
    return scenarioContext.getString(ContextKeys.CURRENT_PATH);
  }
}
