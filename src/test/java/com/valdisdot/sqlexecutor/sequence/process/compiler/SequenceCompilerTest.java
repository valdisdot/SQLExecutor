package com.valdisdot.sqlexecutor.sequence.process.compiler;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParser;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceCompilerTest {
    @Test
    public void test_compiler_throws_exception() throws URISyntaxException, SequenceParserException {
        SequenceCompiler compiler = new SequenceCompiler();
        SequenceHolder holder1 = new SequenceParser().parseSequenceHolder(new File(ClassLoader.getSystemResource("wrong_files/bad_snippet_syntax.txt").toURI()));
        assertThrows(SequenceCompilerException.class, () -> compiler.compileSequenceHolder(holder1));
        SequenceHolder holder2 = new SequenceParser().parseSequenceHolder(new File(ClassLoader.getSystemResource("wrong_files/unresolved_snippet.txt").toURI()));
        assertThrows(SequenceCompilerException.class, () -> compiler.compileSequenceHolder(holder2));
    }

    @Test
    public void test_successful_compile() throws URISyntaxException, SequenceParserException {
        SequenceHolder holder = new SequenceParser().parseSequenceHolder(new File(ClassLoader.getSystemResource("script_2.txt").toURI()));
        assertDoesNotThrow(() -> {
            SequenceQueue queue = new SequenceCompiler().compileSequenceHolder(holder);
            assertEquals("test", queue.getSequenceName());

            assertEquals("db_1", queue.nextConnection());
            assertEquals("db_1", queue.nextDatabase());
            assertEquals("res_db_1", queue.nextResultIdentifier());
            assertEquals("select * from (select * from table_1 union all select * from table_2 union all select * from table_2) as res where dt > '2024-01-15 00:00:00';".replaceAll("\\s", ""), queue.nextSequence().replaceAll("\\s", ""));

            assertEquals("db_2", queue.nextConnection());
            assertEquals("db_2", queue.nextDatabase());
            assertEquals("res_db_2", queue.nextResultIdentifier());
            assertEquals("select * from (select * from table_1 union all select * from table_2 union all select * from table_2) as res where dt > '2024-01-15 00:00:00';".replaceAll("\\s", ""), queue.nextSequence().replaceAll("\\s", ""));

            assertEquals("report", queue.getPostSequenceResultIdentifier());
            assertEquals("select * from (select * from sale_a union all select * from sale_b) res order by 1".replaceAll("\\s", ""), queue.getPostSequenceBody().replaceAll("\\s", ""));
        });
    }
}
