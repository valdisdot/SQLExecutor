package com.valdisdot.sqlexecutor.sequence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SequenceQueueTest {
    @Test
    public void test_sequence_queue(){
        int i = 1;
        int amount = 3;
        SequenceQueue queue = new SequenceQueue("test");
        for(int j = i; j <= amount; ++j) queue.addSequence("con" + j, "dat" + j, "seq" + j, "res" + j);
        queue.addPostSequence("seq", "res");

        assertEquals("test", queue.getSequenceName());
        for(int j = i; j <= amount; ++j) {
            assertEquals("con" + j, queue.nextConnection());
            assertEquals("dat" + j, queue.nextDatabase());
            assertEquals("seq" + j, queue.nextSequence());
            assertEquals("res" + j, queue.nextResultIdentifier());
        }
        assertFalse(queue.hasNextSequence());
        assertTrue(queue.hasPostSequence());
        assertEquals("seq", queue.getPostSequenceBody());
        assertEquals("res", queue.getPostSequenceResultIdentifier());
    }
}
