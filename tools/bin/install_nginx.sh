#!/bin/bash

NGINX_PACKAGE="nginx.tar.gz"
IDS_HOME="/opt/idsHome"
LOG_FILE=./install.log

install_nginx()
{
    if [ -d ${IDS_HOME}/nginx ]
    then
        echo "nginx already install"
    else
       
        sudo tar -xvf ../thirdpackage/nginx/${NGINX_PACKAGE} -C ${IDS_HOME} >> ${LOG_FILE} 2>> ${LOG_FILE}
    fi
    cp -r ../web-client/* ${IDS_HOME}/nginx/html
    echo "install nginx success"

}

echo "******** install nginx start *********"
echo "******** install nginx start *********" >> ${LOG_FILE}

install_nginx

echo "******** install nginx end *********"
echo "******** install nginx end *********" >> ${LOG_FILE}
