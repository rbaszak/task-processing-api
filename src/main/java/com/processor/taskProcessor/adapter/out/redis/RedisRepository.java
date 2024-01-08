package com.processor.taskProcessor.adapter.out.redis;

import com.processor.taskProcessor.domain.model.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String TASK_CREATED_MSG = "Created new task with ID: {}";
    private static final String TASKS_RETRIEVED_MSG = "Retrieved all tasks from Redis: {}";
    private static final String SINGLE_TASK_RETRIEVED_MSG = "Retrieved task with ID: {} from Redis. Task data: {}";

    private static final String KEYSPACE = "tasks";
    private static final String LAST_TASK_ID_KEYSPACE = "lastTaskId";


    public void writeTaskToRedis(Long taskId, String taskData) {
        HashOperations<String, Long, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(KEYSPACE, taskId, taskData);
        redisTemplate.opsForValue().set(LAST_TASK_ID_KEYSPACE, taskId.toString());
        log.debug(TASK_CREATED_MSG, taskId);
    }

    public List<Task> readAllTasksFromRedis() {
        List<Task> tasksList = new ArrayList<>();
        HashOperations<String, Long, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.entries(KEYSPACE).forEach((taskId, taskData) -> tasksList.add(new Task(taskId, taskData)));
        log.debug(TASKS_RETRIEVED_MSG, tasksList);
        return tasksList;
    }

    public String readTaskFromRedis(long taskId) {
        HashOperations<String, Long, String> hashOperations = redisTemplate.opsForHash();
        String taskData = hashOperations.get(KEYSPACE, taskId);
        log.debug(SINGLE_TASK_RETRIEVED_MSG, taskId, taskData);
        return taskData;
    }

    public Long readLastTaskFromRedis() {
        String lastTaskId = (String) redisTemplate.opsForValue().get(LAST_TASK_ID_KEYSPACE);
        return Optional.ofNullable(lastTaskId)
                .filter(str -> !str.isEmpty())
                .map(Long::parseLong)
                .orElse(0L);
    }
}