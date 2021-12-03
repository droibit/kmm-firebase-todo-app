#!/bin/sh

if [ $ACTION == "indexbuild" ]; then
  echo "Not running needle generator during indexing."
  exit 0 
fi

export SOURCEKIT_LOGGING=0 && Carthage/Checkouts/needle/Generator/bin/needle generate TodoApp/DI/NeedleGenerated.swift TodoApp/