---
title: "AEM versions compatibility"
---

## Overview

Below you can find the answer to the question: which AEM version does Bobcat support?

## Support plan
Since version 2.0.0, we have changed the approach to supporting AEM versions.

### Abstraction layer
We have abstracted the whole AEM-related API to `bb-aem-core` module. This way, each version can implement its own bindings in a separate module, allowing us to be more flexible when it comes to supporting specific differences between AEM versions.

**`bb-aem-core` module should not be used by end-users**.

Bobcat users should be using the modules implementing the `bb-aem-core` APIs, such as `bb-aem-64` for AEM 6.4.

### Aligning with next versions

There are two options for supporting next versions of AEM:
1. new module is a copy of the previous one, with any needed customizations applied (or possibly with none at the time of adoption)
2. totally new implementation of `bb-aem-core` APIs is introduced

Either way, each upcoming AEM version will have its own AEM module following this naming schema: `bb-aem-AEM_VERSION`. 

## Bobcat AEM compatibility

|    Module     | Bobcat version |       AEM version       |   Status   |
|:-------------:|:--------------:|:-----------------------:|:----------:|
| bb-touch-ui   |    <= 1.6.0    |       <6.2*             |     EOL    |
|  bb-aem-64    |     latest     |        6.4              | Maintained |
|  bb-aem-64sp2 |     latest     |  6.4.2 (Service Pack 2) | Maintained |
|  bb-aem-65    |     latest     |        6.5              | Maintained |

>* 6.3 was never officially supported, it was automated with heavy customizations by some Bobcat users