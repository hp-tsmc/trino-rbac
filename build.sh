#!/bin/bash

set -e

version=13
mvn clean package -X -Dcheckstyle.skip
docker build -t hpdevelop/trino:407-test-${version} .
docker push hpdevelop/trino:407-test-${version}