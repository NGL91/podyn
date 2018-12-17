#!/bin/bash
# Description: Startup scripts for podyn

set -euox pipefail

JMX_PORT=${JMX_PORT:-9999}
JMX_OPTS="-Dcom.sun.management.jmxremote.port=${JMX_PORT} -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS_DOCKER="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms64M"
set +u
JAVA_OPTS="$JAVA_OPTS $JAVA_OPTS_DOCKER"
if [[ -n $DATA_SYNC ]]; then
    DATA_OPTS="--data"
fi
set -u

TABLE=${TABLE:-Test_Table}
POSTGRES_JDBC_URL=${POSTGRES_JDBC_URL:-jdbc:postgresql://db:5432/podyn?currentSchema=public&user=podyn&password=podyn}
NUM_CONNECTIONS=${NUM_CONNECTIONS:-16}
SCAN_RATE=${SCAN_RATE:-25}
SCHEMA_NAME=${SCHEMA_NAME:-public}

# shellcheck disable=SC2086
exec java $JMX_OPTS \
          $JAVA_OPTS \
          -jar /app/podyn.jar \
          $DATA_OPTS \
          --changes \
          --table "${TABLE}" \
          --postgres-jdbc-url "${POSTGRES_JDBC_URL}" \
          --num-connections "${NUM_CONNECTIONS}" \
          --scan-rate "${SCAN_RATE}" \
          -sn "${SCHEMA_NAME}" \
          com.citusdata.migration.DynamoDBReplicator "$@"
