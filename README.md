# REST API for Asynchronous Tasks Processing

A simple RESTful API for creating and processing tasks asynchronously.

### Task Creation:
To create a task, the API accepts two strings as parameters: the pattern and the input. This task creation endpoint generates a unique task ID and initiates the asynchronous processing.

``` txt
Input: ABCD, Pattern: BCD
```

### Task Processing Result:
The result of a processed task includes the position of the pattern within the input string and the number of typos. 

``` txt
Position: 1, Typos: 0
```

The API uses the Levenshtein Distance algorithm to calculate the difference between substrings and utilizes the Fuzzy String Matching algorithm to compare two similar strings.

### Caching with Redis:
To enhance performance and preserve task data across application restarts, the API uses Redis as a caching mechanism. Redis is utilized to store tasks, their results, and the last assigned task ID. This ensures that even after shutting down and restarting the application, all previously created tasks and IDs will be recovered.

## Table of Contents

- [First Steps](#first-steps)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
    - [Running the Application](#running-the-application)
    - [Application Properties](#application-properties)
- [API Documentation](#api-documentation)
    - [Create a Task](#create-a-task)
    - [Check Task Status](#check-task-status)
    - [List All Tasks](#list-all-tasks)
- [Postman Collection](#postman-collection)
- [cUrl Examples](#curl-examples)

## First Steps

### Prerequisites

- Java 17
- Gradle
- Redis

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/rbaszak/task-processing-api.git
   cd task-processing-api
      ```

2. Build the project:

   ```bash
   ./gradlew clean build
   ```

### Running the Application

Redis is needed for the application to start up properly. Project provides docker-compose.yml that starts Redis instance.

```yaml
version: '3'

services:
  redis:
    image: "redis:latest"
    container_name: "test-redis"
    ports:
      - "6379:6379"
    volumes:
      - ./redis-data:/data
    command: redis-server --appendonly yes
```

Run the following commands to start the application:

```bash
docker-compose up
./gradlew bootRun
```

Alternatively it's possible to use the following commands to run Redis Docker image in the background:

```bash
docker-compose up -d
./gradlew bootRun
```

The application will listen at [http://localhost:8080](http://localhost:8080).

### Application Properties

Application provides several custom properties in [application.yml](https://github.com/rbaszak/task-processing-api/blob/main/src/main/resources/application.yml) file which we can change:

```yaml
spring:
  redis:
    host: localhost  # Redis IP
    port: 6379       # Redis Port

async: # Default Async executor properties. Changing them will lower or raise performance
  executor:            
    core-pool-size: 2
    max-pool-size: 10 
    queue-capacity: 25

taskprocessor:
  number-of-loops: 8 # Number of mock loops. Each loop waits 1000ms. Defaults to 0
  max-typos:   # Empty or <0 defaults to length of provided pattern-1
```

## API Documentation

### Create a Task

Endpoint: `POST /tasks/create`

Create a new task by sending a JSON request with a string input:

```json
{
  "input": "ABCD",
  "pattern": "ABC"
}
```

The response will include a raw task ID.
```json
1
```

### Check Task Status

Endpoint: `POST /tasks/status`

Check the status of a task by sending a JSON request with the task ID:

```json
{
  "taskId": 1
}
```

The response will provide information about the task's progress and result.

```json
// Example 1
{
  "id": 23,
  "input": "Input: ABCD, Pattern: BCD",
  "status": "CREATED",
  "result": "AWAITING"
}

// Example 2
{
  "id": 19,
  "input": "Input: ABCD, Pattern: BCD",
  "status": "PROGRESS: 37.0%",
  "result": "PROCESSING"
}

// Example 3
{
  "id": 19,
  "input": "Input: ABCD, Pattern: BCD",
  "status": "DONE",
  "result": "Position: 1, Typos: 0"
}
```

### List All Tasks

Endpoint: `POST /tasks/list`

```json
{} // Empty body
```

Retrieve a list of all tasks.

```json
[
  {
    "id": 1,
    "input": "Input: ABCD, Pattern: BCD",
    "status": "DONE",
    "result": "Position: 1, Typos: 0"
  },
  {
    "id": 2,
    "input": "Input: ABCD, Pattern: BCD",
    "status": "PROGRESS: 50.0%",
    "result": "PROCESSING"
  },
  {
    "id": 3,
    "input": "Input: ABCD, Pattern: BCD",
    "status": "CREATED",
    "result": "AWAITING"
  }
]
```

## Postman Collection

Postman collection used to test the application is available as a JSON file from the [PostmanCollection.json](https://github.com/rbaszak/task-processing-api/blob/main/postman/TaskProcessorAppCollection.postman_collection.json) file in the project.

## cURL Examples

Sample requests to run in Bash/CMD:

```bash
# Sample 1
curl -X POST \
  http://localhost:8080/tasks/create \
  -H 'Content-Type: application/json' \
  -d '{"input": "ABCDEF", "pattern": "ABC"}'
  
# Sample 2
curl -X POST \
  http://localhost:8080/tasks/status \
  -H 'Content-Type: application/json' \
  -d '{"taskId": 1}'

# Sample 3
curl -X POST \
  http://localhost:8080/tasks/list \
  -H 'Content-Type: application/json' \
  -d '{}'
```