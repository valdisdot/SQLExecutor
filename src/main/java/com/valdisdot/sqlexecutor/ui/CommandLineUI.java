package com.valdisdot.sqlexecutor.ui;

import com.valdisdot.sqlexecutor.configuration.ApplicationConfig;
import com.valdisdot.sqlexecutor.configuration.ConnectionConfig;
import com.valdisdot.sqlexecutor.executor.SequenceExecutor;
import com.valdisdot.sqlexecutor.executor.SequenceExecutorException;
import com.valdisdot.sqlexecutor.executor.database.DatabaseManager;
import com.valdisdot.sqlexecutor.executor.database.DatabaseManagerException;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;
import com.valdisdot.sqlexecutor.sequence.process.compiler.SequenceCompiler;
import com.valdisdot.sqlexecutor.sequence.process.compiler.SequenceCompilerException;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParser;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandLineUI implements Runnable {
    private final ApplicationConfig applicationConfig;
    private final SequenceCompiler sequenceCompiler;
    private final SequenceExecutor sequenceExecutor;
    private final SequenceParser sequenceParser;
    private final Logger logger;
    private final PrintStream out;
    private final Scanner in;
    private final Map<Integer, SequenceHolder> sequenceHolderSheet;

    public CommandLineUI(ApplicationConfig applicationConfig, List<ConnectionConfig> connectionConfigs) throws DatabaseManagerException {
        this.applicationConfig = applicationConfig;
        this.sequenceExecutor = new SequenceExecutor(new DatabaseManager(connectionConfigs, this.applicationConfig), this.applicationConfig);
        this.sequenceCompiler = new SequenceCompiler();
        this.sequenceParser = new SequenceParser();
        this.logger = LogManager.getLogger(this.getClass());
        this.out = System.out;
        this.in = new Scanner(System.in);
        this.sequenceHolderSheet = new LinkedHashMap<>();
    }

    @Override
    public void run() {
        loadSequenceHolderSheet();
        String input = "";
        String menu = makeMainMenu();
        out.print(menu);
        int lastSequenceHolderNumber;
        SequenceHolder lastSequenceHolder = null;
        SequenceQueue lastSequnceQueue = null;
        File lastResultFile = null;
        while (true) {
            out.print("Type the sequence holder number or prompt a command: ");
            input = in.nextLine();
            try {
                lastSequenceHolderNumber = Integer.parseInt(input);
                if (sequenceHolderSheet.containsKey(lastSequenceHolderNumber)) {
                    lastSequenceHolder = this.sequenceHolderSheet.get(lastSequenceHolderNumber);
                    lastSequnceQueue = compileSequenceHolder(lastSequenceHolder);
                    lastResultFile = processSequenceHolder(lastSequnceQueue, lastSequenceHolder, lastSequenceHolderNumber);
                    if (lastResultFile != null) {
                        out.println("Complete! Result file: " + lastResultFile.getAbsolutePath());
                        out.print("Open the result file and its directory? (y): ");
                        if (in.nextLine().trim().equalsIgnoreCase("y")) openFile(lastResultFile, true);
                    }
                } else {
                    out.printf("Sequence holder number '%d' is out of range, try again.\n", lastSequenceHolderNumber);
                }
            } catch (NumberFormatException e) {
                input = input.trim().toLowerCase();
                switch (input) {
                    case "exit": {
                        exit();
                        break;
                    }
                    case "menu": {
                        menu = makeMainMenu();
                        out.print(menu);
                        break;
                    }
                    case "reload": {
                        loadSequenceHolderSheet();
                        menu = makeMainMenu();
                        out.print(menu);
                        break;
                    }
                    default:
                        out.print("Unknown command.");
                }
            } catch (Exception e) {
                logger.warn("Unknown exception", e);
            }
        }
    }

    private void loadSequenceHolderSheet() {
        File[] files = applicationConfig.getInputDirectory().listFiles();
        while (files == null) {
            out.println("Sequence input folder is empty. Path: " + this.applicationConfig.getInputDirectory().getAbsolutePath());
            out.print("Try to reload? (y): ");
            if (!in.nextLine().trim().equalsIgnoreCase("y")) exit();
            files = this.applicationConfig.getInputDirectory().listFiles();
        }
        int holderIndex = 1;
        this.sequenceHolderSheet.clear();
        for (File file : files) {
            SequenceHolder holder = loadSequenceHolder(file);
            this.sequenceHolderSheet.put(holderIndex++, holder);
        }
    }

    private String makeMainMenu() {
        StringBuilder builder = new StringBuilder("Sequence holders:\n");
        sequenceHolderSheet.forEach((i, holder) -> builder.append(i).append("\t").append(holder.getName()).append("\n"));
        return builder.toString();
    }

    private SequenceHolder loadSequenceHolder(File file) {
        try {
            SequenceHolder holder = this.sequenceParser.parseSequenceHolder(file);
            //validate compiling
            this.sequenceCompiler.compileSequenceHolder(holder);
            return holder;
        } catch (SequenceParserException e1) {
            this.logger.warn(e1);
            displayException("Can't parse the file: " + file.getAbsolutePath(), e1);
            out.print("Try to open the sequence file with default editor? (y): ");
            if (in.nextLine().trim().equals("y")) {
                openFile(file, false);
                out.print("Hit enter to continue and try again.");
                in.nextLine();
                return loadSequenceHolder(file);
            } else {
                out.println("File has been skipped!");
                return null;
            }
        } catch (SequenceCompilerException e2) {
            this.logger.warn(e2);
            displayException("Can't compile the file: " + file.getAbsolutePath(), e2);
            out.print("Try to open the sequence file with default editor? (y): ");
            if (in.nextLine().trim().equals("y")) {
                openFile(file, false);
                out.print("Hit enter to continue and try again.");
                in.nextLine();
                return loadSequenceHolder(file);
            } else {
                out.println("File has been skipped!");
                return null;
            }
        }
    }

    private SequenceQueue compileSequenceHolder(SequenceHolder sequenceHolder) {
        try {
            return this.sequenceCompiler.compileSequenceHolder(sequenceHolder);
        } catch (SequenceCompilerException e) {
            this.logger.warn("Error during sequence compiling", e);
            displayException("Error during sequence compiling", e);
            out.print("Try to open the sequence file with default editor? (y): ");
            if (in.nextLine().trim().equals("y")) {
                openFile(sequenceHolder.getOrigin(), false);
                out.print("Hit enter to continue and try again.");
                in.nextLine();
                sequenceHolder = loadSequenceHolder(sequenceHolder.getOrigin());
                return compileSequenceHolder(sequenceHolder);
            } else {
                out.println("File has been skipped!");
                return null;
            }
        }
    }

    private File processSequenceHolder(SequenceQueue sequenceQueue, SequenceHolder originSequenceHolder, int originSequenceHolderIndex) {
        try {
            return sequenceQueue != null ? this.sequenceExecutor.execute(sequenceQueue) : null;
        } catch (SequenceExecutorException e) {
            //maybe error in sql sequence, display an exception
            this.logger.warn("Error during sequence execution", e);
            //offer options
            displayException("Error during sequence execution", e);
            //open file and reload it as a SequenceHolder and then re-execute
            out.print("Try to open the sequence file with default editor? (y): ");
            if (in.nextLine().trim().equals("y")) {
                openFile(originSequenceHolder.getOrigin(), false);
                out.print("Hit enter to continue and try again.");
                in.nextLine();
                originSequenceHolder = loadSequenceHolder(originSequenceHolder.getOrigin());
                this.sequenceHolderSheet.put(originSequenceHolderIndex, originSequenceHolder);
                return processSequenceHolder(compileSequenceHolder(originSequenceHolder), originSequenceHolder, originSequenceHolderIndex);
            } else {
                out.println("File has been skipped!");
                return null;
            }
        }
    }

    private void displayException(String message, Exception exception) {
        out.println(message);
        exception.printStackTrace(out);
    }

    private void openFile(File file, boolean alsoOpenDirectory) {
        try {
            Desktop.getDesktop().open(file);
            if (alsoOpenDirectory) {
                file = file.getParentFile();
                if (file != null) Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            displayException("Can't open the file: " + file.getAbsolutePath(), e);
        }
    }

    private void exit() {
        out.print("Exit the application...");
        System.exit(0);
    }
}
