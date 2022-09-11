#!/bin/sh
source setenv.sh

$OPENEJB_HOME/bin/openejb deploy "$@" example.jar
