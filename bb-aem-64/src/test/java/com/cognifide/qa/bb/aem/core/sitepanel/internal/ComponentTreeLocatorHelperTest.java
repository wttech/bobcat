package com.cognifide.qa.bb.aem.core.sitepanel.internal;

import static org.junit.Assert.*;

import com.cognifide.qa.bb.aem.core.sitepanel.EditComponentActonData;
import org.junit.Test;

public class ComponentTreeLocatorHelperTest {

  @Test
  public void generateCssPath() {

  }

  private EditComponentActonData generateData(){
    return new EditComponentActonData("container[0]/container[1]","Text",2);
  }
}