package com.valdisdot.sqlexecutor.executor.writer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class XLSXResultWriter extends ResultWriter {
    private Map<File, XSSFWorkbook> holder;

    public XLSXResultWriter(File outputFolder, Supplier<String> fileSuffixSupplier) {
        super(outputFolder, fileSuffixSupplier, ".xlsx");
        holder = new HashMap<>();
    }

    @Override
    public File initializeResultFile(String resultFileName) throws ResultWriterException {
        File initialized = super.initializeResultFile(resultFileName);
        holder.put(initialized, new XSSFWorkbook());
        return initialized;
    }

    @Override
    public void write(String resultIdentifier, ResultSet resultSet, File output) throws SQLException, ResultWriterException {
        Workbook workbook = holder.get(output);
        //unknown xlsx files or xlsx files which are not from the writing chain will be not supported
        if (workbook == null) throw new ResultWriterException("Unknown file: " + output);
        Sheet sheet = workbook.createSheet(resultIdentifier);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Create header row with column names
        Row headerRow = sheet.createRow(0);
        for (int i = 1; i <= columnCount; i++) {
            Cell cell = headerRow.createCell(i - 1);
            cell.setCellValue(metaData.getColumnName(i));
        }

        // Initialize row counter
        int rowCount = 0;

        // Write data rows
        while (resultSet.next()) {
            Row row = sheet.createRow(++rowCount);  // Start from the second row
            for (int i = 1; i <= columnCount; i++) {
                Cell cell = row.createCell(i - 1);
                cell.setCellValue(resultSet.getString(i));  // Write each column's data
            }
        }

        sheet.setAutoFilter(new CellRangeAddress(0, rowCount, 0, columnCount - 1));
        for (int i = columnCount; i >= 0; --i) sheet.autoSizeColumn(i);
        try (OutputStream fileOut = new FileOutputStream(output)) {
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new ResultWriterException("Error during collecting the result data into the final result file: " + output.getAbsolutePath(), e);
        }
    }
}
