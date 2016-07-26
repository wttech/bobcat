package com.cognifide.bdd.demo.aem.touchui;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.carousel.CarouselComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.data.componentconfigs.FieldConfig;
import com.cognifide.qa.bb.aem.data.components.Components;
import com.cognifide.qa.bb.aem.data.pages.Pages;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemCarouselTest {

  private static final String CONFIGURATION = "Carousel - Update&Read";

  private static final String COMPONENT_NAME = "Carousel";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private AuthorPageFactory authorPageFactory;

  @Inject
  private Pages pages;

  @Inject
  private Components components;

  private AuthorPage page;

  @Before
  public void setup() {
    aemLogin.authorLogin();
    page = authorPageFactory.create(pages.getPath(CONFIGURATION));
    page.open();
    assertThat("Page has not loaded", page.isLoaded(), is(true));
  }

  @Test
  public void editCarouselComponentTest() {
    String parsys = pages.getParsys(CONFIGURATION);
    page.addComponent(parsys, COMPONENT_NAME);
    assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_NAME.toLowerCase()));
    Map<String, List<FieldConfig>> data = page.configureComponent(parsys,
        COMPONENT_NAME, COMPONENT_NAME.toLowerCase());

    CarouselComponent component =
        (CarouselComponent) page.getContent(components.getClazz(COMPONENT_NAME));

    assertEquals(2, component.getSize());

    page.deleteComponent(parsys, COMPONENT_NAME);
  }
}
