mvn clean package -X -Dcheckstyle.skip
docker build -t hpdevelop/trino:407-test-4 .
docker push hpdevelop/trino:407-test-4