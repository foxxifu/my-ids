#! /bin/bash

REDIS_HOME="/usr/redis"
IDS_HOME="/opt/idsHome";
down_server()
{
   echo "********down ids********"
   ./stop_ids_all.sh
}

uninstall_mysql()
{
  
  if [ `rpm -qa | grep  MariaDB-compat-10.2.11-1.el7.centos.x86_64 | wc -l` -eq 1 ]
	then
	      rpm -e MariaDB-compat-10.2.11-1.el7.centos.x86_64 --nodeps
	fi
  if [ `rpm -qa | grep  MariaDB-common-10.2.11-1.el7.centos.x86_64 | wc -l` -eq 1 ]
	then
	      rpm -e MariaDB-common-10.2.11-1.el7.centos.x86_64 --nodeps
	fi
  
  if [ `rpm -qa | grep MariaDB-client-10.2.11-1.el7.centos.x86_64 | wc -l` -eq 1 ]
        then
               rpm -e MariaDB-client-10.2.11-1.el7.centos.x86_64 --nodeps
        fi

  if [ `rpm -qa | grep   MariaDB-server-10.2.11-1.el7.centos.x86_64 | wc -l` -eq 1 ]
        then
              rpm -e  MariaDB-server-10.2.11-1.el7.centos.x86_64 --nodeps
        fi
  #rm  /var/lib/mysql/*
  rm -rf /etc/my.cnf*
  rm -rf /opt/mysql/data/mysql
  echo "uninstall mysql success"
}



uninstall_redis()
{
    
   if [ -d $REDIS_HOME ]
    then
       rm -rf ${REDIS_HOME}
       echo "uninstll redis success"
   fi
}

uninstall_ids()
{
   
   if [ -d $IDS_HOME ]
    then
       rm -rf ${IDS_HOME}
       echo "uninstll ids success"
   fi
}



down_server

uninstall_mysql

uninstall_redis

uninstall_ids
