#!/bin/bash


PROC_DEV="ids-biz"

start_biz()
{
	if [ $(ps -ef --columns 2000 | grep "Proc=${PROC_DEV}" |  grep -v grep | wc -l) -eq 0 ]
	then
		
		
		echo -e "run biz "
		
		nohup ./run_biz_java.sh  1>/srv/log/logs/biz.log 2>/srv/log/logs/bizError.log &
	else
		echo "biz has started"
	fi
}


start_biz

