@ echo off

REM script which executes build

REM tag to use when checking out .map file project
set mapVersionTag=HEAD

REM default setting for buildTYpe
set buildType=

REM default bootclasspath
set bootclasspath=

REM vm used to run the build.  Defaults to java on system path
set vm=java

if x%1==x goto usage

:processcmdlineargs

REM ****************************************************************
REM
REM Process command line arguments
REM
REM ****************************************************************
if x%1==x goto run
if x%1==x-mapVersionTag set mapVersionTag=%2 && shift && shift && goto processcmdlineargs
if x%1==x-vm set vm=%2 && shift && shift && goto processcmdlineargs
if x%1==x-bc set bootclasspath="-Dbootclasspath=%2" && shift && shift && goto processcmdlineargs
set buildType=%1 && shift && goto processcmdlineargs

:run
%vm% -cp ..\org.eclipse.releng.basebuilder\startup.jar org.eclipse.core.launcher.Main -application org.eclipse.ant.core.antRunner -f buildAll.xml %bootclasspath% -DmapVersionTag=%mapBersionTag% -DbuildType=%buildType%
goto end

:usage
echo "usage: buildAll [-mapVersionTag HEAD|<branch name>] [-vm <url to java executable to run build>] [-bc <bootclasspath>] I|M"

:end