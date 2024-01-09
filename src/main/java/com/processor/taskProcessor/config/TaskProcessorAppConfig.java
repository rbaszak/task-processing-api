package com.processor.taskProcessor.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "taskprocessor")
public class TaskProcessorAppConfig {
    private int numberOfLoops = 0;
    private Integer maxTypos;
}
