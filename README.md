> ## This repository is no longer actively maintained, please [read this article for more details](https://wttech.blog/blog/2021/bobcat-retirement/).
<hr>

<p align="center">
    <img src="https://github.com/wttech/aet/raw/master/misc/img/WT_Logo_Blue_Positive_RGB.png" alt="Wunderman Thompson Logo" width="150"/>
</p>

[![Build Status](https://travis-ci.org/Cognifide/bobcat.svg?branch=master)](https://travis-ci.org/Cognifide/bobcat)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.cognifide.qa.bb%3Abobcat&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.cognifide.qa.bb%3Abobcat)
[![Maven Central](https://img.shields.io/maven-central/v/com.cognifide.qa.bb/bobcat.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.cognifide.qa.bb%22%20AND%20a%3A%22bobcat%22)
[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/cognifide/bobcat.svg?label=License)](http://www.apache.org/licenses/)
[![Gitter chat](https://badges.gitter.im/bobcat-framework/Lobby.png)](https://gitter.im/bobcat-framework/Lobby)
[![Javadocs](http://www.javadoc.io/badge/com.cognifide.qa.bb/bb-core.svg)](http://www.javadoc.io/doc/com.cognifide.qa.bb/bb-core)

<p align="center">
  <img src="assets/bobcat-logo-caption-180x180.png" alt="Bobcat Logo"/>
</p>



Bobcat is an automated testing framework for functional testing of web applications. It wraps [Selenium](https://github.com/SeleniumHQ/selenium), so anything possible in raw Selenium can be done with Bobcat, including running it on all supported browsers, Selenium Grid, cloud providers like SauceLabs, Cross Browser Testing, BrwoserStack etc. You can also utilize [Appium](http://appium.io/) for mobile testing. Bobcat comes with a handy set of utilities and accelerators that will help you develop your tests faster.

Bobcat is implemented in Java and allows the test development in pure JUnit or via Cucumber-JVM for BDD-oriented people.

## Getting started

To start your adventure with Bobcat, please refer to our documentation: [Getting started](https://cognifide.github.io/bobcat/docs/getting-started/)

## AEM Support

Since Cognifide expertises in development on top of [Adobe Experience Manager (AEM)](https://www.adobe.com/marketing-cloud/experience-manager.html), Bobcat accelerates test development for this platform, especially when it comes to AEM authoring. The framework provides dedicated modules which allow you to automate almost every action on the AEM author side. This includes:
- Site Admin,
- dialogs,
- parsyses,
- components,
- component fields,
- CRX.

For information about compatibility with specific AEM versions, please refer to [documentation](https://cognifide.github.io/bobcat/docs/modules/aem/compatibility/)

## What's so special about Bobcat?

We created this framework at Cognifide after years of experience with multiple simpler solutions. After reaching a certain point of maturity, we have decided it was time for a tool developed properly, by bringing the knowledge of both our QA engineers and developers. We wanted our framework to be maintainable, stable and scalable.

Bobcat relies heavily on the Page Object pattern, which is an embedded concept in most of its internals. We can model our websites easily and create a neat Page Object tree structure, scoping Page Objects inside other Page Objects, reducing the effort of selector maintenance. You can read more about it [here](https://github.com/Cognifide/bobcat/wiki/PageObject). We make all the magic possible thanks to dependency injection with Google's Guice. Additionally, we wanted to make it as scalable as possible, so thread-safety was one of the key principles during the development.

## Documentation
See our [Quick-start guide](https://cognifide.github.io/bobcat/docs/getting-started/) and [Bobcat Wiki](https://github.com/Cognifide/bobcat/wiki) for examples and documentation of all features.

You might also want to check latest [Bobcat API docs](http://www.javadoc.io/doc/com.cognifide.qa.bb/bb-core).

## Contributing

You can help make Bobcat more awesome by raising any encountered issues or feature requests here on [Github](https://github.com/Cognifide/bobcat/issues). For any questions, please contact us on [Gitter](https://gitter.im/bobcat-framework/Lobby) or via [email](mailto:bobcat@cognifide.com). See our [contribution guidelines](https://github.com/Cognifide/bobcat/blob/master/CONTRIBUTING.md) for more details.

## License

**Bobcat** is licensed under [Apache License, Version 2.0 (the "License")](https://www.apache.org/licenses/LICENSE-2.0.txt)
