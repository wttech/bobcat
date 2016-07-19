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
