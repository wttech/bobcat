---
title: Writing your first analytics test with Bobcat
---

This guide uses `bobcat-aem-junit` template to create tests. Some concepts are the same as in the [first test guide]({{site.baseurl}}/docs/guides/first-test/) and we won't explain them here.

## Context

We will write a simple Google Analytics test that will verify the datalayer implementation for the page load event.

1. Open the Cognifide homepage.
2. Check that the expected analytics datalayer is present.


## Setup

Please follow this link to find the analytics module setup in the [Getting started]({{site.baseurl}}/docs/_docs/modules/analytics/) section.


## Defining expected datalayers

Our analytics test will compare the existing datalayer with the one defined by us. 
Expected datalayers should be defined in json files located in the test resources folder under the \analytics\datalayers\ path.

For our test we will create a new json file with the following content:

```json
[
  {
    "pageName": "Home",
    "pageURL": "https://www.cognifide.com/"
  }
]
```
The file will be named: "home_pageload" - we will use this name in our test.


## Test
While creating your tests, you will probably need to create some page objects (more information about the page objects you can find in the [first test guide]({{site.baseurl}}/docs/guides/first-test/)), however those are not crucial for the analytics tests, so in this example we will stick to the simplest approach to open the Cognifide homepage.

In our test, to check the datalayer we need to inject the GoogleAnalytics class (in case of the Adobe Analytics, you should inject the AdobeAnalytics class).
Within the test one simple step will check the existing datalayer. As a parameter we will put the file name of our expected dataleyer:

```java
    analytics.compare("home_pageload");
```

### Full test
Full test class created in this tutorial
```java

package com.bobcat.test;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import com.cognifide.qa.bb.junit5.guice.Modules;
import com.google.inject.Inject;

@Modules(BobcatRunModule.class)
public class GoogleAnalyticsTest {

  @Inject
  private WebDriver webDriver;

  @Inject
  private GoogleAnalytics analytics;

  @Test
  public void shouldSetDataLayerForHomepageLoad() {
    webDriver.get("https://www.cognifide.com/");
    analytics.compare("home_pageload");
  }
}
```

## Running tests
Now we can trigger our test class from the IDE or execute the following command from the command line:

```
gradlew clean test
```

## Summary

As you can see, creating the analytics tests is very simple - you just need to add one step to check the datalayer. 
The main effort 