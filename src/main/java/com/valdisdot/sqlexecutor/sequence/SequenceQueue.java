package com.valdisdot.sqlexecutor.sequence;

import java.util.LinkedList;

/**
 * Represents a queue structure for managing sequences and their associated metadata.
 * <p>
 * This class provides functionality to queue sequences along with their connection identifiers,
 * database names, SQL bodies, and result identifiers. It also supports managing a post-sequence
 * with its result identifier.
 * </p>
 */
public class SequenceQueue {
    private LinkedList<String> connections = new LinkedList<>();
    private LinkedList<String> databases = new LinkedList<>();
    private LinkedList<String> sequences = new LinkedList<>();
    private LinkedList<String> resultIdentifiers = new LinkedList<>();
    private String postSequenceBody;
    private String postSequenceResultIdentifier;
    private String sequenceName;

    /**
     * Constructs a new {@code SequenceQueue} with the specified sequence name.
     *
     * @param sequenceName the name of the sequence.
     */
    public SequenceQueue(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    /**
     * Adds a sequence to the queue with its associated metadata.
     *
     * @param connection       the connection identifier for the sequence.
     * @param database         the database name for the sequence.
     * @param sequence         the SQL body of the sequence.
     * @param resultIdentifier the result identifier associated with the sequence.
     */
    public void addSequence(String connection, String database, String sequence, String resultIdentifier) {
        connections.add(connection);
        databases.add(database);
        sequences.add(sequence);
        resultIdentifiers.add(resultIdentifier);
    }

    /**
     * Adds a post-sequence and its result identifier.
     *
     * @param sequence         the SQL body of the post-sequence.
     * @param resultIdentifier the result identifier for the post-sequence.
     */
    public void addPostSequence(String sequence, String resultIdentifier) {
        this.postSequenceBody = sequence;
        this.postSequenceResultIdentifier = resultIdentifier;
    }

    /**
     * Gets the name of the sequence.
     *
     * @return the sequence name.
     */
    public String getSequenceName() {
        return sequenceName;
    }

    /**
     * Checks if there are any remaining sequences in the queue.
     *
     * @return {@code true} if there are sequences left in the queue; {@code false} otherwise.
     */
    public boolean hasNextSequence() {
        return !connections.isEmpty() &&
                connections.size() == databases.size() &&
                connections.size() == sequences.size() &&
                connections.size() == resultIdentifiers.size();
    }

    /**
     * Retrieves and removes the next connection identifier from the queue.
     *
     * @return the next connection identifier.
     */
    public String nextConnection() {
        return connections.poll();
    }

    /**
     * Retrieves and removes the next database name from the queue.
     *
     * @return the next database name.
     */
    public String nextDatabase() {
        return databases.poll();
    }

    /**
     * Retrieves and removes the next SQL body of the sequence from the queue.
     *
     * @return the next SQL body of the sequence.
     */
    public String nextSequence() {
        return sequences.poll();
    }

    /**
     * Retrieves and removes the next result identifier from the queue.
     *
     * @return the next result identifier.
     */
    public String nextResultIdentifier() {
        return resultIdentifiers.poll();
    }

    /**
     * Checks if there is a post-sequence present.
     *
     * @return {@code true} if a post-sequence is present; {@code false} otherwise.
     */
    public boolean hasPostSequence() {
        return postSequenceBody != null && postSequenceResultIdentifier != null;
    }

    /**
     * Gets the SQL body of the post-sequence.
     *
     * @return the post-sequence SQL body.
     */
    public String getPostSequenceBody() {
        return postSequenceBody;
    }

    /**
     * Gets the result identifier of the post-sequence.
     *
     * @return the post-sequence result identifier.
     */
    public String getPostSequenceResultIdentifier() {
        return postSequenceResultIdentifier;
    }

    /**
     * Converts the sequence queue to a SQL canonical string representation.
     *
     * <p>The representation includes each sequence and its metadata as comments,
     * followed by the SQL body. If a post-sequence is present, it is appended at the end.</p>
     *
     * @return a SQL canonical string representation of the sequence queue.
     */
    public String toSQLCannonicalString() {
        StringBuilder builder = new StringBuilder();
        if (hasNextSequence()) {
            for (int i = 0; i < connections.size(); ++i) {
                builder
                        .append("/*\n")
                        .append("sequence: #").append(i + 1).append("\n")
                        .append("connection: ").append(connections.get(i)).append("\n")
                        .append("database: ").append(databases.get(i)).append("\n")
                        .append("result identifier: ").append(resultIdentifiers.get(i)).append("\n")
                        .append("*/\n")
                        .append(sequences.get(i))
                        .append("\n");
            }
        }
        if (hasPostSequence()) {
            builder
                    .append("/*\n")
                    .append("post-sequence\n")
                    .append("result identifier: ").append(postSequenceResultIdentifier).append("\n")
                    .append("*/\n")
                    .append(postSequenceBody);
        }
        return builder.toString().trim();
    }
}
