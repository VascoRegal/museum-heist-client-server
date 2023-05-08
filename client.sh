#!/bin/bash


client=$1
client_dir=client/main/Client

find . -name "*.class" -type f -print0 | xargs -0 /bin/rm -f
javac ${client_dir}${1}.java
java ${client_dir}${1}
