---
title: Getting started
---

Decided to try Bobcat out? Great!

On this page, you will find all the necessary information that will help you kickstart your journey with Bobcat.

## Prerequisites

Before you start, please make sure you have following stuff installed on your machine:

- JDK 1.8
- Maven 3.X.X

## Generating a Bobcat project

To start using Bobcat, you could simply create an empty project, add the required dependencies of wanted Bobcat modules, create a Guice module, set necessary propreties... but it already sounds like a bit of work!

That is why we streamlined the whole setup - we provide a handy Bobcat project template, which can generate your desired flavor of Bobcat configuration. Go to the [template](https://github.com/Cognifide/bobcat-gradle-template) repository, clone it and follow the instructions in README.

And that's it! You have a working Bobcat project :).

### Manual setup

If you want to go the more hardcore way or simply want to understand how we have assembled each template, you may find the [manual setup guide]({{site.baseurl}}/docs/manual-setup/) helpful.

## Choosing Bobcat modules

Even though you already have the most important modules installed thanks to the template, you might want to customize the loadout according to your needs.

Read more about available [Bobcat modules]({{site.baseurl}}/docs/modules/).

### Configuring Bobcat

Now that you have the project up and running, you might want to configure it. To do so, please refer to [Configuring Bobcat]({{site.baseurl}}/docs/configuring-bobcat/)

## Writing your first test case

Finally, the time has come to [write your first test using Bobcat!]({{site.baseurl}}/docs/first-test/)