package com.processor.taskProcessor.adapter.in;

import com.processor.taskProcessor.domain.model.Task;
import com.processor.taskProcessor.service.TaskService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/create")
    public ResponseEntity<Long> createTask(@RequestBody Map<String, String> request) {
        String pattern = request.get("pattern");
        String input = request.get("input");
        long taskId = taskService.createTask(pattern, input);
        return new ResponseEntity<>(taskId, HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Task>> listTasks() {
        List<Task> tasks = taskService.listTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity<String> checkTaskStatusAndResult(@RequestBody Map<String, Long> request) {
        Long taskId = request.get("taskId");
        Task task = taskService.checkTaskStatus(taskId);
        return new ResponseEntity<>(task.toString(), HttpStatus.OK);
    }
}