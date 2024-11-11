package com.valdisdot.sqlexecutor.sequence;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a container for a sequence, including its origin file, name, identifiers, script snippets,
 * sequences, and post-sequence. Used to organize and manage SQL sequences and associated metadata.
 */
public class SequenceHolder {

    /** The file origin of the sequence script. */
    private File origin;

    /** The name of the sequence, typically used for the result file name. */
    private String name;

    /** A set of identifiers (tags) associated with the sequence. */
    private final Set<String> identifiers;

    /** The snippet body, representing special variables or parts of the script. */
    private String snippetBody;

    /** A list of {@link Sequence} objects that represent the main sequences. */
    private final List<Sequence> sequences;

    /** A {@link PostSequence} to be executed after the main sequences. */
    private PostSequence postSequence;

    /**
     * Constructs a new {@code SequenceHolder} with empty sequences and identifiers.
     */
    public SequenceHolder() {
        sequences = new ArrayList<>();
        identifiers = new LinkedHashSet<>();
    }

    /**
     * Adds an identifier (tag) to the sequence holder.
     *
     * @param identifier the identifier to add
     */
    public void addIdentifier(String identifier) {
        this.identifiers.add(identifier);
    }

    /**
     * Adds a {@link Sequence} to the sequence holder.
     *
     * @param sequence the sequence to add
     */
    public void addSequence(Sequence sequence) {
        this.sequences.add(sequence);
    }

    /**
     * Retrieves the origin file of the sequence.
     *
     * @return the origin file
     */
    public File getOrigin() {
        return origin;
    }

    /**
     * Sets the origin file for the sequence.
     *
     * @param origin the file to set as the origin
     */
    public void setOrigin(File origin) {
        this.origin = origin;
    }

    /**
     * Retrieves the name of the sequence.
     *
     * @return the sequence name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name for the sequence.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the identifiers (tags) associated with the sequence.
     *
     * @return a set of identifiers
     */
    public Set<String> getIdentifiers() {
        return identifiers;
    }

    /**
     * Checks if the sequence holder contains a snippet body.
     *
     * @return {@code true} if the snippet body is not {@code null}, {@code false} otherwise
     */
    public boolean hasSnippetBody() {
        return snippetBody != null;
    }

    /**
     * Retrieves the snippet body, representing special variables or parts of the script.
     *
     * @return the snippet body
     */
    public String getSnippetBody() {
        return snippetBody;
    }

    /**
     * Sets the snippet body for the sequence holder.
     *
     * @param snippetBody the snippet body to set
     */
    public void setSnippetBody(String snippetBody) {
        this.snippetBody = snippetBody;
    }

    /**
     * Retrieves the list of {@link Sequence} objects in the sequence holder.
     *
     * @return a list of sequences
     */
    public List<Sequence> getSequences() {
        return sequences;
    }

    /**
     * Checks if a post-sequence is set for the sequence holder.
     *
     * @return {@code true} if a post-sequence is present, {@code false} otherwise
     */
    public boolean hasPostSequence() {
        return postSequence != null;
    }

    /**
     * Retrieves the post-sequence, if present.
     *
     * @return the {@link PostSequence} object, or {@code null} if not set
     */
    public PostSequence getPostSequence() {
        return postSequence;
    }

    /**
     * Sets the post-sequence for the sequence holder.
     *
     * @param postSequence the post-sequence to set
     */
    public void setPostSequence(PostSequence postSequence) {
        this.postSequence = postSequence;
    }
}
