#!/bin/bash
#
# simple custom linux based launcher script used by most of the current planConnect
# Java / Jetty / dropwizard / ... modules.
#
#

echo "terminating running process (if any)..."

kill -9 `ps fax|grep ${project.artifactId}|grep java|awk '{print $1}'|xargs`

echo "restarting..."

#
# tune your JVM parameters here; the default values shouldn't be too bad.

nohup java -server -XX:+UseConcMarkSweepGC -XX:-UseGCOverheadLimit -jar ${project.artifactId}-${project.version}.jar  >> /dev/null 2>> /dev/null &
