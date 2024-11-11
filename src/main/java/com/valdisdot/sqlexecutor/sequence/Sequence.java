package com.valdisdot.sqlexecutor.sequence;

/**
 * Represents a regular SQL sequence with connection and database details.
 * Extends {@link BaseSequence} to include additional properties for connection identification and database name.
 */
public class Sequence extends BaseSequence {

    /** Identifier for the database connection. */
    private String connectionIdentifier;

    /** Name of the database associated with the sequence. */
    private String databaseName;

    /**
     * Sets the identifier for the database connection.
     *
     * @param connectionIdentifier the connection identifier to set;
     *                             if blank, it will be set to {@code null}
     */
    public void setConnectionIdentifier(String connectionIdentifier) {
        this.connectionIdentifier = connectionIdentifier.isBlank() ? null : connectionIdentifier;
    }

    /**
     * Retrieves the identifier for the database connection.
     *
     * @return the connection identifier, or {@code null} if not set
     */
    public String getConnectionIdentifier() {
        return connectionIdentifier;
    }

    /**
     * Sets the name of the database associated with the sequence.
     *
     * @param databaseName the database name to set;
     *                     if blank, it will be set to {@code null}
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName.isBlank() ? null : databaseName;
    }

    /**
     * Retrieves the name of the database associated with the sequence.
     *
     * @return the database name, or {@code null} if not set
     */
    public String getDatabaseName() {
        return databaseName;
    }

    @Override
    public String toString() {
        return "Sequence{" +
                "connectionIdentifier='" + connectionIdentifier + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", resultTable='" + resultTable + '\'' +
                ", body=" + body +
                '}';
    }
}
