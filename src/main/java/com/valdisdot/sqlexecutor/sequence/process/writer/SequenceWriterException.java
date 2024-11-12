package com.valdisdot.sqlexecutor.sequence.process.writer;

/**
 * Exception thrown during the sequence writing process in {@link SequenceWriter}.
 *
 * <p>This exception indicates that an error occurred while saving a
 * {@link com.valdisdot.sqlexecutor.sequence.SequenceHolder} to a file, such as file access issues or I/O failures.</p>
 */
public class SequenceWriterException extends Exception {

    /**
     * Constructs a new {@code SequenceWriterException} with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause   the underlying cause of the exception.
     */
    public SequenceWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
