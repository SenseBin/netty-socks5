#!/bin/bash

#by sense.

cd `dirname $0`
VAR_BIN_DIR=`pwd`
cd ..
VAR_DEPLOY_DIR=`pwd`
VAR_CONF_DIR=$VAR_DEPLOY_DIR/conf
VAR_LOG_DIR=$VAR_DEPLOY_DIR/log
VAR_LIB_DIR=$VAR_DEPLOY_DIR/lib
VAR_SRC_DIR=$VAR_DEPLOY_DIR/src
VAR_LIB_JARS=`ls $VAR_LIB_DIR|grep .jar|awk '{print "'$VAR_LIB_DIR'/"$0}'|tr "\n" ":"`
VAR_STDOUT_FILE=$VAR_LOG_DIR/stdout.log

if [ ! -d $VAR_LOG_DIR ]; then
    mkdir $VAR_LOG_DIR
fi

if [ -n "$JAVA_OPTS" ]; then
    VAR_JAVA_OPTS=$JAVA_OPTS
else
    VAR_JAVA_OPTS="-Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC"
fi

VAR_PRE_JAVA_PROPERTIES=
VAR_JAVA_PROPERTIES="$JAVA_PROPERTIES $VAR_PRE_JAVA_PROPERTIES"

MAIN_CLASS="groovy.ui.GroovyMain"
GROOVY_MAIN="../src/sense/test/socks5/Main.groovy"

ARGS=""
for arg_temp in $@
do
    ARGS="$ARGS $arg_temp"
done

VAR_JAVA_PATH=""
if [ -n "$JAVA_HOME" ]; then
    VAR_JAVA_PATH=$JAVA_HOME/bin/java
else
    VAR_JAVA_PATH="java"
fi

$VAR_JAVA_PATH $VAR_JAVA_OPTS -classpath $VAR_CONF_DIR:$VAR_LIB_JARS:$VAR_SRC_DIR $VAR_PRE_JAVA_PROPERTIES $MAIN_CLASS -indy -c UTF-8 $GROOVY_MAIN $ARGS > $VAR_STDOUT_FILE 2>&1 &
 