package com.cognifide.qa.bb.aem.util;

import org.apache.commons.lang3.StringUtils;

public class DataPathUtil {

  public static final String JCR_CONTENT = "/jcr:content";

  public static String normalize(String dataPath) {
    return Character.isDigit(dataPath.charAt(dataPath.length() - 1)) ?
        StringUtils.substringBeforeLast(dataPath, "_") :
        dataPath;
  }
}
