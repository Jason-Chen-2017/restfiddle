#!/bin/bash
MAVEN_OPTS="-XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m" mvn spring-boot:run
