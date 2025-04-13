#!/bin/bash
cd Database
# build sql-server image
docker build -t opm-sql .
cd ../SEP490_G16_OPM/
# build tomcat image
docker build -t opm-web .
# stop containers
docker ps --filter "name=opm" -q | xargs -r docker kill || true
# remove containers
docker rm $(docker ps --filter "name=opm" -q -a) || true
# run tomcat
docker run -d --name opm-web -p 8080:8080 --network="host" opm-web
# run sql-server
docker run -d --name opm-sql -p 1433:1433 --network="host" opm-sql
docker system prune -f
