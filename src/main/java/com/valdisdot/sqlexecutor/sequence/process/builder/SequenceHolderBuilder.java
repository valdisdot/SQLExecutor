package com.valdisdot.sqlexecutor.sequence.process.builder;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * A builder class for constructing {@link SequenceHolder} instances and their parts, including sequences and post-sequences.
 * The builder ensures that all required properties are set and validates the resulting {@link SequenceHolder}.
 */
public class SequenceHolderBuilder {
    private SequenceHolder instance;

    /**
     * Private constructor to initialize the builder.
     */
    private SequenceHolderBuilder() {
        instance = new SequenceHolder();
    }

    /**
     * Creates a new {@link SequenceHolderBuilder}.
     *
     * @return a new instance of {@link SequenceHolderBuilder}
     */
    public static SequenceHolderBuilder builder() {
        return new SequenceHolderBuilder();
    }

    /**
     * Sets the origin file of the sequence holder.
     *
     * @param origin the origin file to set
     * @return the builder instance for method chaining
     * @throws SequenceBuildingException if the origin file is null, not a file, or does not exist
     */
    public SequenceHolderBuilder origin(File origin) throws SequenceBuildingException {
        if (origin == null) throw new SequenceBuildingException("Sequence holder origin file is null");
        if (origin.isDirectory() || !origin.exists())
            throw new SequenceBuildingException("Sequence holder origin file is not a file or does not exist");
        instance.setOrigin(origin);
        return this;
    }

    /**
     * Sets the name of the sequence holder.
     *
     * @param name the name to set
     * @return the builder instance for method chaining
     * @throws SequenceBuildingException if the name is null or empty
     */
    public SequenceHolderBuilder name(String name) throws SequenceBuildingException {
        if (name == null || name.isBlank()) throw new SequenceBuildingException("Sequence holder name is empty");
        instance.setName(name);
        return this;
    }

    /**
     * Adds an identifier (tag) to the sequence holder.
     *
     * @param identifier the identifier to add
     * @return the builder instance for method chaining
     */
    public SequenceHolderBuilder identifier(String identifier) {
        if (identifier != null && !identifier.isBlank()) instance.addIdentifier(identifier);
        return this;
    }

    /**
     * Sets the snippet body for the sequence holder.
     *
     * @param snippetBody the snippet body to set
     * @return the builder instance for method chaining
     * @throws SequenceBuildingException if the snippet body is null or empty
     */
    public SequenceHolderBuilder snippet(String snippetBody) throws SequenceBuildingException {
        if (snippetBody == null || snippetBody.isBlank())
            throw new SequenceBuildingException("Sequence holder snippet is empty");
        instance.setSnippetBody(snippetBody);
        return this;
    }

    /**
     * Provides a builder for creating a {@link Sequence}.
     *
     * @return a new {@link SequenceBuilder} instance
     */
    public SequenceBuilder sequenceBuilder() {
        return new SequenceBuilder();
    }

    /**
     * Provides a builder for creating a {@link PostSequence}.
     *
     * @return a new {@link PostSequenceBuilder} instance
     */
    public PostSequenceBuilder postSequenceBuilder() {
        return new PostSequenceBuilder();
    }

    /**
     * Builds and validates the {@link SequenceHolder} instance.
     *
     * @return the fully constructed {@link SequenceHolder} instance
     * @throws SequenceBuildingException if the required fields are not set or validation fails
     */
    public SequenceHolder build() throws SequenceBuildingException {
        if (instance == null) throw new SequenceBuildingException("Sequence holder has already been built");
        if (instance.getOrigin() == null) throw new SequenceBuildingException("Sequence holder origin file is not set");
        if (instance.getName() == null) throw new SequenceBuildingException("Sequence holder name is not set");
        if (instance.getIdentifiers().isEmpty())
            throw new SequenceBuildingException("Sequence holder has no identifiers");
        if (instance.getSequences().isEmpty()) throw new SequenceBuildingException("Sequence holder has no sequences");
        List<String> resultIdentifiers = new LinkedList<>();
        if (instance.hasPostSequence()) resultIdentifiers.add(instance.getPostSequence().getResultTable());
        instance.getSequences().stream().map(Sequence::getResultTable).forEach(resultIdentifiers::add);
        if (instance.getSequences().size() + (instance.hasPostSequence() ? 1 : 0) != new HashSet<>(resultIdentifiers).size())
            throw new SequenceBuildingException("Sequence holder has non-unique result table names in its sequences");
        try {
            return instance;
        } finally {
            instance = null;
        }
    }

    /**
     * Builder for creating a {@link Sequence}.
     */
    public class SequenceBuilder {
        private Sequence instance = new Sequence();

