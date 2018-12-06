---
title: "Module: AEM 64"
---

AEM 64 is the module for AEM  version 6.4 Authoring tests. To configure it in your project you'll obligatory need these two steps:
1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-aem-64', version: '<Bobcat Version>'
    }
    ```
2. Installation:
    
    To install this module add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.aem.core.modules.Aem64FullModule
     ```