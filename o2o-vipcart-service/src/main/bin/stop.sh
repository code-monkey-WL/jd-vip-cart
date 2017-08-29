#!/bin/sh
BASEDIR=$(cd "$(dirname "$0")"; cd ../; pwd);
echo $BASEDIR;
PIDPROC=`ps -ef | grep -w "$BASEDIR" | grep -v 'grep' | grep -v 'stop.sh' | awk '{print $2}'`;

SAFNAME=`basename "$BASEDIR"`;

if [ -z "$PIDPROC" ];then
echo $SAFNAME" is not running"
exit 0
fi

echo "RUNNING  PIDPROC: "$PIDPROC

echo "$PIDPROC" | xargs  kill -9
echo $SAFNAME" stop finished."