############################################################
此目录存放该程序的启动文件以及停止文件。
运行本程序需要JDK1.7+的JVM环境。

############################################################
start.bat
此文件用与在windows系统下启动本程序。
接受JAVA_OPTS环境变量，用于配置JVM的运行参数，其默认值为-Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC
接受JAVA_PROPERTIES环境变量，用于配置JVM的系统属性
接受JAVA_CLASSPATH环境变量，用于配置额外的类查找路径
接受JAVA_HOME环境变量，当存在此环境变量时，将使用$JAVA_HOME/bin/java代替java运行本程序

############################################################
start.sh
此文件用与在linux系统下启动本程序。
接受JAVA_OPTS环境变量，用于配置JVM的运行参数，其默认值为-Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC
接受JAVA_PROPERTIES环境变量，用于配置JVM的系统属性
接受JAVA_HOME环境变量，当存在此环境变量时，将使用$JAVA_HOME/bin/java代替java运行本程序

############################################################
kill.sh
此文件用与在linux系统下终止本程序。

——by:sense