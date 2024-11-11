package com.valdisdot.sqlexecutor.sequence.process.builder;

/**
 * Exception thrown during the process of building a SequenceHolder via {@link SequenceHolderBuilder}.
 * This exception is used when invalid input or a failure occurs while constructing a sequence holder or its parts.
 */
public class SequenceBuildingException extends Exception {

    /**
     * Constructs a new {@link SequenceBuildingException} with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public SequenceBuildingException(String message) {
        super(message);
    }
}
