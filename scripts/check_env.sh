#!/bin/bash

env_names=("ANDROID_HOME" "JAVA_HOME")

for item in "${env_names[@]}"
do
    if [ -n "${!item+x}" ]; then
        echo "$item: OK"
    else
        echo "$item: FAILED"
        echo "The variable $item is not present."
    fi
done
