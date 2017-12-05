#!/usr/bin/env bash

./gradlew clean build

pushd source/
    cf push -f manifest.yml
popd

pushd processor/
    cf push -f manifest.yml
popd

pushd sink/
    cf push -f manifest.yml
popd