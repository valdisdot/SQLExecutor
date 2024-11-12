package com.valdisdot.sqlexecutor.sequence.process.writer;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.process.SyntaxToken;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Utility class for saving a {@link SequenceHolder} to its original file.
 * Converts the {@link SequenceHolder} and its associated sequences into a structured
 * text format using predefined syntax tokens.
 */
public class SequenceWriter {

    /**
     * Writes the contents of a {@link SequenceHolder} to its origin file.
     *
     * <p>This method generates a structured text representation of the sequence holder,
     * including its header, snippets, sequences, and optional post-sequence.
     * The output is saved in the file specified by {@link SequenceHolder#getOrigin()}.</p>
     *
     * @param holder the {@link SequenceHolder} to be written to its origin file.
     * @throws SequenceWriterException if an error occurs while writing to the file,
     *                                 such as file not found or I/O exceptions.
     */
    public void writeSequenceHolder(SequenceHolder holder) throws SequenceWriterException {
        try (FileWriter writer = new FileWriter(holder.getOrigin())) {
            // Write header section
            writer
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.HEAD.token()).append("\n")
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.NAME.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(holder.getName()).append("\n")
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.IDENTIFIERS.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(holder.getIdentifiers().stream().collect(Collectors.joining(SyntaxToken.LIST_SEPARATOR.token()))).append("\n")
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.END.token()).append("\n\n");

            // Write snippets section
            writer
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.SNIPPETS.token()).append("\n")
                    .append(holder.getSnippetBody()).append("\n")
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.END.token()).append("\n\n");

            // Write each sequence
            for (Sequence sequence : holder.getSequences()) {
                writer
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.REGULAR_SEQUENCE.token()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.CONNECTION.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(sequence.getConnectionIdentifier()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.DATABASE.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(sequence.getDatabaseName()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.RESULT_TABLE.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(sequence.getResultTable()).append("\n")
                        .append(sequence.getBody().trim()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.END.token()).append("\n\n");
            }

            // Write post-sequence if present
            if (holder.hasPostSequence()) {
                PostSequence postSequence = holder.getPostSequence();
                writer
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.POST_SEQUENCE.token()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.RESULT_TABLE.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(postSequence.getResultTable()).append("\n")
                        .append(postSequence.getBody().trim()).append("\n")
                        .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.END.token()).append("\n");
            }

            writer.flush();
        } catch (FileNotFoundException e1) {
            throw new SequenceWriterException("Can't find a sequence holder file to write, file: " + holder.getOrigin(), e1);
        } catch (IOException e2) {
            throw new SequenceWriterException("Error during writing the sequence holder, file: " + holder.getOrigin(), e2);
        }
    }
}
