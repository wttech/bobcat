package com.cognifide.qa.bb.aem.data.components;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponentDescription {
  @JsonProperty
  public String datapath;

  @JsonProperty
  public Class clazz;

  @JsonProperty
  public Map<String, String> variants;

  public String getDatapath() {
    return datapath;
  }

  public Class getClazz() {
    return clazz;
  }

  public Map<String, String> getVariants() {
    return variants;
  }
}
