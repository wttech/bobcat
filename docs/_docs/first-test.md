---
title: Writing your first test with Bobcat
---

Depending on your choice during project generation or your own setup, you either ended up with JUnit or CucumberJVM as your test runner option. Please refer to the respective sections below. Note that the 'common part' refers to both runners.

## Context

We will write a simple test that will check the following:

1. Open the Wikipedia homepage.
2. Search for 'hello world' in the search box.
3. Open the first article
4. Check that it's the '"Hello, World!" program' article.

## Common part

No matter how you want to run your tests, there will always be a common part - how you access and manipulate your webpage under test.

In case of Bobcat, to make the most use of its powers, the recommended approach is the Page Object pattern. It is a concept strongly embedded within Bobcat's internals.

Let's create our first page object then!

### Our first Page Objects

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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import com.cognifide.qa.bb.qualifier.PageObject;
import com.google.inject.Inject;

@PageObject
public class Homepage {

  private static final String URL =  "https://en.wikipedia.org";

  @Inject
  private WebDriver webDriver;

  @FindBy(id = "p-search")
  private SearchComponent searchComponent;

  public SearchComponent getSearchComponent() {
    return searchComponent;
  }

  public Homepage open() {
    webDriver.get(URL);
    return this;
  }
}
```

Few elements probably require a bit of explanation:

- `@PageObject` annotation on the whole class informs Bobcat that this is a... page object :)! Thanks to this, Bobcat will create a proper page object structure and handle Page Factory initialization across the whole tree. **Always remember about putting `@PageObject` annotation on your page objects!**
- How have we obrtained a `WebDriver` instance? By simply injecting it!
```java
  @Inject
  private WebDriver webDriver;
```
Guice, the dependency injection framework used in Bobcat, will create and provide a proper instance of WebDriver based on your configuration options. If you're not interested in how it's being done, you can simply stick with this information. If you're more curious, you can check `WebDriverProvider` in Bobcat source :).
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

*Note:* if you would like to read more about working with Page Objects in Bobcat, check out the [Working with Page Objects in Bobcat]({{site.baseurl}}/docs/page-objects/).
{: .notice--info}
--------------

## JUnit

When writing tests in JUnit, one thing needs to be taken care of: constructing a Guice context. Now, you could do this all by yourself but to speed things up, Bobcat provides a dedicated test runner.

Using it looks like this:
```java
package com.hello.world.wikipedia;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.cognifide.qa.bb.junit.Modules;
import com.cognifide.qa.bb.junit.TestRunner;
import com.google.inject.Inject;
import com.hello.world.GuiceModule;

@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
public class WikipediaTest {

  private static final String SEARCH_QUERY = "hello world";
  private static final String HEADING = "\"Hello, World!\" program";

  @Inject
  private Homepage homepage;

  @Inject
  private ArticlePage articlePage;

  @Test
  public void wikipediaSearchTest() {
    homepage.open()
      .getSearchComponent()
      .searchForQuery(SEARCH_QUERY);
    assertThat(articlePage.getHeading(), is(HEADING));
  }
}
```

All it takes is adding the following annotations to your test class:
```java
@RunWith(TestRunner.class)
@Modules(GuiceModule.class)
```

Note: `GuiceModule` is the default name of the module generated from the template - in case you changed that, you need to provide your Guice module(s) here.
{: .notice--info}

------

## Cucumber-JVM

If you have picked the BDD-flavored project template, that means you will be running your tests using Cucumber.

Nothing changes when it comes to page objects but we will need to introduce additional layer - step definitions. They are responsible for translating our Gherkin scenario into code.

### Gherkin scenario

First, let's create our scenario: a helloworld.feature file in `src/test/resources/features` directory:

```gherkin
@hello-world
Feature: As a user I want to search for query on wikipedia page

  Scenario: Search for 'hello-world' query
    Given I open wikipedia homepage
    When I search for "hello world" query
    Then article page with header "\"Hello, World!\" program" is displayed
```

### Step definitions

Now it's time to create step definitions for each of the sentences in our scenario (this process does not differ from raw Cucumber).
Create WikipediaSteps class in `com.hello.world.steps` package:

```java
package com.hello.world.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.google.inject.Inject;
import com.hello.world.pageobjects.wikipedia.ArticlePage;
import com.hello.world.pageobjects.wikipedia.Homepage;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class WikipediaSteps {

  @Inject
  private Homepage homepage;

  @Inject
  private ArticlePage articlePage;

  @Given("^I open wikipedia homepage$")
  public void iOpenWikipediaHomepage() {
    homepage.open();
  }

  @When("^I search for \"([^\"]*)\" query$")
  public void iSearchForQuery(String query) {
    homepage.getSearchComponent().searchForQuery(query);
  }

  @Then("^article page with header \"(.*)\" is displayed$")
  public void followingPageHeaderIsDisplayed(String headerText) {
    assertThat("Article page for " + headerText + " is not displayed",
            articlePage.getHeading(), is(headerText));
  }
}
```

### Creating a test runner

There is only one thing that is missing - a test runner:

```java
package com.hello.world;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features/",
    plugin = {"pretty", "html:target/cucumber-html-report/example",
        "json:target/example.json"},
    tags = {"@hello-world"},
    glue = "com.hello.world"
)
public class WikipediaTest {
  // This class is empty on purpose - it's only a runner for cucumber tests.
}
```

## Running tests
Now you're ready to roll - trigger your test class from you IDE or execute the following command from the command line!

```
mvn clean test -Dtest=WikipediaTest
```

Note: due to the fact that Bobcat needs a Guice context constructed, running scenarios via in-built Cucumber plugins (e.g. in IntelliJ IDEA) will fail. You need to use the JUnit runner.
{: .notice--warning}

## Summary

As you can see, the whole process of writing tests is pretty straightforward. No matter what type of runner you use, you can use all the features exposed by [Bobcat modules]({{site.baseurl}}/docs/modules/).