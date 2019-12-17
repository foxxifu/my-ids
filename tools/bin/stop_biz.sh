#! /bin/bash


PROC_BIZ='ids-biz'


stop_biz()
{

   if [ $(ps -ef --columns 2000 | grep "Proc=${PROC_BIZ}" | grep -v grep | wc -l) -ne 0 ]
	then
               
		bizid=`ps -ef  --columns 2000 | grep "Proc=${PROC_BIZ}" | grep -v grep | awk '{print $2}'`
		sudo /bin/kill -9 $bizid
		sleep 1
   fi

}

stop_biz

