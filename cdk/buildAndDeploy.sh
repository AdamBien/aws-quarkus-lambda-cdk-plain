#!/bin/sh
set -e
mvn clean package && cdk deploy