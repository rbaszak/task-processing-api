package com.processor.taskProcessor.service;

import com.processor.taskProcessor.domain.model.Task;
import com.processor.taskProcessor.adapter.out.redis.RedisRepository;
import com.processor.taskProcessor.domain.port.TaskServicePort;
import com.processor.taskProcessor.exception.RedisUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService implements TaskServicePort {

    private final RedisRepository redisRepository;
    private final MockTaskProcessor taskProcessor;

    private static final String REDIS_UNAVAILABLE_ERR_MSG = "Redis unavailable. Check your connection/docker.";

    private long taskIdCounter = 0L;

    public long createTask(String pattern, String input) {
        try {
            log.debug("Received task data. Pattern: {}, Input: {}", pattern, input);
            long taskId = taskIdCounter++;
            redisRepository.writeTaskToRedis(taskId, "Created.");
            taskProcessor.processTaskAsynchronously(taskId);
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
            return new Task(taskId, taskData);
        } catch (RedisConnectionFailureException e) {
            log.error(REDIS_UNAVAILABLE_ERR_MSG);
            throw new RedisUnavailableException(e);
        }
    }
}