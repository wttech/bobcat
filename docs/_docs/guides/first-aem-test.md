---
title: Writing your first AEM authoring test with Bobcat
---

This guide uses `bobcat-aem-junit` template to create tests. Some concepts are the same as in [first test guide]({{site.baseurl}}/docs/guides/first-test/) and we won't explain them here.

## Context

We will write a test that will check the following:

1. Login to AEM author instance
2. We create simple test page with few components
3. Configure text component
4. Check if component has entered values
5. Remove created page after test is finished.

We will use AEM 6.4 instance.

## Setup

We used gradle template so we have much of the job already done. We have already prepared default [runmode]({{site.baseurl}}/docs/modules/core/runmodes/) with all required modules:

```yaml
- com.cognifide.qa.bb.modules.CoreModule
- com.cognifide.qa.bb.aem.core.modules.Aem64FullModule
```
These two modules are required to run authoring tests for AEM 6.4.

## Page Objects

We will start with creating required page objects

### Create Test Page
 
Page Object that will be representation of our test page

```java
package com.bobcat.test.pages;

import org.openqa.selenium.support.ui.ExpectedConditions;

import com.cognifide.qa.bb.aem.core.pages.AemAuthorPage;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.cognifide.qa.bb.wait.BobcatWait;
import com.google.inject.Inject;

@PageObject
public class TestPage extends AemAuthorPage<TestPage> {

  @Inject
  private BobcatWait bobcatWait;

  private String title = "English";

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isDisplayed() {
    return bobcatWait.isConditionMet(ExpectedConditions.titleIs(getTitle()));
  }

  public boolean isNotAvailable() {
    return bobcatWait.isConditionMet(ExpectedConditions.titleContains("404 Resource"));
  }
}
``` 

- `AemAuthorPage` is extend version of `Page.class` it contains method to retrieving component page object from preview mode. Thanks to this we can check if configuration was correct  

### Text Component

Then we will create Page Object that represents Text Component available in AEM. We will use both `PageObjectInterface` and `PageObject`

```java
package com.bobcat.test.pageobjects;

import com.cognifide.qa.bb.qualifier.PageObjectInterface;

@PageObjectInterface
public interface TextComponent {
  String getInnerHTML();
}
```
```java
package com.bobcat.test.pageobjects;

import com.cognifide.qa.bb.constants.HtmlTags.Properties;
import com.cognifide.qa.bb.qualifier.CurrentScope;
import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;
import org.openqa.selenium.WebElement;

@PageObject(css = ".cmp-text")
public class TextComponentImpl implements TextComponent {

  @Inject
  @CurrentScope
  private WebElement component;

  public String getInnerHTML() {
    return component.getAttribute(Properties.INNER_HTML);
  }

  public String getCssClassNameProperty() {
    return component.getAttribute(Properties.CLASS_NAME);
  }

}
```

### Guice module

We should bind interface to implementation so we create simple guice module
```java
package com.bobcat.test;

import com.bobcat.test.pageobjects.TextComponent;
import com.bobcat.test.pageobjects.TextComponentImpl;
import com.google.inject.AbstractModule;

public class ComponentModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(TextComponent.class).to(TextComponentImpl.class);
  }
}
```

Then we should add new module to our runmode in `default.yaml`
```yaml
- com.cognifide.qa.bb.modules.CoreModule
- com.cognifide.qa.bb.aem.core.modules.Aem64FullModule
- com.bobcat.test.ComponentModule
```

## Test
We prepared all required page objects and we can start writing test.

### Test frame
Lets start with creating a test frame which we will fill in next steps:
```java
package com.bobcat.test;

import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.modules.BobcatRunModule;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Modules(BobcatRunModule.class)
@Epic("AEM 6.4 Base Tests")
@Feature("TextComponent Tests")
public class ConfigureComponentTest {

  @BeforeEach
  public void loginAndCreateTestPage() {  
  }

  @Test
  public void configureTextComponentTest() {
  }


  @AfterEach
  public void deleteTestPage() {    
  }
}
```
 - We have empty test method, JUnit BeforeEach and AfterEach methods
 - `@Epic` and `@Feature` are Allure annotations.

### Login to AEM

Our configuration already has neccessary parameters, but sometimes instance address or password have to be changed. In template project for developer instance they are kept in `dev.yaml`
```yaml
dev:
  author.url: http://127.0.0.1:4502
  author.ip: http://127.0.0.1:4502
  author.login: admin
  author.password: admin
  login.token.name: login-token
```

With this parameters login to AEM requires injecting `ActionsController.class` and calling one action. We do this in `BeforeEach` method.

