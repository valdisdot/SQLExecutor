package com.valdisdot.sqlexecutor.executor;

public class SequenceExecutorException extends Exception {
    public SequenceExecutorException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceExecutorException(String message) {
        super(message);
    }
}
