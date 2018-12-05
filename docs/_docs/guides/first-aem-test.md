---
title: Writing your first AEM authorin test with Bobcat
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

We used gradle template so we have much of the job already done. We have already prepared default [runmode]({{site.baseurl}}docs/modules/core/runmodes/) with all required modules:

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
  public void loginAndCreateTestAPge() {  
  }

  @Test
  public void configureTextComponentTest() {
  }


  @AfterEach
  public void deleteTestPage() {    
  }
}
```
 - We have empty test method, JUnit Before and After methods
 - `@Epic` and `@Feature` are Allure annotations.

### Login to AEM

### Create test page

### Configure component

### Check component

### Remove page

### Full test

## Running tests
Now you're ready to roll - trigger your test class from you IDE or execute the following command from the command line!

```
gradlew clean test
```

Note: due to the fact that Bobcat needs a Guice context constructed, running scenarios via in-built Cucumber plugins (e.g. in IntelliJ IDEA) will fail. You need to use the JUnit runner.
{: .notice--warning}

## Summary

As you can see, the whole process of writing tests is pretty straightforward