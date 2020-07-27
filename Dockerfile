FROM oracle/graalvm-ce:20.1.0-java11 as graalvm
RUN gu install native-image

COPY . /home/app/simplelocalize-cli
WORKDIR /home/app/simplelocalize-cli

RUN native-image --no-server -cp target/simplelocalize-cli-*.jar io.simplelocalize.cli.SimplelocalizeCliCommand

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/simplelocalize-cli/simplelocalize-cli /app/simplelocalize-cli
RUN chmod +x /app/simplelocalize-cli
ENTRYPOINT ["/app/simplelocalize-cli"]
