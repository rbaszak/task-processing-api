package com.processor.taskProcessor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatternMatchService {
    public String match(String pattern, String input) {

        int position = 0;
        int typos = 0;

        return "Position: " + position + ", Typos: " + typos;
    }
}
