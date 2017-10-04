@echo off & setlocal enabledelayedexpansion

rem 判断是否存在JAVA_OPTS环境变量。若不存在，设置其为默认值
if not defined JAVA_OPTS (
    set JAVA_OPTS=-Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC
)

rem 设置java虚拟机可执行文件
set JAVA_EXEC=java
if defined JAVA_HOME (
    set JAVA_EXEC=%JAVA_HOME%\bin\java
)

rem 设置主类
set JAVA_MAIN=groovy.ui.GroovyMain
set GROOVY_MAIN=../src/sense/test/socks5/Main.groovy

set LIB_JARS=..\src
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i
cd ..\bin

%JAVA_EXEC% %JAVA_OPTS% -classpath ..\conf;%LIB_JARS%;%JAVA_CLASSPATH% %JAVA_PROPERTIES% %JAVA_MAIN% -indy -c UTF-8 %GROOVY_MAIN% %1 %2 %3 %4 %5 %6 %7 %8 %9

pause