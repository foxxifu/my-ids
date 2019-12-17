#! /bin/bash




stop_redis()
{
    redispid=`ps -ef | grep "usr/redis/src/redis-server" | grep -v grep | awk '{print $2}'`
    for onepid in $redispid
	do
	   sudo /bin/kill -9 $onepid
	   sleep 1
	done
    echo "stop redis success"
}


stop_redis
