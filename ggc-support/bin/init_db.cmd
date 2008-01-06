@echo off

echo.  ****************************************************************
echo.  **                  GGC Database Init                         **
echo.  **      Database selected in GGC_Config will be used !!!!     **
echo.  ****************************************************************
echo.

if "%1"=="doInstall" goto action
goto failed


:action
set LIB_DIR=../lib
set LIB_JDBC=../lib/jdbc


rem # TEMP

if exist .\scripts\init.cmd call .\scripts\init.cmd

rem Clean Db
if exist ..\data\db\pis_int.lck del ..\data\db\pis_int.lck



echo.  Running GGC

java -classpath .;%GGC_LIBS%;%GGC_MAIN_JAR%; ggc.db.tool.InitDb


goto end


:failed
echo.  For protection of Db you need to specify doInstall to execute
echo.
goto end


:end


