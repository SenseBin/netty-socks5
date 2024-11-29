This directory contains the startup and shutdown files for the program. Running this program requires a JVM environment of JDK 8 or above.


## start.bat

This file is used to start the program on `Windows` systems.

- Accepts the JAVA_OPTS environment variable for configuring JVM runtime parameters. Its default value is -Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC.

- Accepts the JAVA_PROPERTIES environment variable for configuring JVM system properties.

- Accepts the JAVA_CLASSPATH environment variable for configuring additional class search paths.

- Accepts the JAVA_HOME environment variable. If this variable exists, the program will use $JAVA_HOME/bin/java to run instead of java.


## start.sh

This file is used to start the program on `Linux` systems.

- Accepts the JAVA_OPTS environment variable for configuring JVM runtime parameters. Its default value is -Xmx2g -Xms2g -XX:+UseParallelGC -XX:+UseParallelOldGC.

- Accepts the JAVA_PROPERTIES environment variable for configuring JVM system properties.

- Accepts the JAVA_HOME environment variable. If this variable exists, the program will use $JAVA_HOME/bin/java to run instead of java.



## kill.sh

This file is used to terminate the program on `Linux` systems.
