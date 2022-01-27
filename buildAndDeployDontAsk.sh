#!/bin/sh
set -e
echo "building functions"
cd lambda && mvn clean package
echo "building CDK"
cd ../cdk && mvn clean package && cdk deploy --all --require-approval=never