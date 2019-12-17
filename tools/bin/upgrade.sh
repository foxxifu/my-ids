#!/bin/bash

IDS_HOME="/opt/idsHome"
LOG_FILE=./upgrade.log
SYSTEM_USER="interest"
SYSTEM_PWD="interest@123"
DB_NAME="ids"
BACKUP_DIR=/tmp/backup

if [ $# != 1 ] 
then 
	echo "USAGE: $0 VersionNo." 
	echo " e.g.: $0 IDS_V2_0_20190830_1" 
	exit 1;
else
	#这里按升级的目标版本来区分文件夹
	UPGRADESQL_File="../thirdpackage/dbscript/upgradesql/$1"
fi 

echo_log()
{
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####"
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####" >> ${LOG_FILE}
}

#升级前，做其他一些特殊处理（这里目前仅支持一条路径的处理）
upgrade_others_pre()
{
	echo_log "upgrade_others_pre start"
	
	if [ ! -d ${BACKUP_DIR} ]
    then
        mkdir -p ${BACKUP_DIR}
    fi
	#针对logo等文件做保留处理
	cp -pf /opt/idsHome/nginx/html/favicon.* ${BACKUP_DIR}
	cp -pf /opt/idsHome/nginx/html/assets/images/logo*.png ${BACKUP_DIR}
	
	echo_log "upgrade_others_pre end"
}

#升级ids文件
#注意：idsHome/bin下面的dump.rdb文件，是redis的持久化快照，每次redis重启会读取，不能删除。
upgrade_ids_file()
{
	echo_log "upgrade_ids_file start"
	
    if [ -d ${IDS_HOME} ]
    then
		 
		#1.删除idsHome/bin目录的.sh文件，替换为新版本的脚本文件
		sudo rm -rf ${IDS_HOME}/bin/*.sh
		sudo cp -rf ./*.sh ${IDS_HOME}/bin
		
		#2.删除idsHome/ids-conf目录和idsHome/lib目录替换为新版本的文件
		sudo rm -rf ${IDS_HOME}/ids-conf
		sudo rm -rf ${IDS_HOME}/lib
        sudo cp -rf ../ids/. ${IDS_HOME}
    else
        echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi
 
	echo_log "upgrade_ids_file end"
}


#升级Nginx中存放的前端文件
upgrade_nginx()
{
	echo_log "upgrade_nginx start"
	
    if [ -d ${IDS_HOME} ]
    then
		#1.删除idsHome/nginx/html/目录下的所有文件，替换为新版本的文件
		sudo rm -rf ${IDS_HOME}/nginx/html/*
		cp -rf ../web-client/* ${IDS_HOME}/nginx/html
    else
        echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi
	
	echo_log "upgrade_nginx end"
}

#升级JDK
upgrade_jdk()
{
	echo_log "upgrade_jdk none"
}

#升级MYSQL，每条路径需要处理的sql语句可能不一样，可以通过UPGRADESQL_File变量指定的sql文件路径结合版本号来完成。（这里目前仅支持一条路径）
upgrade_mysql()
{
	echo_log "upgrade_mysql start"
	
	service mysql start >> ${LOG_FILE} 2>>${LOG_FILE}
	
	for sqlname in ${UPGRADESQL_File}/*.sql
    do
        echo " begin run sqlfile:${sqlname}" >>${LOG_FILE}
        mysql -h127.0.0.1 -P3306 -u${SYSTEM_USER} -p${SYSTEM_PWD} --default-character-set=utf8 ${DB_NAME} < ${sqlname} 2>>${LOG_FILE}
    done
	
	sleep 5
	
	service mysql stop >> ${LOG_FILE} 2>>${LOG_FILE}
	
	echo_log "upgrade_mysql end"
}

#升级redis
upgrade_redis()
{
	echo_log "upgrade_redis none"
}

#其他一些特殊处理（这里目前仅支持一条路径的处理）
upgrade_others_after()
{
	echo_log "upgrade_others_after start"
	
	#记录本次的版本号和升级时间
	./mark_version_info.sh
	
	echo_log "upgrade_others_after end"
	
	#替换logo文件
	cp -pf ${BACKUP_DIR}/favicon.* /opt/idsHome/nginx/html/
	cp -pf ${BACKUP_DIR}/logo*.png /opt/idsHome/nginx/html/assets/images/
}

#jdk、mysql、redis、Nginx沿用系统已安装版本，通常不需要卸载重装
#如果有需要特殊处理的，在相应的函数中完成
echo "******** upgrade ids start `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** upgrade ids start `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}

upgrade_others_pre
upgrade_jdk
upgrade_mysql
upgrade_ids_file
upgrade_redis
upgrade_nginx
upgrade_others_after

echo "******** upgrade ids end `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** upgrade ids end `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}
