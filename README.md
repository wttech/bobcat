![Cognifide logo](http://cognifide.github.io/images/cognifide-logo.png)

[![Build Status](https://travis-ci.org/Cognifide/bobcat.svg?branch=master)](https://travis-ci.org/Cognifide/bobcat)
[![Maven Central](https://img.shields.io/maven-central/v/com.cognifide.qa.bb/bobcat.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.cognifide.qa.bb%22%20AND%20a%3A%22bobcat%22)
[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/cognifide/bobcat.svg?label=License)](http://www.apache.org/licenses/)
[![Gitter chat](https://badges.gitter.im/bobcat-framework/Lobby.png)](https://gitter.im/bobcat-framework/Lobby)
[![Javadocs](http://www.javadoc.io/badge/com.cognifide.qa.bb/bb-core.svg)](http://www.javadoc.io/doc/com.cognifide.qa.bb/bb-core)

<p align="center">
  <img src="assets/bobcat-logo-caption-180x180.png" alt="Bobcat Logo"/>
</p>

Bobcat is an automated testing framework for functional testing of web applications. It wraps [Selenium](https://github.com/SeleniumHQ/selenium), so anything possible in raw Selenium can be done with Bobcat, including running it on all supported browsers, Selenium Grid, cloud providers like SauceLabs, Cross Browser Testing, BrwoserStack etc. You can also utilize [Appium](http://appium.io/) for mobile testing. Bobcat comes with a handy set of utilities and accelerators that will help you develop your tests faster.

Bobcat is implemented in Java and allows the test development in pure JUnit or via Cucumber-JVM for BDD-oriented people.

## AEM Support

Since Cognifide expertises in development on top of [Adobe Experience Manager (AEM)](https://www.adobe.com/marketing-cloud/experience-manager.html), Bobcat accelerates test development for this platform, especially when it comes to AEM authoring. The framework provides dedicated modules which allow you to automate almost every action on the AEM author side. This includes:
- Site Admin,
- dialogs,
- parsyses,
- components,
- component fields,
- CRX.

#### Bobcat was used for automating AEM 6.3, though the latest versions of AEM-related modules were not developed for full compatibility of TouchUI interface - let us know if you run into any issue or want to submit a pull request.

## Getting started

To start your adventure with Bobcat, please refer to our wiki: [Getting started](https://github.com/Cognifide/bobcat/wiki/Getting-Started)

---
:smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat:
## Bobcat 2.0 is coming!

A high level roadmap (milestones still can change), without any concrete deadlines of what we plan for 2.0 release:

#### Milestone #1 (1.4.0) :heavy_check_mark:
1. Archetype cleanup and move to a Gradle-based template :heavy_check_mark:
2. Selenium version bump :heavy_check_mark:
3. Simplify Bobcat configuration :heavy_check_mark:

#### Milestone #2 (part of it may end up in 1.x.x)
- Switch to Gradle from end-user side :heavy_check_mark:
- Reporting revamp :heavy_check_mark:
- JUnit 5 support :heavy_check_mark:
- General refactoring, part 1 :construction: in progress
    - some parts of API might disappear in the future
    - some modules may end up without further support
    - hence the above, we will identify and deprecate the API to be removed in 2.0 release
- last 1.x.x release

#### Milestone #3
- Provide a simpler API for Bobcat users
- General refactoring, part 2
    - remove the deprecated APIs
- after the above is done, 2.0 release

#### Other ideas / nice-to-haves
- move development from Maven to Gradle

:smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat: :smirk_cat:
---

## What's so special about Bobcat?

We created this framework at Cognifide after years of experience with multiple simpler solutions. After reaching a certain point of maturity, we have decided it was time for a tool developed properly, by bringing the knowledge of both our QA engineers and developers. We wanted our framework to be maintainable, stable and scalable.

Bobcat relies heavily on the Page Object pattern, which is an embedded concept in most of its internals. We can model our websites easily and create a neat Page Object tree structure, scoping Page Objects inside other Page Objects, reducing the effort of selector maintenance. You can read more about it [here](https://github.com/Cognifide/bobcat/wiki/PageObject). We make all the magic possible thanks to dependency injection with Google's Guice. Additionally, we wanted to make it as scalable as possible, so thread-safety was one of the key principles during the development.

## Contributing

You can help make Bobcat more awesome by raising any encountered issues or feature requests here on [Github](https://github.com/Cognifide/bobcat/issues). For any questions, please contact us on [Gitter](https://gitter.im/bobcat-framework/Lobby) or via [email](mailto:bobcat@cognifide.com). See our [contribution guidelines](https://github.com/Cognifide/bobcat/blob/master/CONTRIBUTING.md) for more details.

## License

**Bobcat** is licensed under [Apache License, Version 2.0 (the "License")](https://www.apache.org/licenses/LICENSE-2.0.txt)

## Documentation
See our [Quick-start guide](https://cognifide.github.io/bobcat/docs/getting-started/) and [Bobcat Wiki](https://github.com/Cognifide/bobcat/wiki) for examples and documentation of all features.

You might also want to check latest [Bobcat API docs](http://www.javadoc.io/doc/com.cognifide.qa.bb/bb-core).
 
