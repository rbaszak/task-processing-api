package com.processor.taskProcessor.service;

import com.processor.taskProcessor.domain.model.Task;
import com.processor.taskProcessor.adapter.out.redis.RedisRepository;
import com.processor.taskProcessor.domain.port.TaskServicePort;
import com.processor.taskProcessor.exception.RedisUnavailableException;
import com.processor.taskProcessor.exception.TaskNotExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService implements TaskServicePort {

    private final RedisRepository redisRepository;
    private final MockTaskProcessor taskProcessor;

    private static final String REDIS_UNAVAILABLE_ERR_MSG = "Redis unavailable. Check your connection/docker.";

    private long taskIdCounter = 0L;

    public TaskService(RedisRepository redisRepository, MockTaskProcessor mockTaskProcessor){
        this.redisRepository = redisRepository;
        this.taskProcessor = mockTaskProcessor;
        taskIdCounter = getLastTaskIdFromRedis();
    }

    public Long createTask(String input, String pattern) {
        try {
            log.debug("Received task data. Input: {}, Pattern: {}", input, pattern);
            Long taskId = ++taskIdCounter;
            redisRepository.writeTaskToRedis(taskId, "Created.");

            taskProcessor.processTaskAsynchronously(taskId, input, pattern);
            log.debug("Async task processing started.");

            return taskId;
        } catch (RedisConnectionFailureException e) {
            log.error(REDIS_UNAVAILABLE_ERR_MSG);
            throw new RedisUnavailableException(e);
        }
    }

    public List<Task> listTasks() {
        try {
            return redisRepository.readAllTasksFromRedis();
        } catch (RedisConnectionFailureException e) {
            log.error(REDIS_UNAVAILABLE_ERR_MSG);
            throw new RedisUnavailableException(e);
        }
    }

    public Task checkTaskStatus(long taskId) {
        try{
            String taskData = redisRepository.readTaskFromRedis(taskId);
            if (taskData == null) {
                throw new TaskNotExistsException(taskId);
            } else
                return new Task(taskId, taskData);
        } catch (RedisConnectionFailureException e) {
            log.error(REDIS_UNAVAILABLE_ERR_MSG);
            throw new RedisUnavailableException(e);
        }
    }

    private Long getLastTaskIdFromRedis() {
        try{
            Long lastId = redisRepository.readLastTaskFromRedis();
            log.debug("Last ID stored in Redis: {}", lastId);
            return lastId;
        } catch (RedisConnectionFailureException e) {
            log.error(REDIS_UNAVAILABLE_ERR_MSG);
            throw new RedisUnavailableException(e);
        }
    }
}