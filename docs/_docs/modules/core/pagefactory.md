---
title: "Page Factory"
---

## Overview
If we want to represent page in Bobcat we use class with `PageObject` annotation. This also requires adding url to page in different variants of `open` method. Problems start when we would like to use the same page representation (build with all other required page objects) with different urls. We have to implement some way to change url for page or duplicate the code for different pages.

`Page.class` and `BobcatPageFactory.class` are Bobcat response and suggested solution.

## Using `Page.class`

`Page.class` gives us methods to open page and also includes `WebDriver` in its implementation.

Our page object which we want to be page representation should extend this class. The only requirement is adding class name as generic to `Page.class`

```java
@PageObject
public class TestPage extends Page<TestPage> {
  //code
}
```

We don't have to add any open method or path url it is already included.

## Using `BobcatPageFactory.class`

We created class that is page representation but we still need to get instance with required url. This is where we use `BobcatPageFactory.class`

```java
//(imports...)
@Modules(BobcatRunModule.class)
public class WikipediaTest {

  @Inject
  private BobcatPageFactory bobcatPageFactory;

  @Test
  public void wikipediaSearchTest() {
    TestPage testPage = bobcatPageFactory.create("https://en.wikipedia.org", TestPage.class);
    testPage.open();
    // rest of the test code
  }
}
```
We need to inject `BobcatPageFactory.class` into test class and then use its create method. It requires url served as String and page object class we want to create. Instance is created with already injected url

## Additional info

* Class that extend `Page.class` should not override methods  `open`, `getFullUrl` and field `fullUrl`
* `bobcat.page.path` is restricted property used by page factory mechanism and should not be used in project properties
* `AemAuthorPage.class` is more advanced version of `Page.class` and is used in AEM Authoring Tests. Its description can be found [here]({{site.baseurl}}/docs/modules/aem-core/) 