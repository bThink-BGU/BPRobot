# syntax=docker/dockerfile:1

FROM ev3dev/ev3dev-buster-ev3-generic:latest

EXPOSE 22

WORKDIR /home/robot

RUN echo "robot ALL=(ALL) NOPASSWD: ALL" >> /etc/sudoers && \
	echo "deb http://deb.debian.org/debian stretch-backports main" >> /etc/apt/sources.list && \
	echo 'debconf debconf/frontend select Noninteractive' | debconf-set-selections && \
	apt -y --allow-releaseinfo-change update && \
	apt -y full-upgrade && \
	apt -y autoremove && \
	apt-get -t stretch-backports install -y ne maven openjdk-11-jdk-headless && \
	echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-armel" >> /etc/environment
	
RUN echo "alias ll='ls -al'">>.bashrc && \
    export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-armel && \
	git clone https://github.com/bThink-BGU/BPRobot.git && \
	cd BPRobot && \
	git checkout newimpl && \
	mvn compile && \
    mvn package -P"uber-jar" && \
	cd ../ && \
	chown -R robot BPRobot
	
CMD service ssh start && /bin/bash
