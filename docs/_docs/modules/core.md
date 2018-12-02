---
title: "Module: Core"
---

As mentioned in Modules description, Core is the heart of Bobcat. To configure it in your project you'll obligatory need these two steps:
1. Add dependency into your `build.gradle` file:

    ```groovy
    dependencies {
        compile group: 'com.cognifide.qa.bb', name: 'bb-core', version: '<Bobcat Version>'
    }
    ```
2. Installation:

    To install this module add following into your runmode file:

     ```yaml
     - com.cognifide.qa.bb.modules.CoreModule
     ```