@echo off
rem set LIB_DIR=../../lib
set LIB_DIR=../lib
set LIB_UTIL=../lib/utils
set LIB_HIB=../lib/hibernate3
set LIB_JDBC=../lib/jdbc

java -Djava.library.path=../lib/native/win -classpath .;%LIB_DIR%/atech-tools-0.1.14.jar;%LIB_DIR%/commons-logging-1.0.4.jar;%LIB_DIR%/dom4j-1.6.1.jar;%LIB_DIR%/log4j-1.2.11.jar;%LIB_DIR%/jhall.jar;%LIB_DIR%/RXTXcomm.jar ggc.data.meter.MeterTester %1 %2