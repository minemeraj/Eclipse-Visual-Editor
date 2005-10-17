# !/bin/sh

#environment variables
PATH=.:/bin:/usr/bin:/usr/bin/X11:/usr/local/bin:/usr/X11R6/bin:`pwd`/../linux;export PATH
#MOZILLA_FIVE_HOME=/usr/lib/mozilla-1.7.8/;export MOZILLA_FIVE_HOME
LD_ASSUME_KERNEL=2.2.5
LD_LIBRARY_PATH=.
USERNAME=`whoami`
PREVXUTHORITY=$XAUTHORITY
export XAUTHORITY=

Xvfb :42 -screen 0 1024x768x24 -ac &
sleep 3
Xnest :43 -display :42 -depth 24 &
sleep 3
twm -display localhost:43.0 -f /IBMMNT1/jvebuild/twmrc-random &
sleep 3

DISPLAY=$HOSTNAME:43.0
ulimit -c unlimited

export LD_ASSUME_KERNEL LD_LIBRARY_PATH USERNAME DISPLAY

#execute command to run tests
# If it came from CVS, it may not have the execute flag turned on.
if [ ! -x runtests ] ; then
	chmod ugo+x runtests
fi

runtests -os linux -ws gtk -arch x86 -Dplatform=linux.gtk -vm $1 > $2

kill `cat /tmp/.X43-lock`
kill `cat /tmp/.X42-lock`

export XAUTHORITY=$PREVXUTHORITY
