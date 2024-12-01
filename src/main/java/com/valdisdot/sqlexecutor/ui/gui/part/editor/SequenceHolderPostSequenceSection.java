package com.valdisdot.sqlexecutor.ui.gui.part.editor;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import com.valdisdot.sqlexecutor.ui.gui.element.TextArea;
import com.valdisdot.sqlexecutor.ui.gui.element.TextField;
import com.valdisdot.sqlexecutor.ui.gui.element.*;

import javax.swing.*;
import java.awt.*;

public class SequenceHolderPostSequenceSection extends InternalPanel {
    private TextField resultTextField;
    private TextArea postSequenceBodyTextArea;

    public SequenceHolderPostSequenceSection(SequenceHolder sequenceHolder, String postSequenceSectionTitle, String resultTableFieldLabel, String postSequenceBodyFieldLabel, Theme theme) {
        super(theme);
        add(new Label(postSequenceSectionTitle, theme.getEditorSectionTitleFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        JPanel fields = new InternalPanel(theme);
        int fieldWidth = (theme.getEditorWidth() - 2 * theme.getGapX()) / 3;
        fields.add(new Label(resultTableFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), fieldWidth), "wrap");
        this.resultTextField = new TextField(
                sequenceHolder.hasPostSequence() ? sequenceHolder.getPostSequence().getResultTable() : "",
                theme.getEditorSectionTextFieldFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                fieldWidth
        );
        fields.add(this.resultTextField);
        add(fields);
        this.postSequenceBodyTextArea = new TextArea(
                sequenceHolder.hasPostSequence() ? sequenceHolder.getPostSequence().getBody() : "",
                theme.getEditorSectionTextAreaFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                false
        );
        add(new Label(postSequenceBodyFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        add(new ScrollPanel(
                this.postSequenceBodyTextArea,
                new Dimension(theme.getEditorWidth(), theme.getEditorPostSequenceSectionTextAreaHeight()),
                theme.getScrollBarThickness(),
                true,
                new JTextField().getBorder()
        ));
    }

    public TextField getResultTextField() {
        return resultTextField;
    }

    public TextArea getPostSequenceBodyTextArea() {
        return postSequenceBodyTextArea;
    }
}
