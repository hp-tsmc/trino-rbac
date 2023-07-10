mvn clean package -X -Dcheckstyle.skip
docker build -t hpdevelop/trino:407-test-5 .
docker push hpdevelop/trino:407-test-5