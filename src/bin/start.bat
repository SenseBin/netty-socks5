@echo off & setlocal enabledelayedexpansion

rem �ж��Ƿ����JAVA_OPTS�����������������ڣ�������ΪĬ��ֵ
if not defined JAVA_OPTS (
    set JAVA_OPTS=-Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC
)

rem ����java�������ִ���ļ�
set JAVA_EXEC=java
if defined JAVA_HOME (
    set JAVA_EXEC=%JAVA_HOME%\bin\java
)

rem ��������
set JAVA_MAIN=groovy.ui.GroovyMain
set GROOVY_MAIN=../src/sense/test/socks5/Main.groovy

set LIB_JARS=..\src
cd ..\lib
for %%i in (*) do set LIB_JARS=!LIB_JARS!;..\lib\%%i
cd ..\bin

%JAVA_EXEC% %JAVA_OPTS% -classpath ..\conf;%LIB_JARS%;%JAVA_CLASSPATH% %JAVA_PROPERTIES% %JAVA_MAIN% -indy -c UTF-8 %GROOVY_MAIN% %1 %2 %3 %4 %5 %6 %7 %8 %9

pause