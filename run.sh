#!/bin/bash
cd Database
# build sql-server image
podman build -t opm-sql .
cd ../SEP490_G16_OPM/
# build tomcat image
podman build -t opm-web .
# stop containers
podman ps -q -f "name=opm" | xargs -r podman kill
# remove containers
podman rm -f --filter "name=opm" || true
# run tomcat
podman run -d --name opm-web -p 8080:8080 --network="host" opm-web
# run sql-server
podman run -d --name opm-sql -p 1433:1433 --network="host" opm-sql
