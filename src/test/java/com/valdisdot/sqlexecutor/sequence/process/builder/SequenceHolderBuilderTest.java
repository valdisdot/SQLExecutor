package com.valdisdot.sqlexecutor.sequence.process.builder;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceHolderBuilderTest {
    @Test
    public void test_sequence_holder_builder_throws_exceptions() {
        SequenceHolderBuilder builder = SequenceHolderBuilder.builder();
        assertThrows(SequenceBuildingException.class, () -> builder.name(null));
        assertThrows(SequenceBuildingException.class, () -> builder.name(""));
        assertThrows(SequenceBuildingException.class, () -> builder.origin(null));
        File dir = new File("~test");
        dir.mkdir();
        assertThrows(SequenceBuildingException.class, () -> builder.origin(dir));
        assertThrows(SequenceBuildingException.class, () -> builder.origin(new File(dir, "file.txt")));
        dir.delete();
        assertThrows(SequenceBuildingException.class, () -> builder.snippet(null));
        assertThrows(SequenceBuildingException.class, () -> builder.snippet(""));
    }

    @Test
    public void test_sequence_builder_throws_exception() {
        SequenceHolderBuilder.SequenceBuilder builder = SequenceHolderBuilder.builder().sequenceBuilder();
        assertThrows(SequenceBuildingException.class, () -> builder.connectionIdentifier(null));
        assertThrows(SequenceBuildingException.class, () -> builder.connectionIdentifier(""));
        assertThrows(SequenceBuildingException.class, () -> builder.databaseName(null));
        assertThrows(SequenceBuildingException.class, () -> builder.databaseName(""));
        assertThrows(SequenceBuildingException.class, () -> builder.resultTable(null));
        assertThrows(SequenceBuildingException.class, () -> builder.resultTable(""));
        assertThrows(
                SequenceBuildingException.class,
                () -> builder.connectionIdentifier("connect_id").databaseName("db").resultTable("res").applySequence()
        );
    }

    @Test
    public void test_sequence_builder_success_build() {
        SequenceHolderBuilder.SequenceBuilder builder = SequenceHolderBuilder.builder().sequenceBuilder();
        assertDoesNotThrow(() -> builder.connectionIdentifier("connect_id"));
        assertDoesNotThrow(() -> builder.databaseName("db"));
        assertDoesNotThrow(() -> builder.resultTable("res"));
        builder.bodyLine("line");
        assertDoesNotThrow(builder::applySequence);
    }

    @Test
    public void test_finalize_sequence_holder_builder() throws URISyntaxException {
        SequenceHolderBuilder builder = SequenceHolderBuilder.builder();
        assertDoesNotThrow(() -> builder.name("name"));
        assertDoesNotThrow(() -> builder.identifier("id"));
        File file = new File(ClassLoader.getSystemResource("script.txt").toURI());
        assertDoesNotThrow(() -> builder.origin(file));
        assertDoesNotThrow(() -> builder.snippet("snippet"));
        SequenceHolderBuilder.SequenceBuilder sequencedBuilder = builder.sequenceBuilder();
        assertDoesNotThrow(() -> sequencedBuilder.connectionIdentifier("connect_id"));
        assertDoesNotThrow(() -> sequencedBuilder.databaseName("db"));
        assertDoesNotThrow(() -> sequencedBuilder.resultTable("res"));
        sequencedBuilder.bodyLine("line");
        assertDoesNotThrow(sequencedBuilder::applySequence);
        SequenceHolderBuilder.PostSequenceBuilder postSequenceBuilder = builder.postSequenceBuilder();
        assertDoesNotThrow(() -> postSequenceBuilder.resultTable("res"));
        postSequenceBuilder.bodyLine("line");
        assertDoesNotThrow(postSequenceBuilder::applyPostSequence);
        assertThrows(SequenceBuildingException.class, builder::build);
        SequenceHolderBuilder.PostSequenceBuilder postSequenceBuilder2 = builder.postSequenceBuilder();
        assertDoesNotThrow(() -> postSequenceBuilder2.resultTable("res2"));
        postSequenceBuilder2.bodyLine("line");
        assertDoesNotThrow(postSequenceBuilder2::applyPostSequence);
        assertDoesNotThrow(() -> {
            SequenceHolder sequenceHolder = builder.build();
            assertEquals("name", sequenceHolder.getName());
            assertEquals(Set.of("id"), sequenceHolder.getIdentifiers());
            assertEquals(file, sequenceHolder.getOrigin());
            assertEquals("snippet", sequenceHolder.getSnippetBody());
            List<Sequence> sequences = sequenceHolder.getSequences();
            assertFalse(sequences.isEmpty());
            Sequence sequence = sequences.get(0);
            assertNotNull(sequence);
            assertEquals("connect_id", sequence.getConnectionIdentifier());
            assertEquals("db", sequence.getDatabaseName());
            assertEquals("res", sequence.getResultTable());
            assertEquals("line\n", sequence.getBody());
            assertTrue(sequenceHolder.hasPostSequence());
            PostSequence postSequence = sequenceHolder.getPostSequence();
            assertEquals("res2", postSequence.getResultTable());
            assertEquals("line\n", postSequence.getBody());
        });
    }
}
