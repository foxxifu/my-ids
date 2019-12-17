#!/bin/bash

INS_DATE=`date "+%Y-%m-%d_%H:%M:%S"`
VERSION_FILE=/opt/idsHome/ids-conf/biz-conf/version.properties
REDIS_PASS="123456"
LOG_FILE=./install.log

set_version(){
	
	VERSION=`cat ${VERSION_FILE} | grep 'version='`
	VERSION=${VERSION#*=}
	VERSION=${VERSION//_/.}

	echo ${VERSION}
	echo ${VERSION} >>${LOG_FILE}
	
	#重新记录版本号到资源文件中，使得资源文件中的版本号和安装时间不会有重复记录
	echo "version=${VERSION}" > ${VERSION_FILE}

}

set_installDate(){
	
	echo ${INS_DATE}
	echo ${INS_DATE} >>${LOG_FILE}
	
	#记录安装时间到资源文件中
	echo install_date=${INS_DATE} >> ${VERSION_FILE}
	
}


echo "******** mark version info start *********"
echo "******** mark version info start *********" >> ${LOG_FILE}

set_version

set_installDate

echo "******** mark version info end *********"
echo "******** mark version info end *********" >> ${LOG_FILE}
