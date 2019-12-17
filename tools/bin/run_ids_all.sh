#! /bin/bash



service mysql start

./run_redis.sh

./run_nginx.sh

./run_dev.sh
sleep 1
./run_biz.sh
sleep 1