#!/bin/bash


PROC_DEV="ids-dev"

start_dev()
{
	if [ $(ps -ef --columns 2000 | grep "Proc=${PROC_DEV}" |  grep -v grep | wc -l) -eq 0 ]
	then
		
		
		echo -e "run dev "
		
		nohup ./run_dev_java.sh  1>/srv/log/logs/dev.log 2>/srv/log/logs/devError.log &
	else
		echo "dev has started"
	fi
}


start_dev
