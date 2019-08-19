#!/bin/bash

ARGS=()
[[ -z "$DEVICE" ]] && ARGS+=("-Dwebdriver.url=https://${BROWSERSTACK_USERNAME}:${BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub")
[[ ! -z "$BROWSERSTACK_LOCAL_IDENTIFIER" ]] && ARGS+=("-Dwebdriver.cap.browserstack.localIdentifier=${BROWSERSTACK_LOCAL_IDENTIFIER}")
[[ ! -z "$BROWSER" ]] && ARGS+=("-Dwebdriver.cap.browser=$BROWSER")
[[ ! -z "$WEBDRIVER_TYPE" ]] && ARGS+=("-Dwebdriver.type=$WEBDRIVER_TYPE") || ARGS+=('-Dwebdriver.type=remote')
[[ ! -z "$OS" ]] && ARGS+=("-Dwebdriver.cap.os=${OS}")
[[ ! -z "$OS_VER" ]] && ARGS+=("-Dwebdriver.cap.os_version=${OS_VER}")
[[ ! -z "$DEVICE" ]] && ARGS+=("-Dwebdriver.appium.url=https://${BROWSERSTACK_USERNAME}:${BROWSERSTACK_ACCESS_KEY}@hub-cloud.browserstack.com/wd/hub")
[[ ! -z "$DEVICE" ]] && ARGS+=("-Dwebdriver.cap.device=${DEVICE}")
[[ ! -z "$DEVICE" ]] && ARGS+=('-Dwebdriver.cap.realMobile=true')

mvn clean install "${ARGS[@]}"
