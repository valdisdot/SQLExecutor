package com.valdisdot.sqlexecutor.executor.writer;

public class ResultWriterException extends Exception {
    public ResultWriterException(String message) {
        super(message);
    }

    public ResultWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
