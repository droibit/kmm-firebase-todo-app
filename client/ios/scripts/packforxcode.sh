#!/bin/sh

cd "$SRCROOT/.."
./gradlew :shared:packForXCode -PXCODE_CONFIGURATION=${CONFIGURATION}