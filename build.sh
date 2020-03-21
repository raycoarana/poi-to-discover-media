#!/usr/bin/env sh
date="2019-09-17"

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi
cmd="$JAVACMD -jar ./build/libs/poi-to-discover-media-1.2.jar "

`$cmd input=\"./input/Lufop-Zones-de-danger-EU-CSV.zip,./input/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip\" output=\"./output/Release-$date-EU\" date=$date`
`$cmd input=\"./input/Lufop-Zones-de-danger-EU-CSV.zip,./input/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip\" output=\"./output/Release-$date-EU-WithoutHiddenAndPhoto\" date=$date ignore=Hidden,Photo`
`$cmd input=\"./input/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip\" output=\"./output/Release-$date-ES\" date=$date`
`$cmd input=\"./input/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip\" output=\"./output/Release-$date-ES-WithoutHiddenAndPhoto\" date=$date ignore=Hidden,Photo`
