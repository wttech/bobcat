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
This is the recommended template for non-AEM projects. 
{: .notice--info}

This template provides a basic Bobcat project with [JUnit 5](https://junit.org/junit5/) as the test runner.

Modules included in this template:
  - `bb-core`
  - `bb-junit5`

### AEM JUnit Template
This is the recommended template for AEM projects.
{: .notice--info}

This template is suitable for AEM (Adobe Experience Manager) test automation, with JUnit as the test runner. It provides sample AEM Author tests.

Modules included in this template:
  - `bb-core`
  - `bb-junit5`
  - `bb-aem-core`
  - `bb-aem-64`

### BDD Template
Please see the doc about Cucumber usage with Bobcat: [here]({{site.baseurl}}/docs/cucumber/) 
{: .notice--warning}

This template provides a basic Bobcat project with [Cucumber](https://docs.cucumber.io/) as the test runner.
 
Modules included in this template:
  - `bb-core`
  - additional dependencies:
    - `cucumber-java`
    - `cucumber-java8`
    - `cucumber-guice`

### AEM BDD Template
Please see the doc about Cucumber usage with Bobcat: [here]({{site.baseurl}}/docs/cucumber/) 
{: .notice--warning}

This template is suitable for AEM (Adobe Experience Manager) test automation, with [Cucumber](https://docs.cucumber.io/) as the test runner. It provides sample AEM Author tests.

Modules included in this template:
  - `bb-core`
  - `bb-aem-core`
  - `bb-aem-64`
  - additional dependencies:
    - `cucumber-java`
    - `cucumber-java8`
    - `cucumber-guice`

### Appium Template

If you would like to write automated tests for mobile devies, this template will provide you [Appium](http://appium.io/) framework.

Modules included in this template:
  - `bb-core`
  - `bb-junit5`
