package com.valdisdot.sqlexecutor.sequence.process;

/**
 * Enum representing syntax tokens used in sequence processing.
 * <p>
 * This enum defines constants and patterns for parsing specific components of
 * a sequence script. Tokens are used as markers or delimiters to identify
 * sections, variables, or content in the script text.
 * </p>
 */
public enum SyntaxToken {

    // Basic tokens
    /**
     * General token marker for parsing. Default is "##".
     */
    TOKEN("##"),

    /**
     * Separator for key-value pairs. Default is ":".
     */
    SEPARATOR(":"),

    /**
     * Separator for items in a list. Default is ",".
     */
    LIST_SEPARATOR(","),

    // Section tokens
    /**
     * Token marking the start of the header section.
     */
    HEAD("head"),

    /**
     * Token marking the name variable within the header.
     */
    NAME("name"),

    /**
     * Token marking the identifiers variable within the header.
     */
    IDENTIFIERS("identifiers"),

    /**
     * Token marking the snippet section.
     */
    SNIPPETS("snippets"),

    /**
     * Token marking a regular sequence section.
     */
    REGULAR_SEQUENCE("sequence"),

    /**
     * Token marking the connection variable within a sequence.
     */
    CONNECTION("connection"),

    /**
     * Token marking the database variable within a sequence.
     */
    DATABASE("database"),

    /**
     * Token marking the result table variable within a sequence or post-sequence.
     */
    RESULT_TABLE("result-table"),

    /**
     * Token marking the start of the post-sequence section.
     */
    POST_SEQUENCE("post-sequence"),

    /**
     * Token marking the end of a section.
     */
    END("end"),

    // Patterns for parsing sections
    /**
     * Pattern for capturing the header section.
     * Matches everything between "## head" and "## end".
     */
    SEQUENCE_HEAD_TOKEN("(?i)" + TOKEN.token + "\\s*" + HEAD.token + "(.*?)" + TOKEN.token + "\\s*" + END.token),

    /**
     * Pattern for capturing the snippet section.
     * Matches everything between "## snippets" and "## end".
     */
    SNIPPETS_TOKEN("(?is)" + TOKEN.token + "\\s*" + SNIPPETS.token + "\\s*(.*?)" + TOKEN.token + "\\s*" + END.token),

    /**
     * Pattern for capturing a regular sequence section.
     * Matches everything between "## sequence" and "## end".
     */
    REGULAR_SEQUENCE_TOKEN("(?i)" + TOKEN.token + "\\s*" + REGULAR_SEQUENCE.token + "(.*?)" + TOKEN.token + "\\s*" + END.token),

    /**
     * Pattern for capturing the post-sequence section.
     * Matches everything between "## post-sequence" and "## end".
     */
    POST_SEQUENCE_TOKEN("(?i)" + TOKEN.token + "\\s*" + POST_SEQUENCE.token + "(.*?)" + TOKEN.token + "\\s*" + END.token),

    // Patterns for specific variables
    /**
     * Pattern for capturing the name variable within the header.
     */
    NAME_TOKEN("(?i)" + TOKEN.token + "\\s*" + NAME.token + SEPARATOR.token + "\\s*(.*?)(?:\\r?\\n|$)"),

    /**
     * Pattern for capturing the identifiers variable within the header.
     */
    IDENTIFIERS_TOKEN("(?i)" + TOKEN.token + "\\s*" + IDENTIFIERS.token + SEPARATOR.token + "\\s*(.*?)(?:\\r?\\n|$)"),

    /**
     * Pattern for capturing the connection variable within a sequence.
     */
    CONNECTION_TOKEN("(?i)" + TOKEN.token + "\\s*" + CONNECTION.token + SEPARATOR.token + "\\s*(.*?)(?:\\r?\\n|$)"),

    /**
     * Pattern for capturing the database variable within a sequence.
     */
    DATABASE_TOKEN("(?i)" + TOKEN.token + "\\s*" + DATABASE.token + SEPARATOR.token + "\\s*(.*?)(?:\\r?\\n|$)"),

    /**
     * Pattern for capturing the result table variable within a sequence or post-sequence.
     */
    RESULT_TABLE_TOKEN("(?i)" + TOKEN.token + "\\s*" + RESULT_TABLE.token + SEPARATOR.token + "\\s*(.*?)(?:\\r?\\n|$)"),

    /**
     * Pattern for matching the prefix of a snippet variable. Default is "${".
     */
    SNIPPET_VARIABLE_PREFIX_TOKEN("\\$\\{\\s*"),

    /**
     * Token indicating the end of a snippet placeholder.
     * Example: `${variableName}`
     */
    SNIPPET_VARIABLE_POSTFIX_TOKEN("\\s*\\}"),

    /**
     * Token indicating the presence of a snippet variable.
     */
    SNIPPET_VARIABLE_ALL("(?s).*\\$\\{.*\\}.*"),

    /**
     * Token capturing lines that are not prefixed with a token identifier (e.g., ##).
     * Matches all non-tokenized lines in a sequence script.
     */
    LINES_TOKEN("(?m)^(?!##).*?(?:\\r?\\n|$)");

    private final String token;

    /**
     * Constructor to initialize the token with its regex or string representation.
     *
     * @param token the string or regex representation of the syntax token.
     */
    SyntaxToken(String token) {
        this.token = token;
    }

    /**
     * Retrieves the string or regex representation of this syntax token.
     *
     * @return the token value.
     */
    public String token() {
        return token;
    }
}

