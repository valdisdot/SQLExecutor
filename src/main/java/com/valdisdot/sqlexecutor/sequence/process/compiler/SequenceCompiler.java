package com.valdisdot.sqlexecutor.sequence.process.compiler;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;
import com.valdisdot.sqlexecutor.sequence.process.SyntaxToken;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Compiles a {@link SequenceHolder} into a {@link SequenceQueue}, resolving snippet variables
 * and constructing sequences with their associated metadata.
 */
public class SequenceCompiler {

    /**
     * Compiles a {@link SequenceHolder} into a {@link SequenceQueue}.
     * <p>
     * This process includes:
     * <ul>
     *   <li>Resolving snippets into a map of variables.</li>
     *   <li>Replacing snippet variables in each sequence and post-sequence body.</li>
     *   <li>Adding sequences and the post-sequence (if present) into the queue.</li>
     * </ul>
     * </p>
     *
     * @param sequenceHolder the sequence holder to be compiled.
     * @return a {@link SequenceQueue} containing compiled sequences and post-sequences.
     * @throws SequenceCompilerException if snippet variables cannot be resolved or
     *                                   there is an error in snippet syntax.
     */
    public SequenceQueue compileSequenceHolder(SequenceHolder sequenceHolder) throws SequenceCompilerException {
        Map<String, String> snippets = sequenceHolder.hasSnippetBody() ? compileSnippetBody(sequenceHolder.getSnippetBody()) : Map.of();
        SequenceQueue queue = new SequenceQueue(sequenceHolder.getName());
        for (Sequence sequence : sequenceHolder.getSequences()) {
            queue.addSequence(
                    sequence.getConnectionIdentifier(),
                    sequence.getDatabaseName(),
                    compileSequenceBody(sequence.getBody(), snippets),
                    sequence.getResultTable()
            );
        }
        if (sequenceHolder.hasPostSequence()) {
            PostSequence postSequence = sequenceHolder.getPostSequence();
            queue.addPostSequence(
                    compileSequenceBody(postSequence.getBody(), snippets),
                    postSequence.getResultTable()
            );
        }
        return queue;
    }

    /**
     * Compiles a snippet body into a map of snippet variables.
     * <p>
     * Each snippet line should follow the format: `key:value`. The method extracts
     * keys and values, ensuring proper syntax.
     * </p>
     *
     * @param snippetBody the raw snippet body to be compiled.
     * @return a map of snippet variable tokens to their corresponding values.
     * @throws SequenceCompilerException if a snippet line does not conform to the expected syntax.
     */
    private Map<String, String> compileSnippetBody(String snippetBody) throws SequenceCompilerException {
        Map<String, String> compiledSnippet = new LinkedHashMap<>();
        if (snippetBody == null || snippetBody.isBlank()) return compiledSnippet;

        String[] lines = snippetBody.replace("\r", "").split("\n");
        for (String line : lines) {
            if (!line.isBlank()) {
                String[] snippetLine = line.trim().split(":", 2);
                if (snippetLine.length != 2)
                    throw new SequenceCompilerException(String.format("Bad snippet syntax in line '%s'", line));
                compiledSnippet.put(SyntaxToken.SNIPPET_VARIABLE_PREFIX_TOKEN.token() + snippetLine[0] + SyntaxToken.SNIPPET_VARIABLE_POSTFIX_TOKEN.token(), snippetLine[1].trim());
            }
        }
        return compiledSnippet;
    }

    /**
     * Replaces snippet variables in a sequence body using the provided map of snippet variables.
     * <p>
     * The method scans for unresolved snippet variables in the body and substitutes them with
     * corresponding values from the map. If unresolved variables remain, an exception is thrown.
     * </p>
     *
     * @param rawBody         the raw sequence body with potential snippet variables.
     * @param snippetVariables the map of snippet variable tokens and their values.
     * @return the sequence body with snippet variables resolved.
     * @throws SequenceCompilerException if unresolved snippet variables remain in the body.
     */
    private String compileSequenceBody(String rawBody, Map<String, String> snippetVariables) throws SequenceCompilerException {
        if (rawBody.matches(SyntaxToken.SNIPPET_VARIABLE_ALL.token())) {
            for (String regex : snippetVariables.keySet())
                rawBody = rawBody.replaceAll(regex, snippetVariables.get(regex));
            if (rawBody.matches(SyntaxToken.SNIPPET_VARIABLE_ALL.token()))
                throw new SequenceCompilerException("Sequence body has unresolved snippet variables");
        }
        return rawBody;
    }
}
