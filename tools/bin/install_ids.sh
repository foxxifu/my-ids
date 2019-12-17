#!/bin/bash

IDS_HOME="/opt/idsHome"
LOG_FILE=./install.log



#安装ids
install_ids()
{
   if [ -d ${IDS_HOME} ]
    then
        echo "ids already install"
		echo "ids already install" >> ${LOG_FILE}
    else
        if [ ! -d ${IDS_HOME} ]
        then
            sudo mkdir -p ${IDS_HOME}
        fi
        sudo cp -r ../ids/. ${IDS_HOME}
        #拷贝bin目录
        sudo cp -r ../bin  ${IDS_HOME}
    fi
}

echo "******** install ids start *********"
echo "******** install ids start *********" >> ${LOG_FILE}

install_ids

echo "******** install ids end *********"
echo "******** install ids end *********" >> ${LOG_FILE}



