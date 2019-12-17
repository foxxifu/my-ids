IDS_HOME="/opt/idsHome"
JAVA_HOME="/usr/jdk/jdk1.7.0_79"

runServer()
{
	if [ -n "$CLASSPATH" ]; then
		CLASSPATH=""
	fi
	CLASSPATH=${IDS_HOME}/ids-conf/dev-conf
	CLASSPATH="$CLASSPATH":${IDS_HOME}/ids-conf/common-conf

	for i in ${IDS_HOME}/lib/dev-lib/*.jar; do
		CLASSPATH="$CLASSPATH":"$i"
	done
	
	for i in ${IDS_HOME}/lib/common-lib/*.jar; do
		CLASSPATH="$CLASSPATH":"$i"
	done

	for i in ${IDS_HOME}/lib/third-lib/*.jar; do
                CLASSPATH="$CLASSPATH":"$i"
        done

	
	export CLASSPATH

	RunMain="com.interest.ids.dev.starter.web.DevMain"
	
	javacmd="${JAVA_HOME}/bin/java -DProc=ids-dev -Dlog_dev=/srv/log/logs/devLogs -Xms4G -Xmx4G -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly
    -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSScavengeBeforeRemark
    -XX:+CMSClassUnloadingEnabled -XX:PermSize=256M -XX:MaxPermSize=256M -XX:+PrintGCDetails -XX:+PrintGCDateStamps
	-Xloggc:/srv/log/logs/devGc.log -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=20M -XX:+HeapDumpOnOutOfMemoryError  ${RunMain}"
	
	ulimit -n 409600
	ulimit -c unlimited

	exec $javacmd
}

runServer
