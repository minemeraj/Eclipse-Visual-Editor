#!/bin/bash
clean() {
	# First clear it out if it exists
	echo "Do a clean build from scratch, including new base and ve"
	downbase=yes
	downve=yes
	rm -rf builddir
	mkdir builddir
}

downbase() {
	echo "Downloading Basebuilder"
	read baserelease rest < relengMaps/baseTag
	rm -rf org.eclipse.releng.basebuilder
	cvs -d ':pserver:anonymous@dev.eclipse.org:/home/eclipse' export -r $baserelease org.eclipse.releng.basebuilder
	if [ -f /usr/share/ve_build/org.eclipse.ant.optional.ftp.zip ] ; then
		unzip /usr/share/ve_build/org.eclipse.ant.optional.ftp.zip -d `pwd`/org.eclipse.releng.basebuilder/plugins
	fi;
}

downve() {
	echo "Downloading VE builder"
	read release rest < relengMaps/veTag
	rm -rf org.eclipse.ve.releng.builder
	cvs -d $cvsuser'@dev.eclipse.org:/home/tools' export -r $release org.eclipse.ve.releng.builder
	if [ ! -x org.eclipse.ve.releng.builder/buildall.sh ]; then
		chmod +x org.eclipse.ve.releng.builder/buildall.sh 
	fi
}

clrbuild() {
	echo "Clear out the last build. This may cause a new download of base and ve if needed."
	rm -rf  src/eclipse
}

usage() {
	echo "usage: setupvebuild {-mapfiletag tag} {-cvsuser cvsuser} [-clean] | [-clearbuild]"
	echo "Builds will be in `pwd`/builddir"
	echo "-mapfiletag: The map file tag to use, or HEAD if omitted. Will be used to determine the base and ve releng directories and download only if needed."
	echo "-clean: Clear out everything and start from scratch and download base and ve releng directories."
	echo "-clearbuild: Erase just the last build."
	echo "-downonly: Only check download files and download as needed. Do not clean or clear."
	echo "-cvsuser: Supply a cvs user to log into when using cvs to access dev.eclipse.org:/home/tools. The default is ":pserver:anonymous". Use ":ext:userid" and it will automatically set CVS_RSH to ssh. It is assummed that the logged on user is setup for ssh."
	echo "  Either clean, clearbuild, or downonly must be supplied or nothing will occur."
}

parseMapTags() {
	rm -rf tempmaps
	cvs -d $cvsuser'@dev.eclipse.org:/home/tools' export -r $mapfiletag -d tempmaps org.eclipse.ve.releng/maps/builderRelengMaps

	if [ ! -e relengMaps/baseTag ] ; then
		downbase=yes ;
		mkdir relengMaps ;
		mv tempmaps/baseTag relengMaps/baseTag ;
	elif diff -b -q tempmaps/baseTag relengMaps/baseTag ; then echo ;
	else
		downbase=yes ;
		mv -f tempmaps/baseTag relengMaps/baseTag;
	fi

	mv -f tempmaps/basePDE.properties relengMaps/basePDE.properties

	if [ ! -e relengMaps/veTag ] ; then
		downve=yes ;
		mkdir relengMaps ;
		mv tempmaps/veTag relengMaps/veTag ;
	elif diff -b -q tempmaps/veTag relengMaps/veTag ; then echo ;
	else
		downve=yes ;
		mv -f tempmaps/veTag relengMaps/veTag
	fi		

	rm -rf tempmaps
}


if [ "x$1" == "x" ] ; then
	usage
	exit 0
fi

cleanmode=no
downbase=no
downve=no
clearbuild=no
downonly=no
mapfiletag=HEAD
basetag=HEAD
vetag=HEAD
cvsuser=:pserver:anonymous
cvsuseroverride=no
while [ "$#" -gt 0 ]; do 
	case $1 in	
		'-clean') 
			cleanmode=clean;
			;;
		'-clearbuild') 
			clearbuild=yes;
			;;
		'-mapfiletag')
			mapfiletag=$2
			shift 1
			;;
		'-downonly')
			downonly=yes
			;;
		'-cvsuser')
			cvsuser=$2
			shift 1
			cvsuseroverride=yes
			;;
		*) 
			usage;
			exit 0;
			;;
	esac
	shift 1
done

if [ $cvsuseroverride == "yes" ] ; then
	export CVS_RSH=ssh
fi

if [[ $cleanmode == "no" && $clearbuild == "no" && downonly == "no"  ]] ; then
	exit 0
fi
 
if [ ! -d `pwd`/builddir ] ; then
	mkdir builddir;
fi

if [[ ! (-d `pwd`/builddir/org.eclipse.ve.releng.builder) || ! (-d `pwd`/builddir/org.eclipse.releng.basebuilder) ]] ; then
	clean;
fi

if [ $cleanmode == "clean" ] ; then
	clean;
fi

cd `pwd`/builddir;

if [ $clearbuild == "yes" ] ; then
	clrbuild;
fi

parseMapTags;

if [ $downbase == "yes" ] ; then
	downbase;
fi

if [ $downve == "yes" ] ; then
	downve;
fi


