#!/bin/bash
DEV_PROCESS="ids-dev"
BIZ_PROCESS="ids-biz"


check()
{
	devProcNumber=`ps -ef |grep $DEV_PROCESS|grep -v grep|wc -l`
	if [ $devProcNumber -le 0 ];then  
		  
		echo -e "dev     [\033[32m stoped \033[0m]"
	else  
		echo -e "dev     [\033[32m running \033[0m]"
	fi 
	
	bizProcNumber=`ps -ef |grep $BIZ_PROCESS|grep -v grep|wc -l`
	if [ $bizProcNumber -le 0 ];then  
		  
		echo -e "biz     [\033[32m stoped \033[0m]"
	else  
		echo -e "biz     [\033[32m running \033[0m]"
	fi 
	
	
	
}

check
