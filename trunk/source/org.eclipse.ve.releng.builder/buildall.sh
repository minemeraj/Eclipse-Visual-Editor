# !/bin/sh

usage() {
	echo "usage: buildAll [-mapVersionTag HEAD|<branch name>] [-vm <url to java executable to run build>] [-bc <bootclasspath>] [-target <buildall target to execute>] [-buildID <buildID, e.g. 2.1.2>]  [-ftp <userid> <password>] [-rsync <rsync password file>] I|M"
}

# tag to use when checking out .map file project
mapVersionTag=HEAD

# default setting for buildType
buildType=""

# default setting for buildID
buildID=""

# default bootclasspath
bootclasspath=""

# vm used to run the build.  Defaults to java on system path
vm=java

# target used if not default (to allow run just a portion of buildAll)
target=""

# FTP user/password, required for Windows to ftp. Without it, no push.
ftpUser=""
ftpPassword=""

# RSYNC Password file location, required for Linux. Without it, no push.
rsyncPWFile=""

# NOTEST flags
notest=""

if [ "x$1" == "x" ] ; then
        usage
        exit 0
fi

while [ "$#" -gt 0 ] ; do
        case $1 in
                '-mapVersionTag')
                        mapVersionTag=$2;
                        shift 1
                    ;;
                '-vm')
                        vm=$2;
                        shift 1
                    ;;
                '-bc')
                        bootclasspath="-Dbootclasspath=$2";
                        shift 1
                    ;;
                '-target')
                        target=$2;
                        shift 1
                    ;;
                '-buildID')
                        buildID="-DbuildId=$2";
                        shift 1
                    ;;
                '-ftp')
                        ftpUser="-DftpUser=$2";
                        ftpPassword="-DftpPassword=$3"
                        shift 2
                    ;;
                '-rsync')
                		rsyncPWFile="-DrsyncPWFile=$2"
                		shift 1
                	;;
                '-notest')
                		notest="-Dnotest=true"
                	;;
                *)
                        buildType=$1
                    ;;
        esac
        shift 1
done

if [ -z $target && -z $buildType ] ; then
	usage
	exit 0
fi

$vm -cp ../org.eclipse.releng.basebuilder/startup.jar org.eclipse.core.launcher.Main -application org.eclipse.ant.core.antRunner -f buildAll.xml $target $bootclasspath -DbuildingOSGi=true -Dplatform=LinuxGTK -DmapVersionTag=$mapVersionTag -DbuildType=$buildType $notest $buildID $rsyncPWFile $ftpUser $ftpPassword
