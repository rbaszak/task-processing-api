package com.processor.taskProcessor.exception;

import org.springframework.dao.DataAccessResourceFailureException;

public class RedisUnavailableException extends DataAccessResourceFailureException {

    private static final String ERROR_MSG = "Failed to connect to Redis.";

    public RedisUnavailableException(Throwable cause) {
        super(ERROR_MSG, cause);
    }
}
