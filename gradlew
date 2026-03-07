#!/bin/sh
#
# Gradle start up script for POSIX
# Quick Math Android - use with gradle/wrapper/gradle-wrapper.jar (generate via Android Studio or: gradle wrapper)
#
APP_HOME=$( cd "${0%/*}/" && pwd -P ) || exit
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar
exec java -Dfile.encoding=UTF-8 -Xmx2048m -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
