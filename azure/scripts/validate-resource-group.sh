#!/bin/bash

set -e
if [ 'true' == $(cat rgResult.txt) ]; then
	echo 'ERROR: Resource group name already exists, you need go to portal azure and delete this resource group and execute this job again'
	exit 1
fi
