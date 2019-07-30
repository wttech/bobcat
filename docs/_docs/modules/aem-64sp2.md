---
title: "Module: AEM 6.4 SP2"
---

`bb-aem-64sp2` is the module dedicated for authoring tests on AEM version 6.4 with Service Pack 2 (6.4.2). 
To configure it in your project you'll obligatory need these two steps:

1. Add dependency to the module into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-aem-64sp2', version: '<Bobcat Version>'
    }
    ```
2. Install this module:
    - by adding following into your runmode file:
    
    ```yaml
     - com.cognifide.qa.bb.aem.core.modules.Aem64SP2FullModule
    ```
    
    - by adding following line directly in your project's Guice module:
    
    ```java
    install(new Aem64SP2FullModule());
    ```