FROM java:8
ENV ARTIFACTID experiment
ENV ARTIFACTVERSION 1.0-SNAPSHOT
ENV HOME_PATH /home/experiment
WORKDIR $HOME_PATH
ADD /$ARTIFACTID-$ARTIFACTVERSION.jar $HOME_PATH/$ARTIFACTID-$ARTIFACTVERSION.jar
RUN sed -i "s/archive.ubuntu./mirrors.aliyun./g" /etc/apt/sources.list
RUN sed -i "s/deb.debian.org/mirrors.aliyun.com/g" /etc/apt/sources.list
RUN sed -i "s/security.debian.org/mirrors.aliyun.com\/debian-security/g" /etc/apt/sources.list
ENTRYPOINT ["java", "-jar", "experiment-1.0-SNAPSHOT.jar"]