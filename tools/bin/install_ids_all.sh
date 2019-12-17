#!/bin/bash

echo "============== start install `date "+%Y-%m-%d_%H:%M:%S"` ============="
echo "============== start install `date "+%Y-%m-%d_%H:%M:%S"` =============" > ./install.log
./install_jdk.sh

./install_mysql.sh

./install_ids.sh

./install_redis.sh

./install_nginx.sh

./mark_version_info.sh

echo "============== end install `date "+%Y-%m-%d_%H:%M:%S"` ============="
echo "============== end install `date "+%Y-%m-%d_%H:%M:%S"` =============" >> ./install.log
