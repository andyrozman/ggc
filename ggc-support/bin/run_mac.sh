#!/bin/bash

JAVA_EXE=java;
export JAVA_EXE;


#  Build Startup
$JAVA_EXE -classpath atech-tools-0.1.9.jar com.atech.update.startup.BuildStartupFile


sh run_ggc.sh