@echo off
rem set LIB_DIR=../../lib
set LIB_DIR=../../lib
set LIB_UTIL=../../lib/utils
set LIB_HIB=../../lib/hibernate3
set LIB_JDBC=../../lib/jdbc

java -classpath .;%LIB_UTIL%/skinlf-1.2.11.jar;%LIB_JDBC%/hsqldb.jar;%LIB_DIR%/itext-1.2.jar;%LIB_JDBC%/mysql-jconn-3_1_7.jar;%LIB_JDBC%/postgresql-8.0-311.jdbc3.jar;%LIB_HIB%/asm.jar;%LIB_HIB%/asm-attrs.jar;%LIB_HIB%/c3p0-0.9.0.jar;%LIB_HIB%/cglib-2.1.3.jar;%LIB_HIB%/commons-collections-2.1.1.jar;%LIB_HIB%/commons-logging-1.0.4.jar;%LIB_HIB%/dom4j-1.6.1.jar;%LIB_HIB%/hibernate3.jar;%LIB_HIB%/jdbc2_0-stdext.jar;%LIB_HIB%/jta.jar;%LIB_HIB%/log4j-1.2.11.jar;%LIB_HIB%/ehcache-1.1.jar;%LIB_HIB%/commons-lang-2.0.jar;%LIB_HIB%/antlr-2.7.6rc1.jar;%LIB_DIR%/jhall.jar;. ggc.GGC
