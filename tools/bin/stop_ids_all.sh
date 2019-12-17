#! /bin/bash



service mysql stop

./stop_redis.sh

./stop_nginx.sh

./stop_biz.sh

./stop_dev.sh
