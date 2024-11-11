package com.valdisdot.sqlexecutor.sequence;

/**
 * Represents a post-sequence for execution over the results of regular sequences.
 * Extends {@link BaseSequence} to reuse common properties and methods.
 */
public class PostSequence extends BaseSequence {

    /**
     * Constructs a new {@code PostSequence} with an empty sequence body.
     */
    public PostSequence() {
    }

    @Override
    public String toString() {
        return "PostSequence{" +
                "resultTable='" + resultTable + '\'' +
                ", body=" + body +
                '}';
    }
}
