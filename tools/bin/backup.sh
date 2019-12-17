#!/bin/bash

IDS_HOME=/opt/idsHome
LOG_FILE=./backup.log
BACKUP_DIR=/tmp/backup
BACKUP_FILE=idsHome_file_bak_`date "+%Y_%m_%d"`.tar.gz
BACKUP_SQL=idsHome_sql_bak_`date "+%Y_%m_%d"`.sql
BACKUP_SQL_GZ=idsHome_sql_bak_`date "+%Y_%m_%d"`.tar.gz
SYSTEM_USER="interest"
SYSTEM_PWD="interest@123"
DB_NAME="ids"

echo_log()
{
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####"
	echo "#### $@ `date "+%Y-%m-%d_%H:%M:%S"` ####" >> ${LOG_FILE}
}


#在备份前做一些准备工作
before_bakup()
{
	echo_log "before_bakup start"
	
	#老版本因为打印控制台日志到文件，导致该文件记录的数据量非常大，并且大多数都是debug信息，需要先删除掉。
	if [ -d ${IDS_HOME} ]
    then
		rm -f ${IDS_HOME}\biz.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\bizError.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\dev.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\devError.log >> ${LOG_FILE} 2>> ${LOG_FILE}
    else
		echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi
	
	echo_log "before_bakup end"
}
#备份ids系统的文件
backup_ids_file()
{
	echo_log "backup_ids_file start"

	if [ ! -d ${BACKUP_DIR} ]
    then
        mkdir -p ${BACKUP_DIR}
    fi
	
    if [ -d ${IDS_HOME} ]
    then
		rm -f ${IDS_HOME}\biz.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\bizError.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\dev.log >> ${LOG_FILE} 2>> ${LOG_FILE}
		rm -f ${IDS_HOME}\devError.log >> ${LOG_FILE} 2>> ${LOG_FILE}
        tar zcvf ${BACKUP_DIR}/${BACKUP_FILE} ${IDS_HOME} >> ${LOG_FILE} 2>> ${LOG_FILE}
    else
		echo "idsHome does not exist"
		echo "idsHome does not exist" >> ${LOG_FILE}
    fi

	echo_log "backup_ids_file end"
}

#备份mysql数据
backup_mysql()
{
	echo_log "backup_mysql start"
	
	service mysql start >> ${LOG_FILE} 2>>${LOG_FILE}
	
	sleep 5
	#再恢复数据库时，偶遇因为创建触发器的错误，所以，这里忽略触发器的导出，在导入时，重新读取创建触发器的sql文件。
	mysqldump -h127.0.0.1 -P3306 -u${SYSTEM_USER} -p${SYSTEM_PWD} --default-character-set=utf8 --routines --skip-triggers --hex-blob --single-transaction ${DB_NAME} > ${BACKUP_DIR}/${BACKUP_SQL} 2>>${LOG_FILE}
	
	tar zcvf ${BACKUP_DIR}/${BACKUP_SQL_GZ} ${BACKUP_DIR}/${BACKUP_SQL} >> ${LOG_FILE} 2>> ${LOG_FILE}

	rm -f ${BACKUP_DIR}/${BACKUP_SQL} >> ${LOG_FILE} 2>> ${LOG_FILE}
	
	echo_log "backup_mysql end"
}

#对备份的文件做简单检查
check_bak_file()
{
	#检查ids系统文件是否备份成功
	if [ -f ${BACKUP_DIR}/${BACKUP_FILE} ]
    then
        echo "idsFile backup success"
		echo "idsFile backup success" >> ${LOG_FILE}
    else
		echo "idsFile backup failed"
		echo "idsFile backup failed" >> ${LOG_FILE}
    fi
	
	#检查ids数据是否备份成功
	if [ -f ${BACKUP_DIR}/${BACKUP_SQL_GZ} ]
    then
        echo "idsData backup success"
		echo "idsData backup success" >> ${LOG_FILE}
    else
		echo "idsData backup failed"
		echo "idsData backup failed" >> ${LOG_FILE}
    fi
}

echo "******** backup ids start `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** backup ids start `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}

before_bakup

backup_ids_file
backup_mysql

check_bak_file

echo "******** backup ids end `date "+%Y-%m-%d_%H:%M:%S"` *********"
echo "******** backup ids end `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}
