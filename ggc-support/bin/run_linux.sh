#!/bin/sh

JAVA_EXE=../../Linux/jre_16/bin/java;
export JAVA_EXE;


#  Build Startup
$JAVA_EXE -classpath atech-tools-0.1.13.jar com.atech.update.startup.BuildStartupFile


sh run_ggc.sh
