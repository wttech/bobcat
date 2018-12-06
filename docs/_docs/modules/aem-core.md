---
title: "Module: AEM Core"
---

AEM Core is the base module for any AEM Authoring tests. To configure it in your project you'll obligatory need these two steps:
1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-aem-core', version: '<Bobcat Version>'
    }
    ```
2. Installation:

    This module should not be installed alone. It should be included in instalation of required AEM version module.
    But if separate installation is required then add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.aem.core.modules.AemCoreModule
     - com.cognifide.qa.bb.aem.core.modules.AemConfigModule
     ```
