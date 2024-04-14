#!/bin/bash
usage="Usage: ./attachLicense.sh folder license_file"
if [ $# -ne 2 ]
then
  echo $usage
  exit 0
fi
files=`find $1 -name *.java`
for file in $files
do
  echo $file
  cat $2 | cat - $file > /tmp/out && mv /tmp/out $file
done

