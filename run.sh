#!/bin/bash

find . -name "*.java" -print | xargs javac
java -classpath ./src com.wallet.Main