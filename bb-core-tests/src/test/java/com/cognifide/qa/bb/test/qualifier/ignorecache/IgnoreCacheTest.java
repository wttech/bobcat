package com.cognifide.qa.bb.test.qualifier.ignorecache;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.test.TestModule;
import com.cognifide.qa.test.pageobjects.qualifier.ignorecache.IgnoreCachePage;
import com.cognifide.qa.test.util.PageUtils;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(TestModule.class)
public class IgnoreCacheTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private IgnoreCachePage ignoreCachePage;

  @Before
  public void setUp() {
    webDriver.get(PageUtils.buildTestPageUrl(this.getClass()));
  }

  @Test
  public void shouldRefindElementAfterTypeChange() {

    try {
      ignoreCachePage.typeIntoTextField("someInput");
      webDriver.navigate().refresh();
      ignoreCachePage.typeIntoTextField("someInput");
    } catch (Exception e) {
      assertNull(e);
    }

  }

  @Test(expected = StaleElementReferenceException.class)
  public void shouldNotRefindElementAfterTypeChange() {
    ignoreCachePage.typeIntoCachedTextField("someInput");
    webDriver.navigate().refresh();
    ignoreCachePage.typeIntoCachedTextField("someInput");

  }

}
