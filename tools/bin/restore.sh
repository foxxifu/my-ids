#!/bin/bash

IDS_HOME=/opt/idsHome
LOG_FILE=./restore.log
BACKUP_DIR=/tmp/backup

SYSTEM_USER="interest"
SYSTEM_PWD="interest@123"
DB_NAME="ids"
RESTORE_TRIGGER_File="../thirdpackage/dbscript/restoresql/restore-trigger.sql"

if [ $# != 1 ] 
then 
	echo "USAGE: $0 date" 
	echo " e.g.: $0 2018_08_30" 
	exit 1;
else
	BACKUP_FILE=idsHome_file_bak_$1.tar.gz
	BACKUP_SQL=idsHome_sql_bak_$1.sql
	BACKUP_SQL_GZ=idsHome_sql_bak_$1.tar.gz
	
	if [ -f ${BACKUP_DIR}/${BACKUP_FILE} ]
	then
		echo "  BACKUP_FILE=${BACKUP_FILE}"
		echo "  BACKUP_FILE=${BACKUP_FILE}" >> ${LOG_FILE}
	else
		echo "  ${BACKUP_FILE} is not exist!!! Please check the date is correct."
		exit 1;
	fi
	
	if [ -f ${BACKUP_SQL_GZ} ]
	then
		echo "  BACKUP_SQL_GZ=${BACKUP_SQL_GZ}"
		echo "  BACKUP_SQL_GZ=${BACKUP_SQL_GZ}" >> ${LOG_FILE}
		echo "  BACKUP_SQL=${BACKUP_SQL}"
		echo "  BACKUP_SQL=${BACKUP_SQL}" >> ${LOG_FILE}
	else
		echo "  ${BACKUP_SQL_GZ} is not exist!!! Please check the date is correct."
		exit 1;
	fi
fi 

echo_log()
{
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####"
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####" >> ${LOG_FILE}
}

#恢复ids系统的文件至老版本
restore_ids_file()
{
	echo_log "restore_ids_file start"
	
    if [ -d ${BACKUP_DIR} ]
    then
        tar zxvf ${BACKUP_DIR}/${BACKUP_FILE} -C ${BACKUP_DIR} >> ${LOG_FILE} 2>> ${LOG_FILE}
    else
		echo "bak_file does not exist"
		echo "bak_file does not exist" >> ${LOG_FILE}
    fi
	
	if [ -d ${IDS_HOME} ]
    then
		 
		#1.删除idsHome/bin目录的.sh文件，替换为老版本备份的脚本文件
		sudo rm -rf ${IDS_HOME}/bin/*.sh >> ${LOG_FILE} 2>> ${LOG_FILE}
		sudo cp -rf ${BACKUP_DIR}${IDS_HOME}/bin/*.sh ${IDS_HOME}/bin >> ${LOG_FILE} 2>> ${LOG_FILE}
		
		#2.删除idsHome/ids-conf目录和idsHome/lib目录，替换为老版本备份的文件
		sudo rm -rf ${IDS_HOME}/ids-conf >> ${LOG_FILE} 2>> ${LOG_FILE}
		sudo rm -rf ${IDS_HOME}/lib >> ${LOG_FILE} 2>> ${LOG_FILE}
        sudo cp -rf ${BACKUP_DIR}${IDS_HOME}/. ${IDS_HOME} >> ${LOG_FILE} 2>> ${LOG_FILE}
    else
        echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi

	echo_log "restore_ids_file end"
}

#恢复ids系统的数据至老版本
restore_mysql()
{
	echo_log "restore_mysql start"

	service mysql start >> ${LOG_FILE} 2>>${LOG_FILE}
		
	tar zxvf ${BACKUP_DIR}/${BACKUP_SQL_GZ} -C / >> ${LOG_FILE} 2>> ${LOG_FILE}
	
	mysql -h127.0.0.1 -P3306 -u${SYSTEM_USER} -p${SYSTEM_PWD} --default-character-set=utf8 ${DB_NAME} < ${BACKUP_DIR}/${BACKUP_SQL} >> ${LOG_FILE} 2>>${LOG_FILE}
	mysql -h127.0.0.1 -P3306 -u${SYSTEM_USER} -p${SYSTEM_PWD} --default-character-set=utf8 ${DB_NAME} < ${RESTORE_TRIGGER_File} >> ${LOG_FILE} 2>>${LOG_FILE}

	sleep 5
	
	service mysql stop >> ${LOG_FILE} 2>>${LOG_FILE}
	
	echo_log "restore_mysql end"
}

#恢复ids系统的前端界面至老版本
restore_nginx()
{
	echo_log "restore_nginx start"
	
    if [ -d ${IDS_HOME} ]
    then
		#1.删除idsHome/nginx/html/目录下的所有文件，替换为老版本备份的文件
		sudo rm -rf ${IDS_HOME}/nginx/html/* >> ${LOG_FILE} 2>> ${LOG_FILE}
		cp -rf ${BACKUP_DIR}${IDS_HOME}/nginx/html/* ${IDS_HOME}/nginx/html
    else
        echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi
	
	echo_log "restore_nginx end"
}

#删除恢复过程中产生的临时文件
rm_tmp_file()
{
	echo_log "rm_tmp_file start"
	
	rm -rf ${BACKUP_DIR}/opt >> ${LOG_FILE} 2>> ${LOG_FILE}
	rm -f ${BACKUP_DIR}/${BACKUP_SQL} >> ${LOG_FILE} 2>> ${LOG_FILE}
	
	echo_log "rm_tmp_file end"
}

echo "******** restore ids start `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** restore ids start `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}

restore_ids_file
restore_nginx
restore_mysql

rm_tmp_file

echo "******** restore ids end `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** restore ids end `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}
