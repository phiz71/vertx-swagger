#!/bin/sh

export TARGET_OUTPUT="target"

if [ ! -d "$TARGET_OUTPUT" ]; then
    echo "Control will enter here if $TARGET_OUTPUT doesn't exist. Therefore build operation going to start."
    mvn clean install
fi

java -jar target/petstore-vertx-rx-server-1.0.0-SNAPSHOT-fat.jar -conf config.json