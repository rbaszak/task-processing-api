package com.processor.taskProcessor.domain.port;

import com.processor.taskProcessor.domain.model.Task;

import java.util.List;

public interface TaskServicePort {
    long createTask(String pattern, String input);
    List<Task> listTasks();
    Task checkTaskStatus(long taskId);
}