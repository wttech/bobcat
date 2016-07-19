package com.cognifide.qa.bb.aem.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.Resources;

public class YamlReader {
  private static final Logger LOG = LoggerFactory.getLogger(YamlReader.class);

  private static final String YAML = ".yaml";

  public static <T> T read(String path, TypeReference typeReference) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    try {
      URI uri = Resources.getResource(path + YAML).toURI();
      File file = Paths.get(uri).toFile();
      return mapper.readValue(file, typeReference);
    } catch (IOException | URISyntaxException e) {
      LOG.error("Could not read YAML file: " + path);
    }
    throw new IllegalStateException("YAML file could not be read");
  }
}
