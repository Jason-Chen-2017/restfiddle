#!/bin/bash
MAVEN_OPTS="-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m" mvn package spring-boot:repackage spring-boot:run
#MAVEN_OPTS="-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m" mvn spring-boot:run
