#! /bin/bash

set -e

sudo apt update
sudo apt install -y openjdk-17-jdk-headless

sudo rm /usr/lib/jvm/default-java
sudo ln -s /usr/lib/jvm/java-17-openjdk-amd64 /usr/lib/jvm/default-java

wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.8.6/apache-maven-3.8.6-bin.tar.gz

sudo tar xzf apache-maven-3.8.6-bin.tar.gz -C /opt
sudo ln -s /opt/apache-maven-3.8.6 /opt/maven

sudo touch /etc/profile.d/maven.sh

echo "export JAVA_HOME=/usr/lib/jvm/default-java" >>/etc/profile.d/maven.sh
echo "export M2_HOME=/opt/maven" >>/etc/profile.d/maven.sh
echo "export MAVEN_HOME=/opt/maven" >>/etc/profile.d/maven.sh
echo "export PATH=${M2_HOME}/bin:${PATH}" >>/etc/profile.d/maven.sh

source /etc/profile.d/maven.sh
