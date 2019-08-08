#!/bin/sh
mvn clean install -Dwebdriver.type=remote -Dwebdriver.url=https://${BROWSERSTACK_USERNAME}:${BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub -Dwebdriver.cap.browser=$BROWSER -Dwebdriver.cap.os="${OS}" -Dwebdriver.cap.browserstack.localIdentifier="${BROWSERSTACK_LOCAL_IDENTIFIER}"
