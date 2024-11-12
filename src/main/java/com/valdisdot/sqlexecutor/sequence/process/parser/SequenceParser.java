package com.valdisdot.sqlexecutor.sequence.process.parser;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.process.SyntaxToken;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceBuildingException;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceHolderBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses a sequence script file or raw text to construct a {@link SequenceHolder} object.
 * Handles parsing of sequences, post-sequences, snippets, and associated metadata.
 */
public class SequenceParser {
    /**
     * Parses a {@link SequenceHolder} from a given file.
     *
     * @param file the file containing the sequence script
     * @return a {@link SequenceHolder} constructed from the file
     * @throws SequenceParserException if an error occurs while reading or parsing the file
     */
    public SequenceHolder parseSequenceHolder(File file) throws SequenceParserException {
        try {
            return parseSequenceHolder(Files.readString(Path.of(file.toURI())), file);
        } catch (IOException e) {
            throw new SequenceParserException("Error during reading the file", file, e);
        }
    }

    /**
     * Parses a {@link SequenceHolder} from raw text content and associates it with a file.
     *
     * @param rawText the raw text content of the sequence script
     * @param file the file containing the sequence script
     * @return a {@link SequenceHolder} constructed from the raw text
     * @throws SequenceParserException if an error occurs while parsing the text
     */
    public SequenceHolder parseSequenceHolder(String rawText, File file) throws SequenceParserException {
        SequenceHolderBuilder builder = SequenceHolderBuilder.builder();
        try {
            List<String> currentSections;
            List<String> currentSubsections;
            String currentSection;
            SequenceHolderBuilder.SequenceBuilder currentSequenceBuilder;
            SequenceHolderBuilder.PostSequenceBuilder currentPostSequenceBuilder;

            builder.origin(file);

            // Extract the head section
            currentSections = extractSections(rawText, Pattern.compile(SyntaxToken.SEQUENCE_HEAD_TOKEN.token(), Pattern.DOTALL), 1);
            if (currentSections.size() > 1) {
                throw new SequenceParserException("More than one head section is present", file);
            } else if (currentSections.isEmpty()) {
                throw new SequenceParserException("No head section is present", file);
            }
            currentSection = currentSections.get(0);

            // Extract name
            currentSections = extractSections(currentSection, Pattern.compile(SyntaxToken.NAME_TOKEN.token()), 1);
            if (currentSections.size() > 1) {
                throw new SequenceParserException("More than one name variable is present", file);
            } else if (currentSections.isEmpty()) {
                throw new SequenceParserException("No name variable is present", file);
            }
            builder.name(currentSections.get(0));

            // Extract identifiers
            currentSections = extractSections(currentSection, Pattern.compile(SyntaxToken.IDENTIFIERS_TOKEN.token()), 1);
            for (String identifiers : currentSections) {
                for (String identifier : identifiers.split(SyntaxToken.LIST_SEPARATOR.token())) {
                    builder.identifier(identifier);
                }
            }

            // Process snippets
            currentSections = extractSections(rawText, Pattern.compile(SyntaxToken.SNIPPETS_TOKEN.token()), 1);
            if (!currentSections.isEmpty()) {
                if (currentSections.size() > 1) {
                    throw new SequenceParserException("More than one snippet section is present", file);
                }
                builder.snippet(currentSections.get(0));
            }

            // Extract regular sequences
            currentSections = extractSections(rawText, Pattern.compile(SyntaxToken.REGULAR_SEQUENCE_TOKEN.token(), Pattern.DOTALL), 1);
            for (String sequenceSection : currentSections) {
                if (sequenceSection.isBlank()) {
                    throw new SequenceParserException("One of the sequence sections is empty", file);
                }
                currentSequenceBuilder = builder.sequenceBuilder();

                // Extract connection identifier
                currentSubsections = extractSections(sequenceSection, Pattern.compile(SyntaxToken.CONNECTION_TOKEN.token()), 1);
                if (currentSubsections.size() > 1) {
                    throw new SequenceParserException("More than one connection variable in a sequence section", file);
                } else if (currentSubsections.isEmpty()) {
                    throw new SequenceParserException("No connection variable in a sequence section", file);
                }
                currentSequenceBuilder.connectionIdentifier(currentSubsections.get(0));

                // Extract database name
                currentSubsections = extractSections(sequenceSection, Pattern.compile(SyntaxToken.DATABASE_TOKEN.token()), 1);
                if (currentSubsections.size() > 1) {
                    throw new SequenceParserException("More than one database variable in a sequence section", file);
                } else if (currentSubsections.isEmpty()) {
                    throw new SequenceParserException("No database variable in a sequence section", file);
                }
                currentSequenceBuilder.databaseName(currentSubsections.get(0));

                // Extract result table
                currentSubsections = extractSections(sequenceSection, Pattern.compile(SyntaxToken.RESULT_TABLE_TOKEN.token()), 1);
                if (currentSubsections.size() > 1) {
                    throw new SequenceParserException("More than one result table variable in a sequence section", file);
                } else if (currentSubsections.isEmpty()) {
                    throw new SequenceParserException("No result table variable in a sequence section", file);
                }
                currentSequenceBuilder.resultTable(currentSubsections.get(0));

                // Extract body lines
                currentSubsections = extractSections(sequenceSection, Pattern.compile(SyntaxToken.LINES_TOKEN.token(), Pattern.DOTALL), 0);
                for (String line : currentSubsections) {
                    currentSequenceBuilder.bodyLine(line);
                }
                builder = currentSequenceBuilder.applySequence();
            }

            // Extract post-sequence
            currentSections = extractSections(rawText, Pattern.compile(SyntaxToken.POST_SEQUENCE_TOKEN.token(), Pattern.DOTALL), 1);
            if (!currentSections.isEmpty()) {
                if (currentSections.size() > 1) {
                    throw new SequenceParserException("More than one post-sequence section is present", file);
                }
                currentSection = currentSections.get(0);
                currentPostSequenceBuilder = builder.postSequenceBuilder();

                // Extract result table
                currentSubsections = extractSections(currentSection, Pattern.compile(SyntaxToken.RESULT_TABLE_TOKEN.token()), 1);
                if (currentSubsections.size() > 1) {
                    throw new SequenceParserException("More than one result table variable in a post-sequence section", file);
                } else if (currentSubsections.isEmpty()) {
                    throw new SequenceParserException("No result table variable in a post-sequence section", file);
                }
                currentPostSequenceBuilder.resultTable(currentSubsections.get(0));

                // Extract body lines
                currentSubsections = extractSections(currentSection, Pattern.compile(SyntaxToken.LINES_TOKEN.token(), Pattern.DOTALL), 0);
                for (String line : currentSubsections) {
                    currentPostSequenceBuilder.bodyLine(line);
                }
                builder = currentPostSequenceBuilder.applyPostSequence();
            }

            return builder.build();
        } catch (SequenceBuildingException e) {
            throw new SequenceParserException("Error during building the sequence holder", file, e);
        } catch (Exception e) {
            throw new SequenceParserException("Unexpected error during building the sequence holder", file, e);
        }
    }

    /**
     * Extracts sections from the provided content based on the given regex pattern.
     *
     * @param content the text content to parse
     * @param pattern the regex pattern to match sections
     * @param group the regex group to extract
     * @return a list of matched sections
     */
    private List<String> extractSections(String content, Pattern pattern, int group) {
        Matcher matcher = pattern.matcher(content);
        List<String> sections = new ArrayList<>();
        while (matcher.find()) {
            sections.add(matcher.group(group).trim());
        }
        return sections;
    }
}
