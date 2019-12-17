#!/bin/bash

systemUser="interest"
systemPwd="interest@123"
DBROOTPWD="system@123"
dbName="ids"
db_install_log="mysql.log"
createTableFile="../thirdpackage/dbscript/createtable"
initTableFile="../thirdpackage/dbscript/initdata"
mysqlDataDir="/opt/mysql/data"
LOG_FILE=./install.log

db_error_log()
{
	echo "$(date +%Y-%m-%d) $(date +%H:%M:%S) ERROR:$@"
}

db_info_log()
{
	echo "$(date +%Y-%m-%d) $(date +%H:%M:%S) INFO:$@"
}

#检查是否安装MYSQL
checkinstalldb()
{
	if [ `rpm -qa | grep -i "MariaDB-server-10.2.11-1.el7.centos.x86_64" | wc -l` -eq 1 ]
	then							
		echo "Mysql server is exits,please uninstall mysql server"
		db_error_log "Mysql server is exits,please uninstall mysql server" >>$db_install_log
		exit 1
	fi
	
	if [ `rpm -qa | grep -i "MariaDB-client-10.2.11-1.el7.centos.x86_64" | wc -l` -eq 1 ]
	then
		echo "Mysql client is exits,please uninstall mysql client"
		db_error_log "Mysql client is exits,please uninstall mysql client" >>$db_install_log
		exit 1
	fi
}

