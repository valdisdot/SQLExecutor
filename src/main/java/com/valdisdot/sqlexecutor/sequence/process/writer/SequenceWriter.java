package com.valdisdot.sqlexecutor.sequence.process.writer;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.process.SyntaxToken;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceBuildingException;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceHolderBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
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
                    .append(SyntaxToken.TOKEN.token()).append(" ").append(SyntaxToken.IDENTIFIERS.token()).append(SyntaxToken.SEPARATOR.token()).append(" ").append(holder.getIdentifiers().stream().collect(Collectors.joining(SyntaxToken.LIST_SEPARATOR.token() + " "))).append("\n")
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

    public void writeSequenceHolderSeed(File destination) throws SequenceWriterException {
        if(destination == null) throw new SequenceWriterException("Sequence holder seed destination file is null", new NullPointerException());
        if(destination.isDirectory()) throw new SequenceWriterException("Sequence holder seed destination file is a directory", new IllegalArgumentException());
        if(destination.exists() && destination.length() > 0) throw new SequenceWriterException("Sequence holder seed destination file already exists", new IllegalArgumentException());
        String name = "SEQUENCE_HOLDER_NAME_" + LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        List<String> identifiers = List.of("IDENTIFIER_1", "IDENTIFIER_2", "IDENTIFIER_3", "IDENTIFIER_N");
        String snippet = "SNIPPET_VALUE_1: VALUE\nSNIPPET_VALUE_2: VALUE\nSNIPPET_VALUE_N: VALUE";
        String connection = "CONNECTION_IDENTIFIER";
        String db = "DATABASE_NAME";
        String result = "RESULT_NAME";
        String sequence = "SEQUENCE BODY OPTIONALLY USING ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N}";
        String postSequence = "POST-SEQUENCE BODY OPTIONALLY USING ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N}";
        try {
            SequenceHolderBuilder builder = SequenceHolderBuilder.builder();
            builder
                    .name(name)
                    .origin(destination)
                    .identifiers(identifiers)
                    .snippet(snippet);
            builder.sequenceBuilder()
                    .connectionIdentifier(connection)
                    .databaseName(db)
                    .resultTable(result)
                    .bodyLine(sequence)
                    .applySequence();
            builder.postSequenceBuilder()
                    .resultTable(result + "_FINAL")
                    .bodyLine(postSequence)
                    .applyPostSequence();
            writeSequenceHolder(builder.build());
        } catch (SequenceBuildingException e) {
            throw new SequenceWriterException("Error during sequence holder seed building", e);
        }

        String howTo = String.format("\n" +
                "/*\n" +
                "[HEAD BLOCK] includes metadata about the script, such as its name and key identifiers, which are used in UI and final result file name.\n" +
                "Only one head block per file is allowed.\n" +
                "Replace %s with your own sequence holder name, IDENTIFIER_1, IDENTIFIER_2, IDENTIFIER_3, IDENTIFIER_N with your key identifiers.\n" +
                "\n" +
                "[SNIPPETS BLOCK] defines reusable variables and conditions for parameterizing SQL queries.\n" +
                "Meaning, values (the text after a semicolumns) of the variables will replace the variable tokens in next sequence bodies.\n" +
                "Otherwise, the VALUE will replace ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N} in the sequences.\n" +
                "For example, SEQUENCE BODY OPTIONALLY USING ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N} will be converted to SEQUENCE BODY OPTIONALLY USING VALUE, VALUE AND VALUE.\n" +
                "Only one snippet block per file is allowed.\n" +
                "Replace snippet with your own values.\n" +
                "\n" +
                "[SEQUNCE BLOCK] define SQL queries to be executed on specific database connections.\n" +
                "You must have specified the CONNECTION_IDENTIFIER and its databases (including DATABASE_NAME) in the connection.json file.\n" +
                "RESULT_NAME must be unique for all sequunces and post-sequence.\n" +
                "Multiple sequence blocks per file are allowed.\n" +
                "Replace CONNECTION_IDENTIFIER, DATABASE_NAME, RESULT_NAME and SEQUENCE BODY OPTIONALLY USING ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N} with your own values.\n" +
                "\n" +
                "[POST-SEQUENCE BLOCK] aggregates and manipulates results from previous sequence sections with internal temporary SQLite database (threating RESULT_NAMEs as SQLite database tables).\n" +
                "RESULT_NAME_FINAL must be unique for all sequunces and post-sequence.\n" +
                "Only one post-sequence block per file is allowed.\n" +
                "Replace RESULT_NAME_FINAL and POST-SEQUENCE BODY OPTIONALLY USING ${SNIPPET_VALUE_1}, ${SNIPPET_VALUE_2} AND ${SNIPPET_VALUE_N} with your own values.\n" +
                "\n" +
                "After saving, reload sequence holder through the menu.\n" +
                "\n" +
                "Details: https://github.com/valdisdot/SQLExecutor\n" +
                "*/", name);
        try (FileWriter writer = new FileWriter(destination, true)) {
            writer.write(howTo);
            writer.flush();
        } catch (IOException e) {
            throw new SequenceWriterException("Error during how-to comment appending to the sequence holder seed file", e);
        }
    }

    public static void main(String[] args) throws SequenceWriterException {
        new SequenceWriter().writeSequenceHolderSeed(new File("scripts/seed.txt"));
    }
}
