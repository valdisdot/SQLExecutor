package com.valdisdot.sqlexecutor.sequence.process.writer;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParser;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceWriterTest {
    @Test
    public void test_sequence_writer() throws URISyntaxException, IOException {
        assertDoesNotThrow(() -> {
            File file = new File("test.txt");
            file.createNewFile();
            String sequence = Files.readString(Path.of(ClassLoader.getSystemResource("script.txt").toURI()));
            SequenceHolder sequenceHolder = new SequenceParser().parseSequenceHolder(sequence, file);
            SequenceWriter writer = new SequenceWriter();
            assertDoesNotThrow(() -> writer.writeSequenceHolder(sequenceHolder));
            assertEquals(sequence.replaceAll("\\s", ""), Files.readString(file.toPath()).replaceAll("\\s", ""));
            assertDoesNotThrow(() -> new SequenceParser().parseSequenceHolder(file));
            file.delete();
        });
    }
}
