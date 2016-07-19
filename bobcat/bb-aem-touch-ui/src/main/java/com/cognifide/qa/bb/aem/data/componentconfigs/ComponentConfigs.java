package com.cognifide.qa.bb.aem.data.componentconfigs;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.cognifide.qa.bb.aem.util.YamlReader;

public class ComponentConfigs {

  private static final String CFG_PATH = "component-configs/";

  public Map<String, Map<String, List<FieldConfig>>> getConfigs(String component) {
    TypeReference typeReference = new TypeReference<Map<String, Map<String, List<FieldConfig>>>>() {
    };
    String path = CFG_PATH + component.toLowerCase().replace(" ", "-");
    return YamlReader.read(path, typeReference);
  }
}
