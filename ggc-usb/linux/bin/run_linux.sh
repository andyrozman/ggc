#!/bin/sh

# uncomment for debugging
#set -x

# cd to this script's directory, wherever it is called from
# if it fails, ignore that failure and hope we're in the right place, anyway
cd "$(dirname "${0}")" || true

#  **********************************************************************
#  ********   Set Java Path (calling setenv_java if exists)          ****
#  **********************************************************************
if [ -f ./setenv_java_linux.sh ]; then
   . ./setenv_java_linux.sh
fi


#  **********************************************************************
#  ********             Set ATech Tools Startup Jar                  ****
#  **********************************************************************
. ./setenv_atech.sh


#  **********************************************************************
#  ********                Create Startup Files                      ****
#  **********************************************************************
java -classpath "$ATECH_TOOLS_STARTUP" com.atech.update.startup.BuildStartupFile

#  **********************************************************************
#  ********                  Check Db Version                        ****
#  **********************************************************************
. ./db_check.sh


#  **********************************************************************
#  ********              Run 'GNU Gluco Control'                     ****
#  **********************************************************************
. ./run_ggc.sh

