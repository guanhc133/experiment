# 基础镜像
FROM  java:8
# author
MAINTAINER guanhc

EXPOSE 8081

# 挂载目录
VOLUME /tmp
# 复制jar文件到路径
ADD ./experiment-1.0-SNAPSHOT.jar experiment-1.0-SNAPSHOT.jar
# 启动认证服务
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]