package com.cognifide.qa.bb.aem.data.components;

import java.util.Map;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cognifide.qa.bb.aem.util.YamlReader;
import com.google.inject.Provider;

@ThreadScoped
public class ComponentsProvider implements Provider<Components> {

  public static final String CONFIG_PATH = "component-descriptions";

  private Components descriptions;

  @Override
  @SuppressWarnings("unchecked")
  public Components get() {
    if (descriptions == null) {
      descriptions = new Components(readConfig());
    }
    return descriptions;
  }

  private Map readConfig() {
    TypeReference<Map<String, ComponentDescription>> typeReference = new TypeReference<Map<String,
        ComponentDescription>>() {
    };
    return YamlReader.read(CONFIG_PATH, typeReference);
  }
}
