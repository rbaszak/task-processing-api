package com.processor.taskProcessor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Task {
    private long id;
    private String data;
    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", data='" + data + '\'' +
                '}';
    }
}