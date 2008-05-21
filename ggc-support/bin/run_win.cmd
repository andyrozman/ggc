@echo off
set JAVA_EXE=..\..\Windows\jre_16\bin\java


echo.  Build Startup
%JAVA_EXE% -classpath atech-tools-0.1.13.jar com.atech.update.startup.BuildStartupFile

call run_ggc.bat




