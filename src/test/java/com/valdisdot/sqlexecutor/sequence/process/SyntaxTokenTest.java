package com.valdisdot.sqlexecutor.sequence.process;

import org.junit.jupiter.api.Test;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SyntaxTokenTest {
    @Test
    public void test_sequence_token_matching(){
        assertTrue(Pattern.compile(SyntaxToken.SEQUENCE_HEAD_TOKEN.token(), Pattern.DOTALL).matcher("## head\n## end").matches());
        assertTrue(Pattern.compile(SyntaxToken.SNIPPETS_TOKEN.token(), Pattern.DOTALL).matcher("## snippets\n## end").matches());
        assertTrue(Pattern.compile(SyntaxToken.REGULAR_SEQUENCE_TOKEN.token(), Pattern.DOTALL).matcher("## sequence\n## end").matches());
        assertTrue(Pattern.compile(SyntaxToken.POST_SEQUENCE_TOKEN.token(), Pattern.DOTALL).matcher("## post-sequence\n## end").matches());
        assertTrue(Pattern.compile(SyntaxToken.NAME_TOKEN.token(), Pattern.DOTALL).matcher("## name:").matches());
        assertTrue(Pattern.compile(SyntaxToken.IDENTIFIERS_TOKEN.token(), Pattern.DOTALL).matcher("## identifiers:").matches());
        assertTrue(Pattern.compile(SyntaxToken.CONNECTION_TOKEN.token(), Pattern.DOTALL).matcher("## connection:").matches());
        assertTrue(Pattern.compile(SyntaxToken.DATABASE_TOKEN.token(), Pattern.DOTALL).matcher("## database:").matches());
        assertTrue(Pattern.compile(SyntaxToken.RESULT_TABLE_TOKEN.token(), Pattern.DOTALL).matcher("## result-table:").matches());
    }
}
