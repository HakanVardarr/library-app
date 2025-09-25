#!/bin/bash
mvn clean package dependency:copy-dependencies
java -cp "target/library-app-1.0-SNAPSHOT.jar:target/dependency/*" com.hakanvardar.App
