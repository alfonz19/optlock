#!/bin/bash

if [[ $# -ne 1 ]]; then
	echo supply uuid;
	exit 1;
fi

echo resetting
curl -X PUT http://localhost:8080/optlock/$1/?a=1\&b=1\&busyWait=false

echo entity before test:
curl -X GET http://localhost:8080/optlock/$1

echo waiting 5s before calling test.
sleep 5s;
echo testing.


curl -X PUT http://localhost:8080/optlock/$1/?a=2\&b=3\&busyWait=true &


echo entity after first PUT:
curl -X GET http://localhost:8080/optlock/$1

sleep 3s
curl -X PUT http://localhost:8080/optlock/$1/?a=200\&b=1\&busyWait=false 

echo entity after second PUT:
curl -X GET http://localhost:8080/optlock/$1

echo waiting for first job.
wait 


echo entity after first  PUT is finished:
curl -X GET http://localhost:8080/optlock/$1
