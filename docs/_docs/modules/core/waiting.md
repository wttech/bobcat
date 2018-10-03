---
title: "Waiting"
---

Available in `bb-core` since version `1.6.0`. Mechanism from previous versions: [link](https://github.com/Cognifide/bobcat/wiki/Explicit-Waits)
{: .notice--info}

## Overview
One of the fundamental problems when automating GUI is handling dynamic elements on a website: pop-ups, carousels, menus, lazy-loaded data in various places, etc.

Bobcat utilizes Selenium's out-of-the-box mechanisms: implicit and explicit waiting. More information about them can be found in [official docs](https://www.seleniumhq.org/docs/04_webdriver_advanced.jsp).  

## Implicit waiting

Implicit waiting happens before any lookup of a `WebElement` is being executed during your tests. When searching for an element it waits for a specific period of time to actually report the issue in case of the element not being present on the page.
This implicit timeout can be set manually using: `webDriver.manage().timeouts().implicitlyWait(<TIMEOUT>, <TIME UNIT>)`.

Bobcat has an in-built modifier that sets that timeout for you, just after the `WebDriver` instance is created: `ImplicitTimeoutModifier`.

The modifier is enabled by default. It is set to **1 second**.

To disable it, put the following property in your config file:
```yaml
modifier.implicitTimeout: false
```

To change the value of the timeout to e.g. 5 seconds, put the following property in your config:
```yaml
timings.implicitTimeout: 5
```

## Explicit waiting

Depending on implicit timeout will solve only a tiny fraction of your problems. That is why, the most recommended approach is to use explicit waiting.

Selenium provides the `FluentWait` and its extension, `WebDriverWait`. Bobcat wraps the latter with bunch of useful methods in the form of `BobcatWait`.

### BobcatWait

Explicit waiting relies on evaluating 'expected conditions'. They are the implementation of `ExpectedCondition` interface.

Selenium provides a hefty list of predefined conditions out-of-the-box in the `ExpectedConditions` class. This will your bread-and-butter when working with dynamic elements.

### How to wait?

To wait for a specific condition you can use two methods from `BobcatWait`:

```java
@Inject
private BobcatWait bobcatWait;

@FindBy(css=".my-element")
private WebElement testedElement;

WebElement element = bobcatWait.until(ExpectedConditions.visibilityOf(testedElement));

boolean result = bobcatWait.isConditionMet(ExpectedConditions.visibilityOf(testedElement));
```

`BobcatWait#until(condition)`:
* if the condition is met, the method will return an object of a type declared in the condition itself.
* if the condition has not been met, `TimeoutException` will be thrown
* this behaviour is exactly the same as in Selenium's `WevDriverWait#until`

`BobcatWait#isConditionMet(condition)`:
* if the condition is met, the method will return `true`
* if the condition has not been met, the method will return `false` 

### Writing your own conditions

When `ExpectedConditions` does not provide you a condition suiting your needs, you can always write your own. You can either implement it in a similar way to Selenium, i.e. create a dedicated class where you will keep your conditions, or you can also provide one inline with lamda:
`BobcatWait.until(webDriver -> heightOfElement == expectedValue);`.

### Fine-tuning your waits

Bobcat takes the following values as defaults:

```yaml
timings.explicitTimeout: 10 # in seconds
timings.implicitTimeout: 1 # in seconds
timings.pollingInterval: 500 # in milliseconds
```

You can tweak these in various ways:
* globally, by providing the above properties in your config or via command line
* locally, by using the `BobcatWait.tweak(Timings)` method before the `until` call
    * it takes an instance of `Timings` as an argument; use the `TimingsBuilder` to create one, e.g.: `new TimingsBuilder().explicitTimeout(5).pollingInterval(200).build()`

### Explicit vs implicit waiting

It is discouraged (also by Selenium maintainers) to use implicit and explicit waiting together, as it may yield unexpected results. That is why Bobcat has an in-built protection against such situations.

After calling `BobcatWait#until(condition)` Bobcat will:
1. reduce the implicit timing before evaluating the condition,
2. evaluate the condition,
3. restore the implicit timeout to the default one, or the customized value from config or a `Timings` instance passed to `BobcatWait` via `tweak(Timings)` method. 
