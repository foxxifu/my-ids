#! /bin/bash

REDIS_HOME="/usr/redis"
IDS_HOME="/opt/idsHome";
down_server()
{
   echo "********stop redis********"
   ./stop_redis.sh
}



uninstall_redis()
{
    
   if [ -d $REDIS_HOME ]
    then
       rm -rf ${REDIS_HOME}
       echo "uninstll redis success"
   fi
}

down_server

uninstall_redis


