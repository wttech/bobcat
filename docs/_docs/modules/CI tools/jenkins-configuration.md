---
title: "Configuring Bobcat tests on Jenkins"
---

{% include under-construction.html %}

Tests are most useful if they are being used during the whole process of development.
It's good to run tests after code changes and after deploying changes to the next environment.
The best option is to do such things automatically, so it would bring benefits to the whole team and also, to the quality of your site.

Jenkins is the one of the Continous Integration tool, which is one of the most popular worldwide.
You may set up Bobcat tests in that tool easily when following these steps:

1. Create a new item and choose type 'Maven project'
2. Configuration:
- JDK version - 1.8
- Code repository - choose your version control system and provide credentials that should be used to login (make sure that your user has required permissions),
example: 
Git, repository URL: https://github.com/myrepository, credentials - choose right user if it's needed in your project
- Branches to build: choose master or develop (depending on what is your default branch for this environment)
- Build: set up using maven version, provide POM path and add options
- Post-steps - create report and archive artifacts
3. Save your project and try to run it
4. Make further changes if it's necessary