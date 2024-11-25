package com.valdisdot.sqlexecutor.ui.gui.controller;

import com.valdisdot.sqlexecutor.executor.SequenceExecutor;
import com.valdisdot.sqlexecutor.executor.SequenceExecutorException;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceBuildingException;
import com.valdisdot.sqlexecutor.sequence.process.compiler.SequenceCompiler;
import com.valdisdot.sqlexecutor.sequence.process.compiler.SequenceCompilerException;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParser;
import com.valdisdot.sqlexecutor.sequence.process.parser.SequenceParserException;
import com.valdisdot.sqlexecutor.sequence.process.writer.SequenceWriter;
import com.valdisdot.sqlexecutor.sequence.process.writer.SequenceWriterException;
import com.valdisdot.sqlexecutor.ui.gui.Localization;
import com.valdisdot.sqlexecutor.ui.gui.notification.Notificator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private Logger logger;
    private SequenceParser sequenceParser;
    private SequenceCompiler sequenceCompiler;
    private SequenceExecutor sequenceExecutor;
    private SequenceWriter sequenceWriter;
    private Notificator notificator;
    private Localization localization;

    public Controller(SequenceExecutor sequenceExecutor, Notificator notificator, Localization localization) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.sequenceExecutor = sequenceExecutor;
        this.sequenceParser = new SequenceParser();
        this.sequenceCompiler = new SequenceCompiler();
        this.sequenceWriter = new SequenceWriter();
        this.notificator = notificator;
        this.localization = localization;
    }

    public List<SequenceHolder> loadSequenceHolders(List<File> files) {
        List<SequenceHolder> result = new ArrayList<>(files.size());
        for (File file : files) {
            SequenceHolder holder = loadSequenceHolder(file);
            if (holder != null) result.add(holder);
        }
        return result;
    }

    public SequenceHolder loadSequenceHolder(File file) {
        try {
            return sequenceParser.parseSequenceHolder(file);
        } catch (SequenceParserException e) {
            Variable<Boolean> doReopen = new Variable<>(false);
            Variable<Boolean> lock = new Variable<>(true);
            notificator.option(
                    localization.getTranslation("notification.error.parser"),
                    ExceptionUtils.getStackTrace(e),
                    localization.getTranslation("sequence.parser.option.reopen"),
                    localization.getTranslation("notification.button.accept"),
                    () -> {
                        doReopen.setValue(true);
                        lock.setValue(false);
                    },
                    localization.getTranslation("notification.button.decline"),
                    () -> {
                        doReopen.setValue(false);
                        lock.setValue(false);
                    }
            );
            while (lock.getValue()) {
            }
            if (doReopen.getValue()) {
                try {
                    Desktop.getDesktop().open(file);
                    notificator.notification(
                            Notificator.Status.SUCCESS,
                            localization.getTranslation("sequence.parser.reopen.message"),
                            localization.getTranslation("sequence.parser.reopen.details"),
                            localization.getTranslation("notification.button.accept")
                    );
                } catch (Exception ex) {
                    logger.error("Unexpended error during sequence holder file reopening", ex);
                    showErrorNotification(e);
                }
                return loadSequenceHolder(file);
            }
            return null;
        }
    }

    public SequenceHolder saveSequenceHolder(SequenceHolder sequenceHolder) throws SequenceWriterException {
        sequenceWriter.writeSequenceHolder(sequenceHolder);
        return sequenceHolder;
    }

    public SequenceQueue compileSequenceQueue(SequenceHolder sequenceHolder) throws SequenceCompilerException {
        return sequenceCompiler.compileSequenceHolder(sequenceHolder);
    }

    public void showSequenceQueue(SequenceHolder originalSequenceHolder, SequenceQueue sequenceQueue) {
        notificator.notification(
                Notificator.Status.SUCCESS,
                originalSequenceHolder.getName(),
                sequenceQueue.toSQLCannonicalString(),
                localization.getTranslation("notification.button.ok")
        );
    }

    public File executeSequenceQueue(SequenceHolder originalSequenceHolder, SequenceQueue sequenceQueue) throws SequenceExecutorException {
        return sequenceExecutor.execute(sequenceQueue);
    }

    public void showErrorNotification(Exception exception) {
        showErrorNotification(exception, null);
    }

    public void showErrorNotification(Exception exception, Runnable afterAcceptAction) {
        String message = null;
        try {
            throw exception;
        } catch (SequenceBuildingException e) {
            message = localization.getTranslation("notification.error.builder");
        } catch (SequenceCompilerException e) {
            message = localization.getTranslation("notification.error.compiler");
        } catch (SequenceWriterException e) {
            message = localization.getTranslation("notification.error.writer");
        } catch (SequenceExecutorException e) {
            message = localization.getTranslation("notification.error.executor");
        } catch (FileNotFoundException e) {
            message = localization.getTranslation("notification.error.no-file");
        } catch (Exception e) {
            message = localization.getTranslation("notification.error.common");
        }
        notificator.notification(
                Notificator.Status.ERROR,
                message,
                ExceptionUtils.getStackTrace(exception),
                localization.getTranslation("notification.button.ok"),
                afterAcceptAction
        );
    }

    public void showSuccessNotification(String message, String details) {
        notificator.notification(
                Notificator.Status.SUCCESS,
                message,
                details,
                localization.getTranslation("notification.button.ok")
        );
    }

    public void offerToOpenResultFile(SequenceHolder originalSequenceHolder, File file) {
        notificator.option(
                localization.getTranslation("sequence.executor.success.message"),
                new StringBuilder()
                        .append(localization.getTranslation("sequence.name")).append(": ").append(originalSequenceHolder.getName()).append("\n")
                        .append(localization.getTranslation("sequence.result.path")).append(": ").append(file.getAbsolutePath())
                        .toString(),
                localization.getTranslation("sequence.executor.result.option"),
                localization.getTranslation("notification.button.accept"),
                () -> {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        showErrorNotification(e);
                    }
                },
                localization.getTranslation("notification.button.decline")
        );
    }
}
