JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF8 -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="$JAVA_OPTS -Dorg.apache.catalina.loader.WebappClassLoader.ENABLE_CLEAR_REFERENCES=false"

CATALINA_OPTS="$CATALINA_OPTS -Duser.timezone=GMT"

# Enable JMX if needed
# TODO you need to figure out which shell script code will give you a good (public)
# IP address, where JMX can bind and be reachable - to which you can connect your
# JMX client remotely
#
#IP_ADDRESS=`hostname --all-ip-addresses`
#CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9000"
#CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
#CATALINA_OPTS="$CATALINA_OPTS -Djava.rmi.server.hostname=$IP_ADDRESS"


##
## JVM tuning for Oracle JDK 8
##

# Log GC activity
#
# Settings combined from:
#   * 6.2 Deployment Checklist
#   * https://blogs.oracle.com/jonthecollector/entry/the_unspoken_gc_times
#   * http://blog.ragozin.info/2011/09/hotspot-jvm-garbage-collection-options.html
#   * https://jyates.github.io/2012/11/05/rolling-java-gc-logs.html
#
CATALINA_OPTS="$CATALINA_OPTS -Xloggc:$CATALINA_HOME/logs/gc.log -verbosegc -XX:+PrintGCDetails"
CATALINA_OPTS="$CATALINA_OPTS -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime"
CATALINA_OPTS="$CATALINA_OPTS -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps"
CATALINA_OPTS="$CATALINA_OPTS -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=20 -XX:GCLogFileSize=10M"

# Dump the memory when OOM error occurs.
#
# If '-XX:HeapDumpPath' is a folder, then the filename will be appended with pid in it
# * https://stackoverflow.com/questions/24809655/using-xxheapdumppath-option-but-want-to-integrate-the-process-id
# * http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html
#
CATALINA_OPTS="$CATALINA_OPTS -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$CATALINA_HOME/logs"


# Heap size, Metaspace
#
# Defaults from liferay-portal-tomcat bundle (-Xmx1024m -XX:MaxMetaspaceSize=384m) not very useful
#
# Better settings used based on:
#   * 6.2 Deployment Checklist
#   * https://blogs.oracle.com/jonthecollector/entry/the_second_most_important_gc
#   * '-Xmn768m' is equivalent to '-XX:NewSize=768m -XX:MaxNewSize=768m'
#
# Heap size is set in setenv.env.sh individually for each environment, based
# on EC2 instance used for given env, so do NOT set it here
#
CATALINA_OPTS="$CATALINA_OPTS -server -XX:MetaspaceSize=512m -XX:MaxMetaspaceSize=512m"
#CATALINA_OPTS="$CATALINA_OPTS -Xms2g -Xmx2g"
CATALINA_OPTS="$CATALINA_OPTS -XX:NewSize=768m -XX:MaxNewSize=768m"
CATALINA_OPTS="$CATALINA_OPTS -XX:SurvivorRatio=6 -XX:TargetSurvivorRatio=90 -XX:MaxTenuringThreshold=15"


# GC
#
# Number of GC threads is set in setenv.env.sh individually for each environment, based
# on EC2 instance used for given env, so do NOT set it here
#
CATALINA_OPTS="$CATALINA_OPTS -XX:+UseParNewGC "
#CATALINA_OPTS="$CATALINA_OPTS -XX:ParallelGCThreads=2"
CATALINA_OPTS="$CATALINA_OPTS -XX:+UseConcMarkSweepGC"
#CATALINA_OPTS="$CATALINA_OPTS -XX:ParallelCMSThreads=2"
CATALINA_OPTS="$CATALINA_OPTS -XX:CMSInitiatingOccupancyFraction=85"
CATALINA_OPTS="$CATALINA_OPTS -XX:+CMSScavengeBeforeRemark -XX:+ScavengeBeforeFullGC"
CATALINA_OPTS="$CATALINA_OPTS -XX:+CMSConcurrentMTEnabled"
CATALINA_OPTS="$CATALINA_OPTS -XX:+CMSParallelRemarkEnabled -XX:+CMSCompactWhenClearAllSoftRefs"



ENV_CONFIG_FILE="$CATALINA_HOME/bin/setenv.env.sh"

if [ -f $ENV_CONFIG_FILE ]; then
    # Include the environment-specific heap size + GC threads counts
    . $ENV_CONFIG_FILE
else
    echo "ERROR: Please create 'setenv.env.sh' in Liferay workspace (in /configs/[env]/tomcat-8.0.32/bin) or don't include the file from '/configs/common/tomcat-8.0.32/bin/setenv.sh'."
    exit 1
fi