---
title: Writing your first test with Bobcat
---

This guide uses `bobcat-junit5` template to create tests.

## Context

We will write a simple test that will check the following:

1. Open the Wikipedia homepage.
2. Search for 'hello world' in the search box.
3. Open the first article
4. Check that it's the '"Hello, World!" program' article.

## Our first Page Objects

Always before jumping straight away into coding, it's a good habit to stop and analyze what we are going to actually need to automate on our website. When it comes to modeling the necessary parts of the page (or multiple ones) as page objects, it's a good idea to draw the relationship first. An important note here: **page object doesn't have to mean an actual page**, it can also refer to a page section, single component etc.

To automate our test case we need to tackle the following elements on their related pages:

```
+ Wikipedia homepage
|--+ search box
   |--+ input field

+ Wikipedia article
|--+ article heading
```

As you can see, we can organize the required elements in a tree structure.

It's time to convert the above into code!

You might be familiar with Page Factory and `@FindBy` annotations from vanilla Selenium - Bobcat uses them too.
{: .notice--info}

#### Wikipedia homepage

```java
package com.hello.world.wikipedia;

import com.cognifide.qa.bb.page.Page;
import com.cognifide.qa.bb.qualifier.PageObject;
import org.openqa.selenium.support.FindBy;

@PageObject
public class Homepage extends Page<Homepage>{

  @FindBy(id = "p-search")
  private SearchComponent searchComponent;

  public SearchComponent getSearchComponent() {
    return searchComponent;
  }
}
```

Few elements probably require a bit of explanation:

- `@PageObject` annotation on the whole class informs Bobcat that this is a... page object :)! Thanks to this, Bobcat will create a proper page object structure and handle Page Factory initialization across the whole tree. **Always remember about putting `@PageObject` annotation on your page objects!**
- `Page.class` this class allows to use factory to create page with url required when we run test. It also gives us injected Webdriver. Guice, the dependency injection framework used in Bobcat, will create and provide a proper instance of WebDriver based on your configuration options. If you're not interested in how it's being done, you can simply stick with this information. If you're more curious, you can check `WebDriverProvider` in Bobcat source :).
- 
```java
  @FindBy(id = "p-search")
  private SearchComponent searchComponent;
```
If you have worked with `@FindBy` earlier, you might noticed it's not annotating a Selenium's `WebElement` but a different class. It's one of the more powerful Bobcat features - **the ability to nest page objects in other ones**, i.e. you can put one page object inside another, using the `@FindBy` annotation! Oh, it works with Lists as well.

#### Search component

```java
package com.hello.world.wikipedia;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class SearchComponent {

  @FindBy(css = "input[type=search]")
  private WebElement searchField;

  public void searchForQuery(String query) {
    searchField.sendKeys(query);
    searchField.submit();
  }
}
```

This is a more regular Selenium example. One important thing to note here: **thanks to the fact that the `SearchComponent` has been embedded inside `Homepage` with a `@FindBy` annotation, all lookups done inside it will be done in the contexts of the locator from the `@FindBy`!**

What does it mean?

In our case, refering to the `searchField` WebElement will trigger a search for element located by the `#p-search input[type=search]` selector. As you can see, thanks to this, your selectors will be way more maintainable across more complicated page object trees.

#### Wikipedia article page

```java
package com.hello.world.wikipedia;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;

@PageObject
public class ArticlePage {

  @FindBy(id = "firstHeading")
  private WebElement heading;

  public String getHeading() {
    return heading.getText();
  }
}
```

Nothing special in this page object :).

We have all the necessary pieces required to write the actual test!

*Note:* if you would like to read more about working with Page Objects in Bobcat, check out [this page]({{site.baseurl}}/docs/modules/core/pageobject/).
{: .notice--info}
--------------

## JUnit

When writing tests in JUnit5 Bobcat already creates Guice context what we must do is to add required modules and we do it with `BobcatRunModule.class`

Using it looks like this:
```java
package com.hello.world.wikipedia;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.cognifide.qa.bb.page.BobcatPageFactory;
import com.google.inject.Inject;
import org.junit.jupiter.api.Test;
import com.cognifide.qa.bb.junit5.guice.Modules;

import com.cognifide.qa.bb.modules.BobcatRunModule;

@Modules(BobcatRunModule.class)
public class WikipediaTest {

  private static final String SEARCH_QUERY = "hello world";
  private static final String HEADING = "\"Hello, World!\" program";

  @Inject
  private BobcatPageFactory bobcatPageFactory;

  @Inject
  private ArticlePage articlePage;

  @Test
  public void wikipediaSearchTest() {
        Homepage homePage = bobcatPageFactory.create("https://en.wikipedia.org", Homepage.class);
        homePage.open().getSearchComponent().searchForQuery(SEARCH_QUERY);
        assertThat(definitionPage.getHeading(), is(HEADING));
  }
}
```

All it takes is adding the following annotations to your test class:
```java
@Modules(BobcatRunModule.class)
```
The template has already created `default.yaml` file that contains all required guice modules


## Running tests
Now you're ready to roll - trigger your test class from you IDE or execute the following command from the command line!

```
gradlew clean test
```
## Summary

As you can see, the whole process of writing tests is pretty straightforward.