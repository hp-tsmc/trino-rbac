# Init Sandbox
```
./init.sh
```

## Java 17

```
sudo apt update 
sudo apt install -y openjdk-17-jdk-headless

sudo rm /usr/lib/jvm/default-java
sudo ln -s /usr/lib/jvm/java-17-openjdk-amd64 /usr/lib/jvm/default-java


wget https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.8.6/apache-maven-3.8.6-bin.tar.gz

sudo tar xzf apache-maven-3.8.6-bin.tar.gz -C /opt 
sudo ln -s /opt/apache-maven-3.8.6 /opt/maven 

sudo vi /etc/profile.d/maven.sh 

export JAVA_HOME=/usr/lib/jvm/default-java
export M2_HOME=/opt/maven
export MAVEN_HOME=/opt/maven
export PATH=${M2_HOME}/bin:${PATH}

source /etc/profile.d/maven.sh
```

## Git Initialization

### Setup User Identity
```
git config --global user.name "YOUR NAME"
git config --global user.email "YOUR EMAIL"
```

### TO Push Your Change
[Generate your personal token for git](https://stackoverflow.com/questions/68775869/support-for-password-authentication-was-removed-please-use-a-personal-access-to)