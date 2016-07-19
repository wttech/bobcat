package com.cognifide.qa.bb.aem.data.pages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PageDescription {

  @JsonProperty
  private String path;

  @JsonProperty
  private String parsys;

  public String getPath() {
    return path;
  }

  public String getParsys() {
    return parsys;
  }
}
