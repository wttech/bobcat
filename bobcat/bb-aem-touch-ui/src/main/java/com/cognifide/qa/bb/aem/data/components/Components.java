package com.cognifide.qa.bb.aem.data.components;

import java.util.Map;

public class Components {

  private Map<String, ComponentDescription> descriptions;

  public Components(Map<String, ComponentDescription> descriptions) {
    this.descriptions = descriptions;
  }

  public String getDataPath(String componentName) {
    return descriptions.get(componentName).getDatapath();
  }

  public Class getClazz(String componentName) {
    return descriptions.get(componentName).getClazz();
  }

  public String getVariantValue(String componentName, String variantName) {
    return descriptions.get(componentName).getVariants().get(variantName);
  }
}
