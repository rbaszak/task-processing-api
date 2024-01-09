package com.processor.taskProcessor.service;

import com.processor.taskProcessor.config.TaskProcessorAppConfig;
import com.processor.taskProcessor.domain.model.FuzzyMatchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
public class PatternMatchService {
    private int maxTypos = -1;

    public PatternMatchService(TaskProcessorAppConfig config) {
        if(config.getMaxTypos()!=null){
            this.maxTypos = config.getMaxTypos();
        }
    }

    public String match(String input, String pattern) {

        log.debug("Pattern length: {}", pattern.length());
        int maxAllowedTypos = pattern.length() - 1;

        if (maxTypos>=0) {
            maxAllowedTypos = maxTypos;
        }

        List<FuzzyMatchResult> allResults = IntStream.rangeClosed(0, input.length() - pattern.length())
                .mapToObj(i -> new FuzzyMatchResult(i, calculateFuzzyMatchResult(input.substring(i, i + pattern.length()), pattern)))
                .collect(Collectors.toList());

        log.debug("Found possible matches: {}", allResults);

        FuzzyMatchResult bestResult = findBestResult(allResults, maxAllowedTypos);

        if (bestResult.getPosition() >= 0) {
            log.debug("Found best possible match at position: {}", bestResult.getPosition());
            return "Position: " + bestResult.getPosition() + ", Typos: " + bestResult.getTypos();
        }
        else
            return "No match.";
    }

    private static int calculateFuzzyMatchResult(String substring, String pattern) {
        return LevenshteinDistance.getDefaultInstance().apply(substring, pattern);
    }

    private static FuzzyMatchResult findBestResult(List<FuzzyMatchResult> results, int maxAllowedTypos) {
        return results.stream()
                .filter(result -> result.getTypos() == 0 || result.getTypos() <= maxAllowedTypos)
                .min(Comparator.comparingInt(FuzzyMatchResult::getTypos))
                .orElse(new FuzzyMatchResult(-1, Integer.MAX_VALUE));
    }
}
