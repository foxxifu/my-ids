#! /bin/bash


uninstall_mysql()
{
  
  service mysql stop
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
 # rm -rf /var/lib/mysql
 rm -rf /etc/my.cnf
  echo "uninstall mysql success"
}




uninstall_mysql



