# Heap size
#
CATALINA_OPTS="$CATALINA_OPTS -Xms2g -Xmx2g"

# Number of GC threads
#
CATALINA_OPTS="$CATALINA_OPTS -XX:ParallelGCThreads=4"
CATALINA_OPTS="$CATALINA_OPTS -XX:ParallelCMSThreads=4"