```java
(...)
import com.cognifide.qa.bb.aem.core.api.AemActions;
import com.cognifide.qa.bb.api.actions.ActionException;
import com.cognifide.qa.bb.api.actions.ActionsController;
(...)
  @Inject
  private ActionsController controller;

  @BeforeEach
  void loginAndCreateTestPage() throws ActionException {
    controller.execute(AemActions.LOG_IN);
  }
(...)  
```
- `ActionsController` allow to execute predefined actions in Bobcat. Some of them (as login action) are already delivered with Bobcat but they can be also developed by users. 

### Create test page

We have to make sure that page we want to test exists in AEM instance. First step is prepare its definition. We create file in `test/resources` folder and we name it ``pageTest.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<jcr:root xmlns:sling="http://sling.apache.org/jcr/sling/1.0"
  xmlns:cq="http://www.day.com/jcr/cq/1.0" xmlns:jcr="http://www.jcp.org/jcr/1.0"
  xmlns:nt="http://www.jcp.org/jcr/nt/1.0"
  jcr:primaryType="cq:Page">
  <jcr:content
    cq:template="/conf/we-retail/settings/wcm/templates/content-page"
    jcr:description="Test page"
    jcr:primaryType="cq:PageContent"
    jcr:title="testPage"
    sling:resourceType="weretail/components/structure/page"
    browserTitle="Test page"
    pageTitle="Test page">
    <root
      jcr:primaryType="nt:unstructured"
      sling:resourceType="wcm/foundation/components/responsivegrid">
      <responsivegrid
        jcr:primaryType="nt:unstructured"
        sling:resourceType="wcm/foundation/components/responsivegrid">
        <text
          jcr:primaryType="nt:unstructured"
          sling:resourceType="weretail/components/content/text"/>
      </responsivegrid>
    </root>
  </jcr:content>
</jcr:root>
```
As you can see it is AEM page exported from crx in xml format. It contains one text component from We Retail. Now using our actions we have to add this page to instance. We do it also in `BeforeEach` method:
```java
(...)
import com.cognifide.qa.bb.aem.core.pages.sling.SlingDataXMLBuilder;
import com.cognifide.qa.bb.aem.core.pages.sling.SlingPageData;
(...)

private static final String TEST_PAGE_PATH = "/content/we-retail/us/en/textcomponenttestpage";

  @BeforeEach
  public void loginAndCreateTestPage() throws ActionException {
   (...)
    controller.execute(AemActions.CREATE_PAGE_VIA_SLING,
        new SlingPageData(TEST_PAGE_PATH,
            SlingDataXMLBuilder.buildFromFile("pageTest.xml")));
  }
(...)
```
- We use here action that requires some parameters. To pass them we use class that implements `ActionData` interface. In our example this class is delivered with Bobcat but in custom actions it has to be created by users
- We need to parameters. Path where in crx page will be created and what page will contain
- `SlingDataXMLBuilder.class` creates page data that Sling can understand from xml file 

### Get page instance
With all things prepared and ready we can start writing test. First lets get our page instance using and open it in our browser

```java
(...)
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.bobcat.test.pages.TestPage;
import com.cognifide.qa.bb.page.BobcatPageFactory;
(...)
@Inject
private BobcatPageFactory bobcatPageFactory;
  
private final static String PAGE_TO_CREATE_TITLE = "testPage";
(...)
  @Test
  public void configureTextComponentTest() {
    TestPage testPage = bobcatPageFactory
        .create("/editor.html" + TEST_PAGE_PATH + ".html", TestPage.class);
    testPage.setTitle(PAGE_TO_CREATE_TITLE);
    assertTrue(testPage.open().isDisplayed());
  }
(...)  
```

### Configure component
We can open page, it contains text component and now we need its configuration. It is also kept in yaml file in `test/resources`. In our case we name it `text.yaml`
```yaml
Properties:
  - type: RICHTEXT
    value: Bobcat test text
  - type: RICHTEXT_FONT_FORMAT
    value: BOLD
```
- Component configuration files contains information about how component dialogs should be field. Text component dialog has richtext which we want to have some text. We can also set that this text should be bold.   

In test we will select component on page. Then we will fill it using configuration file.

```java
(...)
import com.cognifide.qa.bb.aem.core.component.actions.ConfigureComponentData;
import com.cognifide.qa.bb.aem.core.component.configuration.ResourceFileLocation;
(...)
  @Test
  public void configureTextComponentTest() throws ActionException {
    (...)
    controller.execute(AemActions.CONFIGURE_COMPONENT,
        new ConfigureComponentData("container", "Text", 0,
            new ResourceFileLocation("text.yaml")));
  }
