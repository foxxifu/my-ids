#! /bin/bash

REDIS_HOME="/usr/redis"
IDS_HOME="/opt/idsHome";
down_server()
{
   echo "********stop dev********"
   ./stop_dev.sh
   
   echo "********stop biz********"
   ./stop_biz.sh
}




uninstall_ids()
{
   
   if [ -d $IDS_HOME ]
    then
       rm -rf ${IDS_HOME}
       echo "uninstll ids success"
   fi
}


down_server

uninstall_ids
