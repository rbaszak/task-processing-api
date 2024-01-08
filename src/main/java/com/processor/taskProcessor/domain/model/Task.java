package com.processor.taskProcessor.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class Task {
    private long id;
    private String input;
    private String result;

    public Task(long id, String data) {
        this.id = id;
        String[] splitValues = data.split(";");

        if (splitValues.length >= 2) {
            this.input = splitValues[0];
            this.result = splitValues[1];
        } else {
            throw new IllegalArgumentException("Invalid data format: " + data);
        }
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Task class to JSON", e);
        }
    }
}