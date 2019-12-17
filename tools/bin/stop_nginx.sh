#! /bin/bash
MASTER="nginx: master process"
WORKER="nginx: worker process"


INFO_LOG()
{
	echo "$(date +%Y-%m-%d) $(date +%H:%M:%S) INFO:$@" 
}

ERROR_LOG()
{
	echo "$(date +%Y-%m-%d) $(date +%H:%M:%S) ERROR:$@" 
}

stop_nginx()
{
       if [ $(ps -ef --columns 2000 | grep "${MASTER}"  | grep -v grep | wc -l) -ne 0 ]
	then
		webclientmapid=`ps -ef  --columns 2000 | grep "${MASTER}" | grep -v grep | awk '{print $2}'`
		sudo /bin/kill -9 $webclientmapid
		sleep 1
	fi
	
	if [ $(ps -ef --columns 2000 | grep "${WORKER}" | grep -v grep | wc -l) -ne 0 ]
	then
		webclientwopid=`ps -ef  --columns 2000 | grep "${WORKER}" | grep -v grep | awk '{print $2}'`
		sudo /bin/kill -9 $webclientwopid
		sleep 1
	fi    
	
    
}

stop_nginx
