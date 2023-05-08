#!/bin/bash


server=$1
port=$2
server_dir=server/main/Server

find . -name "*.class" -type f -print0 | xargs -0 /bin/rm -f
javac ${server_dir}${1}.java
java ${server_dir}${1} ${2}
