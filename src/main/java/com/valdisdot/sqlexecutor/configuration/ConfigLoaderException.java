package com.valdisdot.sqlexecutor.configuration;

/**
 * Exception thrown when errors occur during the loading or processing of configuration files.
 */
public class ConfigLoaderException extends Exception {

    /**
     * Constructs a new {@code ConfigLoaderException} with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public ConfigLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
