#!/bin/bash
JAVA_HOME="/usr/jdk/jdk1.7.0_79"
JDK_NAME="jdk7u79linux64.tar.gz"
softwarepath="../thirdpackage/jdk"
LOG_FILE=./install.log

#安装jdk
install_jdk()
{
    if [ -d ${JAVA_HOME}/bin ]
    then
        echo "jdk already install"
		echo "jdk already install" >> ${LOG_FILE}
    else
        if [ ! -d /usr/jdk ]
        then
            sudo mkdir -p /usr/jdk
        fi
        sudo tar xzf ${softwarepath}/${JDK_NAME} -C /usr/jdk
    fi
    home=`pwd`

	javahome=`cat /etc/profile | grep JAVA_HOME`
	
	if test -z "${javahome}"
	then
		echo "export JAVA_HOME=${JAVA_HOME}" >> /etc/profile
		echo "export CLASSPATH=$JAVA_HOME/lib" >> /etc/profile
		echo "export PATH=$PATH:$JAVA_HOME/bin" >> /etc/profile
		source /etc/profile
	else
		echo "JAVA ENV already exist. "
		echo "JAVA ENV already exist. " >> ${LOG_FILE}
	fi  
}

echo "******** install jdk start *********"
echo "******** install jdk start *********" >> ${LOG_FILE}

install_jdk

echo "******** install jdk end *********"
echo "******** install jdk end *********" >> ${LOG_FILE}
