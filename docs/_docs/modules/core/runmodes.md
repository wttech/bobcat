---
title: "Run Modes"
---

## Overview

Run Modes system allows to run the same tests with different set of guice modules which can be translated as different sets of guice bindings.
Its power is the best seen when we are using interfaces and bind specific implementations for different enviroments or machines (desktop or mobile testing, different versions of AEM)

For example we have set of AEM Authoring Tests prepared for version 6.4. We use `Aem64FullModule`, we have our own set of interfaces for components and their implementations for 6.4. 
Now we want to run them also on 6.5 version and we want to reuse already written tests. With runmodes we can define that new runmode will use `Aem65FullModule` (when it will appear) not `Aem64FullModule` and module with different implementations. We define this in one yaml file and switch tests using one system property.
Bobcat will switch modules and allow to run same tests for different version of AEM.

## Using Run Modes

Run modes are already included in our templates and their usage requires 3 things:

* Add `BobcatRunModule.class` to test class (it replaces `GuiceRunModule.class` from previous versions)
* Add <runmode_name>.yaml file with modules to `./main/resources/runmodes/` directory
* Run tests with `runmode` property: `gradlew clean test -Drunmode-=<runmode_name>`

Default runmode is named `default` and is used if system property is not set. Every template contains `default.yaml`

### Example

Our test class:

```java
@Modules(BobcatRunModule.class)
public class WikipediaTest {
//Test code
}
```

Runmode file:

```yaml
- com.cognifide.qa.bb.modules.CoreModule
```
    