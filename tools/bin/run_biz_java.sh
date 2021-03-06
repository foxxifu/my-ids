IDS_HOME="/opt/idsHome"
JAVA_HOME="/usr/jdk/jdk1.7.0_79"
runServer()
{
	if [ -n "$CLASSPATH" ]; then
		CLASSPATH=""
	fi
	CLASSPATH=${IDS_HOME}/ids-conf/biz-conf
        CLASSPATH="$CLASSPATH":${IDS_HOME}/ids-conf/common-conf

	for i in ${IDS_HOME}/lib/biz-lib/*.jar; do
		CLASSPATH="$CLASSPATH":"$i"
	done
	
	for i in ${IDS_HOME}/lib/common-lib/*.jar; do
		CLASSPATH="$CLASSPATH":"$i"
	done

	for i in ${IDS_HOME}/lib/third-lib/*.jar; do
                CLASSPATH="$CLASSPATH":"$i"
        done

	
	export CLASSPATH

	RunMain="com.interest.ids.biz.web.BizApplication"
	
	javacmd="${JAVA_HOME}/bin/java -DProc=ids-biz -Dlog_biz=/srv/log/logs/bizLogs -Xms4G -Xmx4G -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly
    -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSScavengeBeforeRemark
	-XX:+CMSClassUnloadingEnabled -XX:PermSize=256M -XX:MaxPermSize=256M -XX:+PrintGCDetails -XX:+PrintGCDateStamps
	-Xloggc:/srv/log/logs/bizGc.log -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=20M -XX:+HeapDumpOnOutOfMemoryError  ${RunMain}"
	
	ulimit -n 409600
	ulimit -c unlimited

	exec $javacmd
}

runServer
