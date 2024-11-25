package com.valdisdot.sqlexecutor.ui.gui.part.editor;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.InternalPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import com.valdisdot.sqlexecutor.ui.gui.element.ScrollPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.TextArea;

import javax.swing.*;
import java.awt.*;

public class SnippedSection extends InternalPanel {
    private TextArea snippetTextArea;

    public SnippedSection(SequenceHolder sequenceHolder, String snippetSectionLabel, Theme theme) {
        super(theme);
        this.snippetTextArea = new TextArea(
                sequenceHolder.hasSnippetBody() ? sequenceHolder.getSnippetBody() : "",
                theme.getEditorSectionTextAreaFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                false
        );

        add(new Label(snippetSectionLabel, theme.getEditorSectionTitleFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        add(new ScrollPanel(
                this.snippetTextArea,
                new Dimension(theme.getEditorWidth(), theme.getEditorSnippetSectionTextAreaHeight()),
                theme.getScrollBarThickness(),
                true,
                new JTextField().getBorder()
        ));
    }

    public TextArea getSnippetTextArea() {
        return snippetTextArea;
    }
}
