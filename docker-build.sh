#!/bin/sh
docker build . -t simplelocalize-cli-v2
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run -p 8080:8080 simplelocalize-cli-v2"
