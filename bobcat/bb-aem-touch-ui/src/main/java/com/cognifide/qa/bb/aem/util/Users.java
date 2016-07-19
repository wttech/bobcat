package com.cognifide.qa.bb.aem.util;

import java.util.Properties;

import com.google.inject.Inject;

public class Users {

  private static final String LOGIN = ".login";

  private static final String PASSWORD = ".password";

  @Inject
  private Properties properties;

  public String getUserLogin(String user) {
    return properties.getProperty(user + LOGIN);
  }

  public String getUserPassword(String user) {
    return properties.getProperty(user + PASSWORD);
  }
}
