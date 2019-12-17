#!/bin/bash
REDIS_HOME="/usr/redis"
REDIS_PACKAGE=redis.tar.gz
LOG_FILE=./install.log

install_redis()
{
   if [ -d ${REDIS_HOME} ]
    then
        echo "redis already install"
    else
        sudo tar xvf ../thirdpackage/redis/${REDIS_PACKAGE} -C /usr >> ${LOG_FILE} 2>> ${LOG_FILE}
    fi

}

echo "******** install redis start *********"
echo "******** install redis start *********" >> ${LOG_FILE}

install_redis

echo "******** install redis end *********"
echo "******** install redis end *********" >> ${LOG_FILE}