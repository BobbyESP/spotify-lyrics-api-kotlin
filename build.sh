#!/bin/sh
./gradlew build
jlink --module-path "$JAVA_HOME"/jmods --add-modules java.base,java.logging --output build/libs/custom-jre
cp build/libs/com.bobbyesp.spotifylyricsapi-all.jar build/libs/custom-jre/bin/
