#!/bin/bash


client=$1
client_dir=client/main/Client

javac ${client_dir}${1}.java
java ${client_dir}${1}
