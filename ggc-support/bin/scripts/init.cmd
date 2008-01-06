@echo off
echo.   Init GGC
if exist .\scripts\ggc_main.cmd call .\scripts\ggc_main.cmd
echo.   Init Libs
if exist ..\lib\scripts\init_libs.cmd call ..\lib\scripts\init_libs.cmd


