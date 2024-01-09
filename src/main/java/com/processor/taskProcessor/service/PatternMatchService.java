package com.processor.taskProcessor.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PatternMatchService {
    public String match(String input, String pattern) {

        int position = 0;
        int typos = 0;

        position = findPatternPosition(input, pattern);

        if (position >= 0)
            return "Position: " + position + ", Typos: " + typos;
        else
            return "No match.";
    }

    private int findPatternPosition(String input, String pattern) {
        return input.indexOf(pattern);
    }
}
