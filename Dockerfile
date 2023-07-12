# docker run --rm -d -p 8080:8080 --name trino trinodb/trino
FROM trinodb/trino:407
RUN mkdir /usr/lib/trino/plugin/rbacaccesscontrol
COPY target/trino-systemaccesscontrol-0.1.jar /usr/lib/trino/plugin/rbacaccesscontrol/trino-systemaccesscontrol-0.1.jar
COPY src/main/resources/rbac.properties /etc/trino/rbac.properties
