#!/bin/bash

IDS_HOME="/opt/idsHome"



run_nginx()
{
 #日志打印
 ${IDS_HOME}/nginx/sbin/nginx -c ${IDS_HOME}/nginx/conf/nginx.conf > /dev/null
 echo -e "run nginx "

}


run_nginx
