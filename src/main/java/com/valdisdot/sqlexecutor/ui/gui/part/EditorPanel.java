package com.valdisdot.sqlexecutor.ui.gui.part;

import com.valdisdot.sqlexecutor.executor.SequenceExecutorException;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.SequenceQueue;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceBuildingException;
import com.valdisdot.sqlexecutor.sequence.process.compiler.SequenceCompilerException;
import com.valdisdot.sqlexecutor.sequence.process.writer.SequenceWriterException;
import com.valdisdot.sqlexecutor.ui.gui.Localization;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.controller.Controller;
import com.valdisdot.sqlexecutor.ui.gui.element.Button;
import com.valdisdot.sqlexecutor.ui.gui.element.ButtonPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.InternalPanel;
import com.valdisdot.sqlexecutor.ui.gui.part.editor.EditorContainerPanel;

import java.io.File;
import java.util.concurrent.ExecutorService;

public class EditorPanel extends InternalPanel {
    public EditorPanel(
            SequenceHolder sequenceHolder,
            Controller controller,
            ExecutorService executorService,
            Theme theme,
            Localization localization
    ) {
        super(theme);
        EditorContainerPanel editorContainerPanel = new EditorContainerPanel(sequenceHolder, theme, localization);
        //reset
        Button resetButton = new Button(
                localization.getTranslation("editor.reset"),
                editorContainerPanel::reset,
                executorService,
                theme.getEditorResetButtonBackground(),
                theme.getEditorResetButtonForeground(),
                theme.getButtonHeight(),
                theme.getButtonFont()
        );
        //save
        Button saveButton = new Button(
                localization.getTranslation("editor.save"),
                theme.getEditorSaveButtonBackground(),
                theme.getEditorSaveButtonForeground(),
                theme.getButtonHeight(),
                theme.getButtonFont()
        );
        saveButton.addAction(
                () -> {
                    try {
                        saveButton.setEnabled(false);
                        SequenceHolder holder = editorContainerPanel.buildSequenceHolder();
                        controller.saveSequenceHolder(holder);
                        editorContainerPanel.update(holder);
                        controller.showSuccessNotification(localization.getTranslation("sequence.save.success.message"), localization.getTranslation("sequence.save.success.details"));
                    } catch (SequenceBuildingException | SequenceWriterException e) {
                        controller.showErrorNotification(e);
                    } finally {
                        saveButton.setEnabled(true);
                    }
                },
                executorService
        );
        //preview
        Button previewButton = new Button(
                localization.getTranslation("editor.preview"),
                theme.getEditorPreviewButtonBackground(),
                theme.getEditorPreviewButtonForeground(),
                theme.getButtonHeight(),
                theme.getButtonFont()
        );
        previewButton.addAction(
                () -> {
                    try {
                        previewButton.setEnabled(false);
                        SequenceHolder holder = editorContainerPanel.buildSequenceHolder();
                        SequenceQueue sequenceQueue = controller.compileSequenceQueue(holder);
                        controller.showSequenceQueue(holder, sequenceQueue);
                    } catch (SequenceCompilerException | SequenceBuildingException e) {
                        controller.showErrorNotification(e);
                    } finally {
                        previewButton.setEnabled(true);
                    }
                },
                executorService
        );
        //execute
        Button executeButton = new Button(
                localization.getTranslation("editor.execute"),
                theme.getEditorExecuteButtonBackground(),
                theme.getEditorExecuteButtonForeground(),
                theme.getButtonHeight(),
                theme.getButtonFont()
        );
        executeButton.addAction(
                () -> {
                    try {
                        executeButton.setEnabled(false);
                        SequenceHolder holder = editorContainerPanel.buildSequenceHolder();
                        SequenceQueue sequenceQueue = controller.compileSequenceQueue(holder);
                        File result = controller.executeSequenceQueue(holder, sequenceQueue);
                        controller.offerToOpenResultFile(holder, result);
                    } catch (SequenceBuildingException | SequenceCompilerException | SequenceExecutorException e) {
                        controller.showErrorNotification(e);
                    } finally {
                        executeButton.setEnabled(true);
                    }
                },
                executorService
        );
        add(editorContainerPanel);
        add(new ButtonPanel(editorContainerPanel.getPreferredSize().width, theme.getGapX(), theme.getApplicationBackground(), resetButton, saveButton, previewButton, executeButton));
    }
}