installMysql()
{
       echo "begin install mysql "
	   echo "begin install mysql " >>$db_install_log
         
       rpm -ivh ../thirdpackage/mysql/*.rpm  --nodeps --force >>$db_install_log 2>>$db_install_log
      
       db_info_log "install mysql tool success" >>$db_install_log
}


#启动数据库
start_mysql()
{
	nStart=$(service mysql start | grep 'OK' | grep -v grep | wc -l)
	if [ ${nStart} -eq 1 ]
	then
		echo "Start mysql success"
		db_info_log "Start mysql success" >>$db_install_log
	else
		echo "Start mysql failed"
		db_error_log "Start mysql failed" >>$db_install_log
		exit 1
	fi
	
}


#停止数据库
stop_mysql()
{
	nStart=$(service mysql stop | grep 'OK' | grep -v grep | wc -l)
	if [ ${nStart} -eq 1 ]
	then
		echo "Stop mysql success"
		db_info_log "Stop mysql success" >>$db_install_log
	else
		echo "Stop mysql failed"
		db_error_log "Stop mysql failed" >>$db_install_log
	fi
	
}

#设置root用户密码
set_root_pwd()
{
	echo "begin set root user password"
	db_info_log "begin set root user password" >>$db_install_log
	#start_mysql
	#通过mysqld_safe跳过权限表grant-tables启动MySQL
	#mysqld_safe --user=mysql --skip-grant-tables --skip-networking 2>&1 >/dev/null &
	#sleep 10
	#if [ $? -ne 0 ]
	#then 
		 #echo "use mysqld_safe failed"
		 #db_error_log "use mysqld_safe failed" >>$db_install_log
		 #exit 1
	#fi
    start_mysql
mysql -uroot mysql <<EOF
use mysql;
UPDATE user SET Password=PASSWORD('${DBROOTPWD}') where USER='root' and host='root' or host='localhost';
FLUSH PRIVILEGES;
exit
EOF

	stop_mysql
	start_mysql
	
mysql -uroot -p${DBROOTPWD} -P3306 -h127.0.0.1 <<EOF
set password=PASSWORD("${DBROOTPWD}");
flush privileges;
exit
EOF
        #设置远程登录
	configRemoteLogin
	echo "set root user password success"
	db_info_log "set root user password success" >>$db_install_log
	
	createUser
}

configRemoteLogin()
{
	
mysql -uroot -p${DBROOTPWD}  <<EOF
use mysql;
update user set host ='%' where user ='root' and host="localhost";
update user set password =PASSWORD("${DBROOTPWD}") where user ='root';
update user set password_expired="N";
delete from user where USER='';
flush privileges;
exit
EOF

	echo "set root user remote login success"
	db_info_log "set root user remote login success" >>$db_install_log

}

#创建interest用户
createUser()
{
echo "begin create system user"
db_info_log "begin create system user" >>$db_install_log
mysql -uroot -p${DBROOTPWD}  <<EOF
use mysql;
create user multi_admin;
update user set password=PASSWORD("multi_admin") where user='multi_admin';
grant all privileges on *.* to multi_admin@"%" Identified by "multi_admin";
create user ${systemUser};
update user set password=PASSWORD("${systemPwd}") where user='${systemUser}';
grant all privileges on *.* to ${systemUser}@"%" Identified by "${systemPwd}";
flush privileges;
exit
EOF
echo "create system user success"
db_info_log "create system user success" >>$db_install_log
}

change_data_dir()
{
  echo "change myql data dir"
  db_info_log "change myql data dir" >>$db_install_log
  start_mysql
  stop_mysql
  
  cp -prf /var/lib/mysql/ ${mysqlDataDir}
  echo socket=${mysqlDataDir}/mysql/mysql.sock >> /etc/my.cnf
  #修改目录
  ln -s ${mysqlDataDir}/mysql/mysql.sock /var/lib/mysql/mysql.sock

  echo "change myql data dir success"
  db_info_log "change myql data dir success" >>$db_install_log
}


config_myconf()
{
	echo "config myconf"
	db_info_log "config myconf" >>$db_install_log
	
	# 如果文件文件夹不存在就创建文件夹
	if [ ! -d /srv/log  ];then
		mkdir /srv/log
	fi
	chmod 777 /srv/log
	cat >>/etc/my.cnf<<EOF
[mysqld]
max_connections = 2048
sort_buffer_size = 32M
read_buffer_size = 32M
key_buffer_size = 128M 
join_buffer_size = 32M
thread_cache_size = 51
query_cache_size = 32M
tmp_table_size = 96M
max_heap_table_size = 96M
slow_query_log = 1
slow_query_log_file = /srv/log/logs/slow.log
log-error=/srv/log/logs/error.log
long_query_time=1
datadir=/opt/mysql/data/mysql
lower_case_table_names=1 #1不区分大小写 0区分大小写
EOF

}

init_mysql()
{
    echo "create database"
	db_info_log "create database" >>$db_install_log
    eval "mysql -u${systemUser} -p${systemPwd} -e 'create database if not exists ${dbName} DEFAULT CHARSET=utf8;'"

    echo "begin create table"
    db_info_log "begin create table" >>$db_install_log
    
	for sqlname in $createTableFile/*.sql
    do
		db_info_log " begin run dbfile:${sqlname}" >>$db_install_log
	 
		mysql -h127.0.0.1 -P3306 -u${systemUser} -p${systemPwd} --default-character-set=utf8 ${dbName} < ${sqlname} 2>>$db_install_log
    done
    echo "init database"
	db_info_log "init database" >>$db_install_log
    
	for sqlname in $initTableFile/*.sql
    do
        db_info_log " begin run dbfile:${sqlname}.sql" >>$db_install_log

        mysql -h127.0.0.1 -P3306 -u${systemUser} -p${systemPwd} --default-character-set=utf8 ${dbName} < ${sqlname} 2>>$db_install_log
    done

}

echo "******** install mysql start `date "+%Y-%m-%d_%H:%M:%S"` *********" 
echo "******** install mysql start `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}

checkinstalldb

installMysql

#修改mysql的数据目录
change_data_dir

#配置mysql参数
config_myconf

#设置root用户密码
set_root_pwd

echo "init mysql `date "+%Y-%m-%d_%H:%M:%S"` "
echo "init mysql `date "+%Y-%m-%d_%H:%M:%S"` " >>${LOG_FILE}
init_mysql

echo "******** install mysql end `date "+%Y-%m-%d_%H:%M:%S"` *********" 
echo "******** install mysql end `date "+%Y-%m-%d_%H:%M:%S"` *********" >> ${LOG_FILE}
