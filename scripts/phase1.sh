#!/bin/bash
#$1: listening port for Phase1TestServer
#$2: group id
#$3: IP Address of Phase1TestServer

java -cp ../bin:../../LSim-libraries/* recipes_service.Server $1 $2 $3 &
