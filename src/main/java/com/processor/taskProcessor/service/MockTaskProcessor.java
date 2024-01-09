package com.processor.taskProcessor.service;

import com.processor.taskProcessor.service.redis.RedisRepository;
import com.processor.taskProcessor.config.TaskProcessorAppConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@Component
public class MockTaskProcessor {

    private final RedisRepository redisRepository;
    private final PatternMatchService patternMatchService;
    private final int loopsNum;

    public MockTaskProcessor(RedisRepository redisRepository, PatternMatchService patternMatchService, TaskProcessorAppConfig config) {
        this.redisRepository = redisRepository;
        this.patternMatchService = patternMatchService;
        this.loopsNum = config.getNumberOfLoops();
    }

    @Async("threadPoolTaskExecutor")
    public void processTaskAsynchronously(Long taskId, String input, String pattern) {
        String inputString = "Input: " + input + ", Pattern: " + pattern;
        IntStream.range(0, loopsNum).forEach(i -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            float percentage = (float) Math.floor((i + 1) * 100.0 / loopsNum);
            redisRepository.writeTaskToRedis(taskId, inputString + ";PROGRESS: " + percentage + "%;PROCESSING");
        });

        String result = patternMatchService.match(input, pattern);
        redisRepository.writeTaskToRedis(taskId, inputString + ";DONE;" + result);
        log.debug("Processing of task {} is done. Result: {}", taskId, result);
    }
}