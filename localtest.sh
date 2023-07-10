mvn clean package -X -Dcheckstyle.skip
docker build -t hpdevelop/trino:407-test-local .
docker run --rm hpdevelop/trino:407-test-local