package com.valdisdot.sqlexecutor.executor.writer;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public abstract class ResultWriter {
    protected final File outputFolder;
    protected final Supplier<String> fileSuffixSupplier;
    protected final String fileExtension;

    public ResultWriter(File outputFolder, Supplier<String> fileSuffixSupplier, String fileExtension) {
        this.outputFolder = outputFolder;
        this.outputFolder.mkdirs();
        this.fileSuffixSupplier = fileSuffixSupplier;
        this.fileExtension = fileExtension;
    }

    public File initializeResultFile(String resultFileName) throws ResultWriterException {
        try {
            File dbFile = new File(outputFolder, resultFileName + fileSuffixSupplier.get() + fileExtension);
            //override the file
            if (dbFile.exists()) {
                dbFile.delete();
                dbFile.createNewFile();
            }
            return dbFile;
        } catch (IOException e) {
            throw new ResultWriterException("Error during result file initialization", e);
        }
    }

    public File write(String resultFileName, String resultIdentifier, ResultSet resultSet) throws ResultWriterException {
        File resultFile = initializeResultFile(resultFileName);
        try {
            write(resultIdentifier, resultSet, resultFile);
            return resultFile;
        } catch (IOException e) {
            throw new ResultWriterException("Error during result file writing", e);
        } catch (SQLException e) {
            throw new ResultWriterException("Error during collecting the result data", e);
        }
    }

    public abstract void write(String resultIdentifier, ResultSet resultSet, File output) throws IOException, SQLException, ResultWriterException;
}
