#!/bin/bash
#$1: num repetitions
#$2..$*: start.sh parameters
#e.g.: ./runN.sh 1 20000 15

ARGS=""
x=$@
for i in ${x#*$1}
do
	ARGS="$ARGS $i"
done

for (( i = 0 ; i < $1; i++ ))
do
	./start.sh $ARGS
done

