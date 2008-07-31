#!/bin/sh

JAVA_EXE='java'
export JAVA_EXE
ATECH_VERSION='0.2.1'


#  Build Startup
${JAVA_EXE} -classpath "atech-tools-${ATECH_VERSION}.jar" \
  'com.atech.update.startup.BuildStartupFile'


sh run_ggc.sh
