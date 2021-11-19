#!/bin/sh
set -e
mvn clean package && cdk deploy --all --require-approval=never