#!/bin/sh
mvn clean install
native-image --no-server -cp target/simplelocalize-cli-*.jar io.simplelocalize.cli.SimplelocalizeCliCommand
mv io.simplelocalize.cli.simplelocalizeclicommand simplelocalize-cli
