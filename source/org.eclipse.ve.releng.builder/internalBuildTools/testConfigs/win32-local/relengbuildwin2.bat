@echo off
REM test script for relengbuildwin.
REM run tests
call runtests.bat -vm ..\jdk1.3.1\jre\bin\java "-Dplatform=win32" > %1