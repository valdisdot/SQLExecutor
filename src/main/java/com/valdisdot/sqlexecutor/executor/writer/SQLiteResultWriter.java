package com.valdisdot.sqlexecutor.executor.writer;

import com.valdisdot.sqlexecutor.executor.database.DatabaseManager;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashSet;
import java.util.Set;

public class SQLiteResultWriter extends ResultWriter {
    private final DatabaseManager databaseManager;

    public SQLiteResultWriter(File outputFolder, DatabaseManager databaseManager) {
        super(outputFolder, () -> "", ".db");
        this.databaseManager = databaseManager;
    }

    @Override
    public void write(String resultIdentifier, ResultSet resultSet, File output) throws SQLException {
        try (
                Connection connection = databaseManager.getLocalConnection(output.getAbsolutePath());
                Statement statement = connection.createStatement()
        ) {
            // Get ResultSet metadata for column names and types
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Create the table with columns from ResultSet metadata
            StringBuilder createTableQuery = new StringBuilder("CREATE TABLE \"" + resultIdentifier + "\" (");
            for (int i = 1; i <= columnCount; i++) {
                createTableQuery.append("\"").append(metaData.getColumnName(i)).append("\" ");
                createTableQuery.append(getSQLiteDataType(metaData.getColumnType(i)));
                if (i < columnCount) {
                    createTableQuery.append(", ");
                }
            }
            createTableQuery.append(");");
            statement.execute(createTableQuery.toString());

            // Insert data into the table
            StringBuilder insertQuery = new StringBuilder("INSERT INTO \"" + resultIdentifier + "\" VALUES (");
            for (int i = 1; i <= columnCount; i++) {
                insertQuery.append("?");
                if (i < columnCount) {
                    insertQuery.append(", ");
                }
            }
            insertQuery.append(");");
            connection.setAutoCommit(false);
            //replace with in-memory chunks maybe and remove batches
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery.toString())) {
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        preparedStatement.setObject(i, resultSet.getObject(i));
                    }
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
                connection.setAutoCommit(true);
            }
        }
    }

    public void writeAllDatabaseTables(File databaseFile, File outputFile, ResultWriter resultWriter) throws ResultWriterException, SQLException {
        try {
            Connection connection = databaseManager.getLocalConnection(databaseFile.getAbsolutePath());
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table';");
            Set<String> tables = new LinkedHashSet<>();
            while (rs.next()) tables.add(rs.getString(1));
            rs.close();
            statement.close();
            for (String table : tables) {
                statement = connection.createStatement();
                rs = statement.executeQuery("select * from \"" + table + "\";");
                resultWriter.write(table, rs, outputFile);
                rs.close();
                statement.close();
            }
            connection.close();
        } catch (SQLException e) {
            throw new SQLException("Error during execution the query in the cache database", e);
        } catch (IOException e) {
            throw new ResultWriterException("Error during collecting the result data into cache database file: " + databaseFile.getAbsolutePath(), e);
        }
    }

    private String getSQLiteDataType(int sqlType) {
        switch (sqlType) {
            case Types.INTEGER:
                return "INTEGER";
            case Types.FLOAT:
            case Types.REAL:
                return "REAL";
            case Types.DOUBLE:
                return "DOUBLE";
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.LONGVARCHAR:
                return "TEXT";
            case Types.DATE:
            case Types.TIMESTAMP:
                return "TEXT"; // Store dates as ISO-formatted strings
            case Types.BLOB:
                return "BLOB";
            default:
                return "TEXT"; // Default to TEXT for other types
        }
    }
}

