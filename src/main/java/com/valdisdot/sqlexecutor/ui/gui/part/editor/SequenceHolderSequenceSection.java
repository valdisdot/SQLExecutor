package com.valdisdot.sqlexecutor.ui.gui.part.editor;

import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import com.valdisdot.sqlexecutor.ui.gui.element.TextArea;
import com.valdisdot.sqlexecutor.ui.gui.element.TextField;
import com.valdisdot.sqlexecutor.ui.gui.element.*;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class SequenceHolderSequenceSection extends InternalPanel {
    private TextField connectionTextField;
    private TextField databaseTextField;
    private TextField resultTextField;
    private TextArea sequenceBodyTextArea;

    public SequenceHolderSequenceSection(Sequence sequence, String sequenceSectionTitle, int sequenceNumber, String connectionFieldLabel, String databaseFieldLabel, String resultTableFieldLabel, String sequenceBodyFieldLabel, Theme theme) {
        super(theme);
        add(new Label(sequenceSectionTitle + " #" + sequenceNumber, theme.getEditorSectionTitleFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        JPanel fields = new JPanel(new MigLayout(
                new LC()
                        .insets("0")
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
        fields.setBackground(theme.getApplicationBackground());
        int fieldWidth = (theme.getEditorWidth() - 2 * theme.getGapX()) / 3;
        fields.add(new Label(connectionFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), fieldWidth));
        fields.add(new Label(databaseFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), fieldWidth));
        fields.add(new Label(resultTableFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), fieldWidth), "wrap");
        this.connectionTextField = new TextField(
                sequence.getConnectionIdentifier(),
                theme.getEditorSectionTextFieldFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                fieldWidth
        );
        this.databaseTextField = new TextField(
                sequence.getDatabaseName(),
                theme.getEditorSectionTextFieldFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                fieldWidth
        );
        this.resultTextField = new TextField(
                sequence.getResultTable(),
                theme.getEditorSectionTextFieldFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                fieldWidth
        );
        fields.add(this.connectionTextField);
        fields.add(this.databaseTextField);
        fields.add(this.resultTextField);
        add(fields);
        this.sequenceBodyTextArea = new TextArea(
                sequence.getBody(),
                theme.getEditorSectionTextAreaFont(),
                theme.getTextFieldsBackground(),
                theme.getTextFieldsForeground(),
                false
        );
        add(new Label(sequenceBodyFieldLabel, theme.getEditorSectionLabelFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        add(new ScrollPanel(
                this.sequenceBodyTextArea,
                new Dimension(theme.getEditorWidth(), theme.getEditorSequenceSectionTextAreaHeight()),
                theme.getScrollBarThickness(),
                true,
                new JTextField().getBorder()
        ));
    }

    public TextField getConnectionTextField() {
        return connectionTextField;
    }

    public TextField getDatabaseTextField() {
        return databaseTextField;
    }

    public TextField getResultTextField() {
        return resultTextField;
    }

    public TextArea getSequenceBodyTextArea() {
        return sequenceBodyTextArea;
    }
}
