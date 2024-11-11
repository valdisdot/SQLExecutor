package com.valdisdot.sqlexecutor.sequence;

/**
 * Base class representing a SQL sequence container.
 * Provides common properties and methods for managing sequence bodies and result table names.
 */
public abstract class BaseSequence {

    /** The name of the table or sheet in the result file. */
    protected String resultTable;

    /** The sequence body represented as a {@code StringBuilder}. */
    protected StringBuilder body;

    /**
     * Constructs a new {@code BaseSequence} with an empty sequence body.
     */
    protected BaseSequence() {
        body = new StringBuilder();
    }

    /**
     * Retrieves the name of the result table or sheet.
     *
     * @return the result table name, or {@code null} if not set
     */
    public String getResultTable() {
        return resultTable;
    }

    /**
     * Sets the name of the result table or sheet.
     *
     * @param resultTable the name to set; if blank, it will be set to {@code null}
     */
    public void setResultTable(String resultTable) {
        this.resultTable = resultTable.isBlank() ? null : resultTable;
    }

    /**
     * Retrieves the body of the sequence as a string.
     *
     * @return the sequence body
     */
    public String getBody() {
        return body.toString();
    }

    /**
     * Adds a line to the sequence body.
     *
     * @param line the line to add; if {@code null}, an empty line will be added
     */
    public void addLine(String line) {
        this.body.append(line == null ? "" : line).append("\n");
    }
}
