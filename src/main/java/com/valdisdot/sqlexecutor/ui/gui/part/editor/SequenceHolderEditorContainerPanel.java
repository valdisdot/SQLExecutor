package com.valdisdot.sqlexecutor.ui.gui.part.editor;

import com.valdisdot.sqlexecutor.sequence.PostSequence;
import com.valdisdot.sqlexecutor.sequence.Sequence;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceBuildingException;
import com.valdisdot.sqlexecutor.sequence.process.builder.SequenceHolderBuilder;
import com.valdisdot.sqlexecutor.ui.gui.Localization;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.InternalPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.ScrollPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SequenceHolderEditorContainerPanel extends InternalPanel {
    private SequenceHolderHeadSection headSection;
    private SequenceHolderSnippedSection snippedSection;
    private List<SequenceHolderSequenceSection> sequenceSections;
    private SequenceHolderPostSequenceSection postSequenceSection;

    public SequenceHolderEditorContainerPanel(
            SequenceHolder sequenceHolder,
            Theme theme,
            Localization localization
    ) {
        super(theme);
        JPanel scrollable = new InternalPanel(theme);
        headSection = new SequenceHolderHeadSection(sequenceHolder, theme);
        add(headSection);
        snippedSection = new SequenceHolderSnippedSection(sequenceHolder, localization.getTranslation("editor.snippet.section"), theme);
        scrollable.add(snippedSection);
        sequenceSections = new ArrayList<>(sequenceHolder.getSequences().size());
        int i = 1;
        for (Sequence sequence : sequenceHolder.getSequences()) {
            SequenceHolderSequenceSection sequenceSection = new SequenceHolderSequenceSection(
                    sequence,
                    localization.getTranslation("editor.sequence.section"),
                    i++,
                    localization.getTranslation("editor.sequence.connection.label") + ":",
                    localization.getTranslation("editor.sequence.database.label") + ":",
                    localization.getTranslation("editor.sequence.resultTable.label") + ":",
                    localization.getTranslation("editor.sequence.body.label") + ":",
                    theme
            );
            sequenceSections.add(sequenceSection);
            scrollable.add(sequenceSection);
        }
        postSequenceSection = new SequenceHolderPostSequenceSection(
                sequenceHolder,
                localization.getTranslation("editor.postSequence.section"),
                localization.getTranslation("editor.postSequence.resultTable.label") + ":",
                localization.getTranslation("editor.postSequence.body.label") + ":",
                theme
        );
        scrollable.add(postSequenceSection);
        add(headSection);
        int height = Math.min(
                theme.getEditorHeight() - headSection.getPreferredSize().height,
                Math.max(theme.getEditorHeight() - headSection.getPreferredSize().height, scrollable.getPreferredSize().height));
        add(new ScrollPanel(scrollable, new Dimension(scrollable.getPreferredSize().width, height), theme.getScrollBarThickness()));
    }

    public SequenceHolder buildSequenceHolder() throws SequenceBuildingException {
        SequenceHolderBuilder builder = SequenceHolderBuilder.builder();
        SequenceHolder previous = headSection.getSequenceHolder();
        builder
                .name(previous.getName())
                .origin(previous.getOrigin())
                .identifiers(previous.getIdentifiers())
                .snippet(snippedSection.getSnippetTextArea().getText());
        for (SequenceHolderSequenceSection sequenceSection : sequenceSections) {
            builder.sequenceBuilder()
                    .connectionIdentifier(sequenceSection.getConnectionTextField().getText())
                    .databaseName(sequenceSection.getDatabaseTextField().getText())
                    .resultTable(sequenceSection.getResultTextField().getText())
                    .bodyLine(sequenceSection.getSequenceBodyTextArea().getText())
                    .applySequence();
        }
        if (!postSequenceSection.getPostSequenceBodyTextArea().getText().isBlank()) {
            builder.postSequenceBuilder()
                    .resultTable(postSequenceSection.getResultTextField().getText())
                    .bodyLine(postSequenceSection.getPostSequenceBodyTextArea().getText())
                    .applyPostSequence();
        }
        return builder.build();
    }

    public void update(SequenceHolder sequenceHolder) {
        headSection.setSequenceHolder(sequenceHolder);
        reset();
    }

    public void reset() {
        SequenceHolder sequenceHolder = headSection.getSequenceHolder();
        snippedSection.getSnippetTextArea().setText(sequenceHolder.getSnippetBody());
        for (int i = 0; i < sequenceSections.size(); ++i) {
            Sequence sequence = sequenceHolder.getSequences().get(i);
            SequenceHolderSequenceSection sequenceSection = sequenceSections.get(i);
            sequenceSection.getConnectionTextField().setText(sequence.getConnectionIdentifier());
            sequenceSection.getDatabaseTextField().setText(sequence.getDatabaseName());
            sequenceSection.getResultTextField().setText(sequence.getResultTable());
            sequenceSection.getSequenceBodyTextArea().setText(sequence.getBody());
        }
        PostSequence postSequence = sequenceHolder.getPostSequence();
        postSequenceSection.getResultTextField().setText(postSequence == null ? "" : postSequence.getResultTable());
        postSequenceSection.getPostSequenceBodyTextArea().setText(postSequence == null ? "" : postSequence.getBody());
    }
}
