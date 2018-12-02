---
title: Bobcat templates
---

Aim of the templates is to easily and fast provide a way to create a Bobcat project structure.

## Using Bobcat templates

To use any Bobcat template listed below you need to:

1. Clone [bobcat-gradle-template repository](https://github.com/Cognifide/bobcat-gradle-template)
2. Choose one of the templates from the list below
3. Generate your local template as it is described in [README](https://github.com/Cognifide/bobcat-gradle-template/blob/master/README.md)

That's it! You're ready to start writing your own tests. Enjoy!

## Templates

All available Bobcat templates are listed below:

### JUnit 5 Template

This template provides a basic Bobcat project with [JUnit 5](https://junit.org/junit5/) as the test runner.

Modules included in this template:
  - `bb-core`
  - `bb-junit5`

### AEM JUnit Template

This template is suitable for AEM (Adobe Experience Manager) test automation, with JUnit as the test runner. It provides sample AEM Author tests.

Modules included in this template:
  - `bb-core`
  - `bb-junit5`
  - `bb-aem-core`
  - `bb-aem-64`

### BDD Template
**Important:** We suggest to use JUnit 5 template instead. This template will work with 2.0 version but some features will not be available. Cucumber is not supporting junit5 
{: .notice--warning}

This template provides a basic Bobcat project with [Cucumber](https://docs.cucumber.io/) as the test runner.
 
Modules included in this template:
  - `bb-core`
  - `bb-cumber`

### AEM BDD Template
**Important:** We suggest to use AEM JUnit template instead. This template will work with 2.0 version but some features will not be available. Cucumber is not supporting junit5
{: .notice--warning}

This template is suitable for AEM (Adobe Experience Manager) test automation, with [Cucumber](https://docs.cucumber.io/) as the test runner. It provides sample AEM Author tests.

Modules included in this template:
  - `bb-core`
  - `bb-cumber`
  - `bb-aem-core`
  - `bb-aem-64`

### Appium Template

If you would like to write automated tests for mobile devies, this template will provide you [Appium](http://appium.io/) framework.

Modules included in this template:
  - `bb-core`
  - `bb-reports`
  - `bb-junit`
