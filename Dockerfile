FROM oracle/graalvm-ce:20.1.0-java11 as graalvm
RUN gu install native-image

COPY . /home/app/simplelocalize-cli-v2
WORKDIR /home/app/simplelocalize-cli-v2

RUN native-image --no-server -cp target/simplelocalize-cli-v2-*.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/simplelocalize-cli-v2/simplelocalize-cli-v2 /app/simplelocalize-cli-v2
ENTRYPOINT ["/app/simplelocalize-cli-v2"]
