#!/bin/bash

REDIS_HOME=/usr/redis
REDIS_PASS="123456"
VERSION_FILE=/opt/idsHome/ids-conf/biz-conf/version.properties

run_redis()
{
   ${REDIS_HOME}/src/redis-server ${REDIS_HOME}/redis.conf > /dev/null & 
   echo -e "run redis "
}

set_version_info()
{
	#记录版本到redis中
	VERSION=`cat ${VERSION_FILE} | grep 'version='`
	VERSION=${VERSION#*=}
	VERSION=${VERSION//_/.}
	/usr/redis/src/redis-cli -a ${REDIS_PASS} set MySystemVersion ${VERSION} > /dev/null

	#记录安装时间到redis中	
	INS_DATE=`cat ${VERSION_FILE} | grep 'install_date='`
	INS_DATE=${INS_DATE#*=}
	/usr/redis/src/redis-cli -a ${REDIS_PASS} set MySystemInstallDate ${INS_DATE} > /dev/null
	
	#使得数据持久,避免重启数据丢失
	/usr/redis/src/redis-cli -a ${REDIS_PASS} save > /dev/null

}

run_redis

sleep 10
set_version_info



