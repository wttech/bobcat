package com.cognifide.qa.bb.aem.data.pages;

import java.util.Map;

public class Pages {

  private Map<String, PageDescription> pages;

  public Pages(Map<String, PageDescription> pages) {
    this.pages = pages;
  }

  public String getParsys(String name) {
    return pages.get(name).getParsys();
  }

  public String getPath(String name) {
    String path = pages.get(name).getPath();
    if (path == null) {
      throw new IllegalArgumentException("There is no path configured in pages.yaml for: " + name);
    }
    return path;
  }
}
