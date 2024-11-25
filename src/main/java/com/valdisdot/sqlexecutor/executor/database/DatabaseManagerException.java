package com.valdisdot.sqlexecutor.executor.database;

public class DatabaseManagerException extends Exception {
    public DatabaseManagerException(String message) {
        super(message);
    }

    public DatabaseManagerException(String message, Exception cause) {
        super(message, cause);
    }
}
