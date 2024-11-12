package com.valdisdot.sqlexecutor.sequence.process.parser;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceParserTest {
    @Test
    public void test_parsing_bad_file() throws URISyntaxException {
        List<File> badFiles = new LinkedList<>();
        badFiles.add(new File(ClassLoader.getSystemResource("wrong_files/no_head_script.txt").toURI()));
        badFiles.add(new File(ClassLoader.getSystemResource("wrong_files/no_sequence_body_script.txt.txt").toURI()));
        badFiles.add(new File(ClassLoader.getSystemResource("wrong_files/no_sequences_script.txt").toURI()));
        badFiles.add(new File(ClassLoader.getSystemResource("wrong_files/violate_head_syntax_script.txt.txt").toURI()));
        badFiles.add(new File(ClassLoader.getSystemResource("wrong_files/violate_sequence_syntax_script.txt.txt").toURI()));
        SequenceParser parser = new SequenceParser();
        badFiles.forEach(file -> assertThrows(SequenceParserException.class, () -> parser.parseSequenceHolder(file)));
    }

    @Test
    public void test_success_parsing() throws URISyntaxException {
        File file = new File(ClassLoader.getSystemResource("script.txt").toURI());
        SequenceParser parser = new SequenceParser();
        assertDoesNotThrow(() -> {
            SequenceHolder sequenceHolder = parser.parseSequenceHolder(file);
            assertEquals("sales report", sequenceHolder.getName());
            assertEquals(Set.of("sales", "report", "year"), sequenceHolder.getIdentifiers());
            assertEquals("customer_predicate: = 259\r\ndate_predicate: between '2024-01-01 00:00:00' and '2025-01-01 00:00'", sequenceHolder.getSnippetBody());
            List<Sequence> sequences = sequenceHolder.getSequences();
            assertNotNull(sequences);
            assertEquals(2, sequences.size());
            Sequence sequence = sequences.get(0);
            assertNotNull(sequences);
            assertEquals("slave", sequence.getConnectionIdentifier());
            assertEquals("sale", sequence.getDatabaseName());
            assertEquals("sales_b", sequence.getResultTable());
            assertEquals("select * from common_sale where customer_id ${prj_predicate} and dt ${date_predicate};\n", sequence.getBody());
            PostSequence postSequence = sequenceHolder.getPostSequence();
            assertNotNull(postSequence);
            assertEquals("report", postSequence.getResultTable());
            assertEquals("select * from (select * from sale_a union all select * from sale_b) res order by 1\n", postSequence.getBody());
        });
    }
}
