#!/bin/sh
source setenv.sh

# the wildcard works only with java 1.6
java -cp "$OPENEJB_HOME/lib/*:example.jar:." ExampleClient
