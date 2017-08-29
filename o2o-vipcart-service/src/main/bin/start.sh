#!/bin/sh

BASEDIR=`dirname $0`/..
BASEDIR=`(cd "$BASEDIR"; pwd)`

# If a specific java binary isn't specified search for the standard 'java' binary
if [ -z "$JAVACMD" ] ; then
  if [ -n "$JAVA_HOME"  ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
    else
      JAVACMD="$JAVA_HOME/bin/java"
    fi
  else
    JAVACMD=`which java`
  fi
fi


# BASEDIR=/export/App/service.vipcart
BASEDIR=$(cd "$(dirname "$0")"; cd ../; pwd)
CLASSPATH="$BASEDIR"/:"$BASEDIR"/lib/*

echo "$CLASSPATH"

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$OPTS_MEMORY" ] ; then
    #OPTS_MEMORY="-Xms2048M -Xmx2048M"
    OPTS_MEMORY="-server -Xms4096M -Xmx4096M -Xmn1536M -XX:PermSize=256m -XX:MaxPermSize=256m -XX:SurvivorRatio=8 -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:ParallelGCThreads=2 -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -XX:+PrintReferenceGC -verbose:gc -Xloggc:../gc.log -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=./java_HeapDump.hprof"
fi

setsid "$JAVACMD" $JAVA_OPTS \
  $OPTS_MEMORY \
  -classpath "$CLASSPATH" \
  -Dbasedir="$BASEDIR" \
  -Dfile.encoding="UTF-8" \
  -Dcom.sun.management.jmxremote\
  -Dcom.sun.management.jmxremote.port=12323\
  -Dcom.sun.management.jmxremote.authenticate=false\
  -Dcom.sun.management.jmxremote.ssl=false\
  com.jd.o2o.vipcart.launcher.VipcartServiceLauncher \
  "$@" > /dev/null 2>&1 &