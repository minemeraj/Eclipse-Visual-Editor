#!/bin/bash

usageexit() {
	echo "Set up VE build before running this script"
	exit 0;
}

if [ ! -d `pwd`/builddir ]; then
	usageexit
fi

if [ ! -d `pwd`/builddir/org.eclipse.ve.releng.builder ] ; then 
	usageexit
fi

cd `pwd`/builddir/org.eclipse.ve.releng.builder

./buildall.sh -vm '/IBMSW/J2SDKs/sun/jdk1.4.2_03/bin/java' -bc '/IBMSW/J2SDKs/sun/jdk1.4.2_windows/jre/lib/rt.jar' $*
