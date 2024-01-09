package com.processor.taskProcessor.adapter.in;

import com.processor.taskProcessor.domain.model.Task;
import com.processor.taskProcessor.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskEndpoint {

    private final TaskService taskService;

    private static final String NOT_NULL_MSG = "ID cannot be null";

    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        String pattern = request.get("pattern");
        Long taskId = taskService.createTask(input, pattern);
        log.info("Created task with ID: {}", taskId);
        return new ResponseEntity<>(taskId, HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Task>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        log.info("Retrieved all tasks.");
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity<String> checkTaskStatusAndResult(@RequestBody Map<String, Long> request) {
        Long taskId = request.get("taskId");

        if (taskId == null) {
            log.error(NOT_NULL_MSG);
            return new ResponseEntity<>(NOT_NULL_MSG, HttpStatus.BAD_REQUEST);
        }

        Task task = taskService.checkTaskStatus(taskId);
        log.info("Retrieved task with ID: {}", taskId);
        return new ResponseEntity<>(task.toString(), HttpStatus.OK);
    }
}