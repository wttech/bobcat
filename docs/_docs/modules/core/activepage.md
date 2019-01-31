---
title: "Active Page"
---

## Overview

`ActivePage.class` allows to use page objects on page but without create class that will gather them in one place. We can open page in browser and dynamically get page objects from it.

## Using `ActivePage.class`

We use method from `BobcatPageFactory.class` to create instance of class and then we can use method `getPageObject` to retrieve page object

```java
 ActivePage homePage = bobcatPageFactory.create("https://en.wikipedia.org");
 homePage.open().getPageObject(SearchComponent.class).searchForQuery(SEARCH_QUERY);
```

We don't have to add any open method or path url it is already included.


## Additional info

* `bobcat.page.path` is restricted property used by page factory mechanism and should not be used in project properties
* `ActiveAemAuthorPage.class` is more advanced version of `ActivePage.class` and is used in AEM Authoring Tests. 