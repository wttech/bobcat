package com.cognifide.bdd.demo.aem.touchui;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;

import com.cognifide.bdd.demo.GuiceModule;
import com.cognifide.bdd.demo.po.touchui.CreditCardComponent;
import com.cognifide.qa.bb.aem.AemLogin;
import com.cognifide.qa.bb.aem.data.components.Components;
import com.cognifide.qa.bb.aem.data.pages.Pages;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPage;
import com.cognifide.qa.bb.aem.pageobjects.pages.AuthorPageFactory;
import com.cognifide.qa.bb.constants.Timeouts;
import com.cognifide.qa.bb.expectedconditions.CommonExpectedConditions;
import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.cognifide.qa.bb.provider.selenium.BobcatWait;
import com.google.inject.Inject;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class AemCreditCardTest {

  private static final String CONFIGURATION = "CreditCard - Update&Read";

  private static final String COMPONENT_NAME = "Credit Card Details";

  private static final String COMPONENT_DATA_PATH = "creditcard";

  private static final String LABEL_TEXT = "Credit Card";

  private static final String CHECKED_CHECKBOX_CONFIGURATION =
      "Credit Card Details - Checked CheckBox";

  private static final String UNCHECKED_CHECKBOX_CONFIGURATION =
      "Credit Card Details - Unchecked CheckBox";

  @Inject
  private AemLogin aemLogin;

  @Inject
  private AuthorPageFactory authorPageFactory;

  @Inject
  private Pages pages;

  @Inject
  private Components components;

  @Inject
  private BobcatWait bobcatWait;

  private AuthorPage page;

  private String parsys;

  @Before
  public void setup() {
    aemLogin.authorLogin();
    page = authorPageFactory.create(pages.getPath(CONFIGURATION));
    page.open();
    assertTrue("Page has not loaded", page.isLoaded());
    parsys = pages.getParsys(CONFIGURATION);
    page.addComponent(parsys, COMPONENT_NAME);
    assertTrue(page.getParsys(parsys).isComponentPresent(COMPONENT_DATA_PATH));
  }

  @Test
  public void checkedCheckBoxTest() {
    page.configureComponent(parsys, COMPONENT_DATA_PATH, CHECKED_CHECKBOX_CONFIGURATION);

    CreditCardComponent component =
        (CreditCardComponent) page.getContent(components.getClazz(COMPONENT_DATA_PATH));

    assertThat(component.getLabelText(), is(LABEL_TEXT));

    bobcatWait.withTimeout(Timeouts.MINIMAL).until(
        CommonExpectedConditions
            .elementNotPresent(By.id(CreditCardComponent.CARD_TYPE_SELECT_ID)));
  }

  @Test
  public void uncheckedCheckBoxTest() {
    page.configureComponent(parsys, COMPONENT_DATA_PATH, UNCHECKED_CHECKBOX_CONFIGURATION);

    CreditCardComponent component =
        (CreditCardComponent) page.getContent(components.getClazz(COMPONENT_DATA_PATH));

    assertThat(component.getLabelText(), is(LABEL_TEXT));
    assertThat(component.getCardTypeSelect(), anything());
  }

  @After
  public void deleteComponent() {
    page.deleteComponent(parsys, COMPONENT_DATA_PATH);
  }
}
