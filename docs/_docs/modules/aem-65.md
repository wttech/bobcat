---
title: "Module: AEM 6.5"
---

`bb-aem-65` is the module for AEM version 6.5 authoring tests. To configure it in your project you'll obligatory need these two steps:
1. Add dependency to the module into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-aem-65', version: '<Bobcat Version>'
    }
    ```
2. Install this module:
    - by adding following into your runmode file:
    
    ```yaml
     - com.cognifide.qa.bb.aem.core.modules.Aem65FullModule
    ```
    
    - by adding following line directly in your project's Guice module:
    
    ```java
    install(new Aem65FullModule());
    ```