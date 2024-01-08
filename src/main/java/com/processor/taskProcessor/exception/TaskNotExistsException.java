package com.processor.taskProcessor.exception;

import org.springframework.dao.DataAccessResourceFailureException;

public class TaskNotExistsException extends DataAccessResourceFailureException {

    private static final String ERROR_MSG = "No data in Redis for ID: ";

    public TaskNotExistsException(Long id) {
        super(ERROR_MSG + id);
    }
}
