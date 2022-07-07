# 基础镜像
FROM  java:8
# author
MAINTAINER guanhc

EXPOSE 8081

# 挂载目录
VOLUME /tmp
# 复制jar文件到路径
COPY experiment-1.0-SNAPSHOT.jar /home/experiment/experiment-1.0-SNAPSHOT.jar
# 启动认证服务
ENTRYPOINT ["java","-jar","experiment-1.0-SNAPSHOT.jar"]