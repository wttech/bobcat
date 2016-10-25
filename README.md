![Cognifide logo](http://cognifide.github.io/images/cognifide-logo.png)

[![Build Status](https://travis-ci.org/Cognifide/bobcat.svg?branch=master)](https://travis-ci.org/Cognifide/bobcat)
[![Maven Central](https://img.shields.io/maven-central/v/com.cognifide.qa.bb/bobcat-parent.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.cognifide.qa.bb%22%20AND%20a%3A%22bobcat-parent%22)
[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/cognifide/bobcat.svg?label=License)](http://www.apache.org/licenses/)

# Bobcat

<p align="center">
  <img src="assets/bobcat-logo.png" alt="Bobcat Logo"/>
</p>

Bobcat is an automated testing framework for functional testing of web applications. It wraps Selenium Browser Automation with a handy set of tools (accelerators). Since using Selenium (Webdriver), it aims to mimic the behaviour of a real user, and as such interacts with the HTML of the application. 

Bobcat is implemented in Java and allows test development in pure JUnit and in Gherkin for BDD approaches. Bobcat also provides set of good practices that accelerates testing process.

## What's philosophy behind Bobcat?

#### We have identified major properties that Bobcat should follow:
  * Maintainable
    * written in Java with Guice,
    * provides set of libraries and helpers,
    * enforces Page Object pattern,
    * supports both JUnit and Cucumber.
  * Stable
    * provides set of Archetypes for project setup,
    * unaffectedly supports Continous Integration environments,
    * comes together with good practices.
  * Scalable
    * allows parallel execution both Junit and Cucumber tests,
    * supports testing on different levels (Integration, Staging, SIT...),
    * integrates with majority of test clouds (Sauce Labs, Cross Browser Testing, Browser Stack...).
    
## What tools does Bobcat consist of?

#### In Bobcat we combine many tools:
* Selenium to enable testing on web browsers,
* Appium to enable testing on mobile devices,
* Cucumber JVM to simplify test automation in BDD,
* ChromeDriver to enable testing on Chrome browser,
* IEDriver to enable testing on IE browser.

#### Bobcat uses set of libraries that supports development:
* Google Guice to let dependency management be more effective,
* JUnit as a test runner,
* Maven as a project managing tool.

## AEM Support

Bobcat accelerates test development, especially when it comes to AEM authoring. Bobcat provides set of libraries which allows you to test almost every action on the AEM author side. This includes:
- Site Admin,
- Dialogs,
- Parsyses,
- Components,
- Component fields,
- Crx.

#### Bobcat supports AEM authoring in newest version - AEM 6.2 and compatible features in older versions. 

## License

**Bobcat** is licensed under [Apache License, Version 2.0 (the "License")](https://www.apache.org/licenses/LICENSE-2.0.txt)

## Dependencies

- org.seleniumhq.selenium
- io.appium
- net.lightbody.bmp
- com.google.inject
- info.cukes.cucumber
- org.asserj
- org.apache.jackrabbit

## Developer setup guide

To work with **Bobcat** the following tools are required:

- JDK 8 (from _065)
- Maven 3
- Chrome Driver - if tests will be executed on chrome

## Roadmap

- AEM Touch UI testing support,
- Gradle,
- Solr testing support,
- Model based testing - http://graphwalker.github.io/

## Documentation
* [Bobcat Wiki](https://github.com/Cognifide/bobcat/wiki)
* [Bobcat 1.1.2 APIdocs](https://cognifide.github.io/bobcat/apidocs/1-1-2/)
 
