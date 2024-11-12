package com.valdisdot.sqlexecutor.sequence.process.parser;

import java.io.File;

/**
 * Exception thrown during the parsing of a {@link com.valdisdot.sqlexecutor.sequence.SequenceHolder}.
 * This exception is used to indicate issues encountered while processing a sequence script file.
 */
public class SequenceParserException extends Exception {

    /**
     * Constructs a new {@code SequenceParserException} with the specified detail message,
     * the origin file where the error occurred, and the root cause of the exception.
     *
     * @param message the detail message describing the error.
     * @param origin  the file where the parsing error occurred.
     * @param cause   the cause of the exception.
     */
    public SequenceParserException(String message, File origin, Throwable cause) {
        super(message + ", file: " + origin, cause);
    }

    /**
     * Constructs a new {@code SequenceParserException} with the specified detail message
     * and the origin file where the error occurred.
     *
     * @param message the detail message describing the error.
     * @param origin  the file where the parsing error occurred.
     */
    public SequenceParserException(String message, File origin) {
        super(message + ", file: " + origin);
    }
}
