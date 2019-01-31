---
title: "Active AEM Page"
---

## Overview

`ActiveAemAuthorPage.class` allows to use page objects on aem authoring page without creating own implementation of `AemAuthorPage.class`. It can help when we want to validate authoring 

## Using `ActiveAemAuthorPage.class`

We use method from `BobcatAemPageFactory.class` to create instance of class and then we can use method `getContent` to retrieve page object in preview mode

```java
 ActiveAemAuthorPage testPage = bobcatAemPageFactory.create("/editor.html" + TEST_PAGE_PATH + ".html");
(...) //Actions on page
 TextComponent content = testPage.getContent(TextComponent.class, 0);
```

## Additional info

* `bobcat.page.path` is restricted property used by page factory mechanism and should not be used in project properties
