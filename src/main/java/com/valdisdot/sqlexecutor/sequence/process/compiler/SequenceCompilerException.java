package com.valdisdot.sqlexecutor.sequence.process.compiler;

/**
 * Exception thrown to indicate an error during the compilation of a sequence holder.
 */
public class SequenceCompilerException extends Exception {

    /**
     * Constructs a {@code SequenceCompilerException} with the specified detail message.
     *
     * @param message the detail message describing the error.
     */
    public SequenceCompilerException(String message) {
        super(message);
    }
}
