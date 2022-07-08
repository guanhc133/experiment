# 基础镜像
FROM  java:8
# author
MAINTAINER guanhc

EXPOSE 8081

# 挂载目录
VOLUME /tmp
# 复制jar文件到路径
# 启动认证服务
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/winstone9109439042255230481.jar"]