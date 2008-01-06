@echo off
set LIB_DIR=../lib
set SCRIPT=../lib/scripts


if exist %SCRIPT%/lib_hibernate.cmd    call %SCRIPT%/lib_hibernate.cmd %LIB_DIR%
if exist %SCRIPT%/lib_atech-tools.cmd  call %SCRIPT%/lib_atech-tools.cmd %LIB_DIR%
if exist %SCRIPT%/lib_jdbc.cmd 	       call %SCRIPT%/lib_jdbc.cmd %LIB_DIR%
if exist %SCRIPT%/lib_skinlf.cmd       call %SCRIPT%/lib_skinlf.cmd %LIB_DIR%
if exist %SCRIPT%/lib_jhelp.cmd        call %SCRIPT%/lib_jhelp.cmd %LIB_DIR%
if exist %SCRIPT%/lib_misc.cmd         call %SCRIPT%/lib_misc.cmd %LIB_DIR%


set GGC_LIBS=%GGC_ATECH-TOOLS%;%GGC_HIBERNATE%;%GGC_JDBC%;%GGC_SKINLF%;%GGC_JHELP%;%GGC_MISC%

rem echo. %GGC_LIBS%





