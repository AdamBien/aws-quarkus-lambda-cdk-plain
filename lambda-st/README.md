# System Test / Black Box Tests

To perform black box tests execute:

mvn -Dbase_uri/mp-rest/url=https://[GENERATED_ID].execute-api.eu-central-1.amazonaws.com clean test-compile failsafe:integration-test

For System Test executions in the pipeline, use:
BASE_URI_MP_REST_URL=https://[GENERATED_ID].execute-api.eu-central-1.amazonaws.com 