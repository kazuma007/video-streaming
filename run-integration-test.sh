#!/bin/bash

echo "Starting Docker Compose..."
docker compose -f test-compose.yaml up -d

if [ $? -ne 0 ]; then
    echo "Failed to start Docker Compose"
    exit 1
fi

echo "Running Integration Tests..."
./gradlew integrationTest

echo "Stopping Docker Compose..."
docker compose -f test-compose.yaml down
