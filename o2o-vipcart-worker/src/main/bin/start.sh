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


# BASEDIR=/export/App/service.template.o2o.jd.com
BASEDIR=$(cd "$(dirname "$0")"; cd ../; pwd)
CLASSPATH="$BASEDIR"/:"$BASEDIR"/lib/*

echo "$CLASSPATH"

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly."
  echo "  We cannot execute $JAVACMD"
  exit 1
fi

if [ -z "$OPTS_MEMORY" ] ; then
    OPTS_MEMORY="-Xms2048M -Xmx2048M"
fi

setsid "$JAVACMD" $JAVA_OPTS \
  $OPTS_MEMORY \
  -classpath "$CLASSPATH" \
  -Dbasedir="$BASEDIR" \
  -Dfile.encoding="UTF-8" \
  com.jd.o2o.vipcart.worker.launchervipcartnWorkerLauncher \
  "$@" > /dev/null 2>&1 &