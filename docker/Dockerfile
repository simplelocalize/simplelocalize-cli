FROM openjdk:8-jre-slim
LABEL maintainer="jakub.pomykala@gmail.com"
ENV PATH                   $PATH:$JAVA_HOME/bin
ENV JAVA_OPTS              "-server -XX:+UseG1GC -XX:+UseStringDeduplication -XX:+OptimizeStringConcat -Dsun.net.inetaddr.ttl=60"
ENV HEAP_SPACE             "-Xms32m -Xmx128m"

WORKDIR /workspace
COPY /tmp_src /workspace/code
COPY /simplelocalize.jar /workspace/app.jar
COPY /simplelocalize.properties /workspace/simplelocalize.properties

CMD ["/bin/sh", "-c", "java $HEAP_SPACE $JAVA_OPTS -jar /workspace/app.jar /workspace/simplelocalize.properties"]
