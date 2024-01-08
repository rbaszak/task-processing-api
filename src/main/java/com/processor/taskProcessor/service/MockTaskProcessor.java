package com.processor.taskProcessor.service;

import com.processor.taskProcessor.adapter.out.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class MockTaskProcessor {

    private final RedisRepository redisRepository;
    private static final int LOOPS_NUM = 8;

    @Async("threadPoolTaskExecutor")
    public void processTaskAsynchronously(Long taskId) {
        IntStream.range(0, LOOPS_NUM).forEach(i -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            float percentage = (float) Math.floor((i + 1) * 100.0 / LOOPS_NUM);
            redisRepository.writeTaskToRedis(taskId, "COMPLETION: " + percentage + "%");
        });
        redisRepository.writeTaskToRedis(taskId, "DONE.");
        log.debug("Processing of task {} DONE.", taskId);
    }
}