        private SequenceBuilder() {
        }

        /**
         * Sets the connection identifier for the sequence.
         *
         * @param connectionIdentifier the connection identifier to set
         * @return the builder instance for method chaining
         * @throws SequenceBuildingException if the connection identifier is null or empty
         */
        public SequenceBuilder connectionIdentifier(String connectionIdentifier) throws SequenceBuildingException {
            if (connectionIdentifier == null || connectionIdentifier.isBlank())
                throw new SequenceBuildingException("Sequence connection identifier is empty");
            instance.setConnectionIdentifier(connectionIdentifier);
            return this;
        }

        /**
         * Sets the database name for the sequence.
         *
         * @param databaseName the database name to set
         * @return the builder instance for method chaining
         * @throws SequenceBuildingException if the database name is null or empty
         */
        public SequenceBuilder databaseName(String databaseName) throws SequenceBuildingException {
            if (databaseName == null || databaseName.isBlank())
                throw new SequenceBuildingException("Sequence database name is empty");
            instance.setDatabaseName(databaseName);
            return this;
        }

        /**
         * Sets the result table for the sequence.
         *
         * @param resultTable the result table to set
         * @return the builder instance for method chaining
         * @throws SequenceBuildingException if the result table is null or empty
         */
        public SequenceBuilder resultTable(String resultTable) throws SequenceBuildingException {
            if (resultTable == null || resultTable.isBlank())
                throw new SequenceBuildingException("Sequence result table is empty");
            instance.setResultTable(resultTable);
            return this;
        }

        /**
         * Adds a line to the sequence body.
         *
         * @param line the line to add
         * @return the builder instance for method chaining
         */
        public SequenceBuilder bodyLine(String line) {
            instance.addLine(line);
            return this;
        }

        /**
         * Applies the built {@link Sequence} to the {@link SequenceHolderBuilder}.
         *
         * @return the {@link SequenceHolderBuilder} instance for method chaining
         * @throws SequenceBuildingException if the sequence is not properly built
         */
        public SequenceHolderBuilder applySequence() throws SequenceBuildingException {
            if (instance == null) throw new SequenceBuildingException("Sequence has already been built");
            if (instance.getConnectionIdentifier() == null || instance.getConnectionIdentifier().isBlank())
                throw new SequenceBuildingException("Sequence connection identifier is not set");
            if (instance.getDatabaseName() == null || instance.getDatabaseName().isBlank())
                throw new SequenceBuildingException("Sequence database name is not set");
            if (instance.getResultTable() == null || instance.getResultTable().isBlank())
                throw new SequenceBuildingException("Sequence result table is not set");
            if (instance.getBody().isBlank()) throw new SequenceBuildingException("Sequence body is not set");
            try {
                SequenceHolderBuilder.this.instance.addSequence(instance);
                return SequenceHolderBuilder.this;
            } finally {
                instance = null;
            }
        }
    }

    /**
     * Builder for creating a {@link PostSequence}.
     */
    public class PostSequenceBuilder {
        private PostSequence instance = new PostSequence();

        private PostSequenceBuilder() {
        }

        /**
         * Sets the result table for the post-sequence.
         *
         * @param resultTable the result table to set
         * @return the builder instance for method chaining
         * @throws SequenceBuildingException if the result table is null or empty
         */
        public PostSequenceBuilder resultTable(String resultTable) throws SequenceBuildingException {
            if (resultTable == null || resultTable.isBlank())
                throw new SequenceBuildingException("Post-sequence result table is empty");
            instance.setResultTable(resultTable);
            return this;
        }

        /**
         * Adds a line to the post-sequence body.
         *
         * @param line the line to add
         * @return the builder instance for method chaining
         */
        public PostSequenceBuilder bodyLine(String line) {
            instance.addLine(line);
            return this;
        }

        /**
         * Applies the built {@link PostSequence} to the {@link SequenceHolderBuilder}.
         *
         * @return the {@link SequenceHolderBuilder} instance for method chaining
         * @throws SequenceBuildingException if the post-sequence is not properly built
         */
        public SequenceHolderBuilder applyPostSequence() throws SequenceBuildingException {
            if (instance == null) throw new SequenceBuildingException("Post-sequence has already been built");
            if (instance.getBody().isBlank()) throw new SequenceBuildingException("Post-sequence body is not set");
            if (instance.getResultTable() == null || instance.getResultTable().isBlank())
                throw new SequenceBuildingException("Post-sequence result table is not set");
            try {
                SequenceHolderBuilder.this.instance.setPostSequence(instance);
                return SequenceHolderBuilder.this;
            } finally {
                instance = null;
            }
        }
    }
}
