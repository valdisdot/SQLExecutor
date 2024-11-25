package com.valdisdot.sqlexecutor.executor;

import com.valdisdot.sqlexecutor.configuration.ApplicationConfig;
import com.valdisdot.sqlexecutor.executor.database.DatabaseManager;
import com.valdisdot.sqlexecutor.executor.writer.ResultWriterException;
import com.valdisdot.sqlexecutor.executor.writer.SQLiteResultWriter;
import com.valdisdot.sqlexecutor.executor.writer.XLSXResultWriter;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SequenceExecutor {
    private DatabaseManager databaseManager;
    private SQLiteResultWriter temporaryResultWriter;
    private XLSXResultWriter resultWriter;
    private boolean shouldIncludeSequenceResults;

    public SequenceExecutor(DatabaseManager databaseManager, ApplicationConfig applicationConfig) {
        this.databaseManager = databaseManager;
        this.shouldIncludeSequenceResults = applicationConfig.shouldIncludeSequenceResults();
        this.resultWriter = new XLSXResultWriter(applicationConfig.getOutputDirectory(), applicationConfig.getUniqueSuffixSupplier());
        this.temporaryResultWriter = new SQLiteResultWriter(applicationConfig.getLocalDatabaseDirectory(), databaseManager);
    }

    public File execute(SequenceQueue sequenceQueue) throws SequenceExecutorException {
        if (!sequenceQueue.hasNextSequence())
            throw new SequenceExecutorException("Sequence queue is empty, nothing to execute");
        try {
            //this operation is slow somehow
            File resultFile = resultWriter.initializeResultFile(sequenceQueue.getSequenceName());
            //if post sequence
            if (sequenceQueue.hasPostSequence()) {
                File temporaryDatabaseFile = temporaryResultWriter.initializeResultFile(sequenceQueue.getSequenceName());
                while (sequenceQueue.hasNextSequence()) {
                    try (Connection connection = databaseManager.getConnection(sequenceQueue.nextConnection(), sequenceQueue.nextDatabase())) {
                        Statement statement = connection.createStatement();
                        String seq = sequenceQueue.nextSequence();
                        //natural bottleneck, may be slow for hard sequences
                        ResultSet rs = statement.executeQuery(seq);
                        temporaryResultWriter.write(sequenceQueue.nextResultIdentifier(), rs, temporaryDatabaseFile);
                        rs.close();
                        statement.close();
                    }
                }
                try (Connection connection = databaseManager.getLocalConnection(temporaryDatabaseFile.getAbsolutePath())) {
                    Statement statement = connection.createStatement();
                    ResultSet rs = statement.executeQuery(sequenceQueue.getPostSequenceBody());
                    //may be slow with a load of data
                    resultWriter.write(sequenceQueue.getPostSequenceResultIdentifier(), rs, resultFile);
                    rs.close();
                    statement.close();
                    if (shouldIncludeSequenceResults) {
                        temporaryResultWriter.writeAllDatabaseTables(temporaryDatabaseFile, resultFile, resultWriter);
                    }
                }
            } else {
                while (sequenceQueue.hasNextSequence()) {
                    try (Connection connection = databaseManager.getConnection(sequenceQueue.nextConnection(), sequenceQueue.nextDatabase())) {
                        Statement statement = connection.createStatement();
                        ResultSet rs = statement.executeQuery(sequenceQueue.nextSequence());
                        resultWriter.write(sequenceQueue.nextResultIdentifier(), rs, resultFile);
                        rs.close();
                        statement.close();
                    }
                }
            }
            return resultFile;
        } catch (SQLException e1) {
            throw new SequenceExecutorException("Error during sequence execution", e1);
        } catch (ResultWriterException e2) {
            throw new SequenceExecutorException("Error during saving the result", e2);
        }
    }
}
