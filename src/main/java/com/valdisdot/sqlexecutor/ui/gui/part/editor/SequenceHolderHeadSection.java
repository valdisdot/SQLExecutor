package com.valdisdot.sqlexecutor.ui.gui.part.editor;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.IdentifiersPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.InternalPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;

public class SequenceHolderHeadSection extends InternalPanel {
    private SequenceHolder sequenceHolder;

    public SequenceHolderHeadSection(SequenceHolder sequenceHolder, Theme theme) {
        super(theme);
        this.sequenceHolder = sequenceHolder;
        add(new Label(this.sequenceHolder.getName(), theme.getEditorTitleFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        add(new Label(this.sequenceHolder.getOrigin().getAbsolutePath(), theme.getEditorSequencePathFont(), theme.getTextFieldsForeground(), theme.getEditorWidth()));
        add(new IdentifiersPanel(
                theme.getEditorWidth(),
                theme.getGapX(),
                theme.getGapY(),
                theme.getApplicationBackground(),
                theme.getBadgeColors(),
                theme.getBadgeForeground(),
                theme.getBadgeFont(),
                theme.getBadgeLines(),
                theme.getBadgePadding(),
                this.sequenceHolder.getIdentifiers()
        ));
    }

    public SequenceHolder getSequenceHolder() {
        return sequenceHolder;
    }

    public void setSequenceHolder(SequenceHolder sequenceHolder) {
        this.sequenceHolder = sequenceHolder;
    }
}