(...)
```
- We of course use another here, last parameter is configuration file but what are other parameters. Lets see where component is placed:
  ![Login Page]({{site.baseurl}}/assets/img/aemtutorial.png)
  Content tree is how we look for component

  First parameter is where in containers (or in which level) we can found component. If we would like to find component with text "one" path would be "container". For text "two" the path would be "container/container[1]" (we have to select nested container and second leaf).  

  Second parameter is component name in Content tree

  Third parameter is which component with this name on selected level we should select. Here we want to select first so 0. If we would like to select one with "three" than the value would be '1'.
  
  We use java array numeration of place.

### Check component
We configured component and we would like to check if everything worked.
```java
(...)
import static org.assertj.core.api.Assertions.assertThat;
import com.bobcat.test.pageobjects.TextComponent;
import com.bobcat.test.pageobjects.TextComponentImpl;
(...)
  @Test
  public void configureTextComponentTest() throws ActionException {
    (...)
    TextComponentImpl content = (TextComponentImpl) testPage.getContent(TextComponent.class, 0);
    assertThat(content.getInnerHTML().trim().replaceAll("\\r|\\n", "")).matches(".*<b>Bobcat test text.*</b>.*");
  }
(...)
```
- `getContent` method from `AemAuthorPage` will switch us to preview mode. Then it will search for `TestComponent` on our page. Because we can have many text components we have to select which one. In our example first one
- And at last we compare text from component with expected text.

### Remove page

After test is finished it should clean the instance. All changes that were made should be discarded. In our example we should delete created page and we do it in `AfterEach` method
```java
(...)
  @AfterEach
  public void deleteTestPage() throws ActionException {
    controller.execute(AemActions.DELETE_PAGE_VIA_SLING, new SlingPageData(TEST_PAGE_PATH));
  }
(...)
```
- We again use one of actions with one parameter which is path to page that we want to remove. 
### Full test
Full test class created in this tutorial

```java
package com.bobcat.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bobcat.test.pageobjects.TextComponent;
import com.bobcat.test.pageobjects.TextComponentImpl;
import com.bobcat.test.pages.TestPage;
import com.cognifide.qa.bb.aem.core.api.AemActions;
import com.cognifide.qa.bb.aem.core.component.actions.ConfigureComponentData;
import com.cognifide.qa.bb.aem.core.component.configuration.ResourceFileLocation;
import com.cognifide.qa.bb.aem.core.pages.sling.SlingDataXMLBuilder;
import com.cognifide.qa.bb.aem.core.pages.sling.SlingPageData;
import com.cognifide.qa.bb.api.actions.ActionException;
import com.cognifide.qa.bb.api.actions.ActionsController;
import com.cognifide.qa.bb.junit5.guice.Modules;
import com.cognifide.qa.bb.modules.BobcatRunModule;
import com.cognifide.qa.bb.page.BobcatPageFactory;
import com.google.inject.Inject;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Modules(BobcatRunModule.class)
@Epic("AEM 6.4 Base Tests")
@Feature("TextComponent Tests")
public class ConfigureComponentTest {

  @Inject
  private ActionsController controller;

  @Inject
  private BobcatPageFactory bobcatPageFactory;

  private final static String PAGE_TO_CREATE_TITLE = "testPage";

  private static final String TEST_PAGE_PATH = "/content/we-retail/us/en/textcomponenttestpage";

  @BeforeEach
  public void loginAndCreateTestPage() throws ActionException {
    controller.execute(AemActions.LOG_IN);
    controller.execute(AemActions.CREATE_PAGE_VIA_SLING,
        new SlingPageData(TEST_PAGE_PATH,
            SlingDataXMLBuilder.buildFromFile("pageTest.xml")));
  }

  @Test
  public void configureTextComponentTest() throws ActionException {
    TestPage testPage = bobcatPageFactory
        .create("/editor.html" + TEST_PAGE_PATH + ".html", TestPage.class);
    testPage.setTitle(PAGE_TO_CREATE_TITLE);
    assertTrue(testPage.open().isDisplayed());
    controller.execute(AemActions.CONFIGURE_COMPONENT,
        new ConfigureComponentData("container", "Text", 0,
            new ResourceFileLocation("text.yaml")));
    TextComponentImpl content = (TextComponentImpl) testPage.getContent(TextComponent.class, 0);

    assertThat(content.getInnerHTML().trim().replaceAll("\\r|\\n", ""))
        .matches(".*<b>test test test.*</b>.*");
  }


  @AfterEach
  public void deleteTestPage() throws ActionException {
    controller.execute(AemActions.DELETE_PAGE_VIA_SLING, new SlingPageData(TEST_PAGE_PATH));
  }
}
```
## Running tests
Now you're ready to roll - trigger your test class from you IDE or execute the following command from the command line!

```
gradlew clean test
```

## Summary

As you can see, the whole process of writing tests is pretty straightforward