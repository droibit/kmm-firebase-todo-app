#!/bin/sh

OUTPUT_DIR=./TodoApp/Resources/Settings.bundle

if which mint >/dev/null; then
  xcrun --sdk macosx mint run mono0926/LicensePlist license-plist --output-path $OUTPUT_DIR
  rm $OUTPUT_DIR/com.mono0926.LicensePlist.latest_result.txt
else
  echo "warning: Mint not installed, download from https://github.com/yonaskolb/Mint"
fi    