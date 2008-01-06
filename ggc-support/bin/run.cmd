@echo off
set LIB_DIR=../lib
set LIB_JDBC=../lib/jdbc


rem # TEMP

if exist .\scripts\init.cmd call .\scripts\init.cmd

rem Clean Db
if exist ..\data\db\pis_int.lck del ..\data\db\pis_int.lck



echo.  Running GGC

java -classpath .;%GGC_LIBS%;%GGC_MAIN_JAR%; ggc.GGC




