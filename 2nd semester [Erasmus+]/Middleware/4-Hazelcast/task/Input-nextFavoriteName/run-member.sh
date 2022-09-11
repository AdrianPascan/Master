#!/bin/sh
source setenv.sh
java -Dhazelcast.socket.bind.any=false Member "$@"
