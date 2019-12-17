#! /bin/bash


PROC_DEV='ids-dev'


stop_dev()
{

   if [ $(ps -ef --columns 2000 | grep "Proc=${PROC_DEV}" | grep -v grep | wc -l) -ne 0 ]
	then
               
		devid=`ps -ef  --columns 2000 | grep "Proc=${PROC_DEV}" | grep -v grep | awk '{print $2}'`
		sudo /bin/kill -9 $devid
		sleep 1
   fi

}

stop_dev
