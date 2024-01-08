package com.processor.taskProcessor.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.processor.taskProcessor")
public class TaskProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskProcessorApplication.class, args);
	}
}