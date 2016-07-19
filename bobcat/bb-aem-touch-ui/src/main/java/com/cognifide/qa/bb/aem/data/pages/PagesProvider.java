package com.cognifide.qa.bb.aem.data.pages;

import java.util.Map;

import com.cognifide.qa.bb.guice.ThreadScoped;
import com.fasterxml.jackson.core.type.TypeReference;
import com.cognifide.qa.bb.aem.util.YamlReader;
import com.google.inject.Provider;

@ThreadScoped
public class PagesProvider implements Provider<Pages> {

  public static final String CONFIG_PATH = "pages";

  private Pages pages;

  @Override
  @SuppressWarnings("unchecked")
  public Pages get() {
    if (pages == null) {
      pages = new Pages(readConfig());
    }
    return pages;
  }

  private Map readConfig() {
    TypeReference<Map<String, PageDescription>> typeReference = new TypeReference<Map<String,
        PageDescription>>() {
    };
    return YamlReader.read(CONFIG_PATH, typeReference);
  }
}
