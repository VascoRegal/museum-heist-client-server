#!/bin/bash


server=$1
port=$2
server_dir=server/main/Server

javac ${server_dir}${1}.java
java ${server_dir}${1} ${2}
