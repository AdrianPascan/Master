#!/bin/sh
source setenv.sh

# server-side beans compilation
javac -cp $OPENEJB_HOME/lib/javaee-api-8.0-5.jar -d classes mwy/*.java
jar cvf example.jar -C classes .

# client compilation
javac -cp example.jar *.java
