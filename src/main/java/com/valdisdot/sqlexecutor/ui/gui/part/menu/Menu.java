package com.valdisdot.sqlexecutor.ui.gui.part.menu;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class Menu extends JPanel {
    private final String selectVerb;
    private final Consumer<SequenceHolder> sequenceHolderConsumer;
    private final ExecutorService actionExecutor;
    private final Theme theme;

    public Menu(
            String selectVerb,
            Consumer<SequenceHolder> sequenceHolderConsumer,
            ExecutorService actionExecutor,
            Theme theme
    ) {
        this.selectVerb = Objects.requireNonNullElse(selectVerb, "Select-button verb is null");
        this.sequenceHolderConsumer = Objects.requireNonNull(sequenceHolderConsumer, "Selected sequence holder consumer is null");
        this.actionExecutor = Objects.requireNonNull(actionExecutor, "Action executor service is null");
        this.theme = Objects.requireNonNull(theme, "Theme is null");
        setBackground(theme.getApplicationBackground());
        setLayout(new MigLayout(
                new LC()
                        .wrap()
                        .insets(String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()))
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
    }

    public void addSequenceHolder(SequenceHolder sequenceHolder) {
        add(new MenuItem(sequenceHolder, sequenceHolderConsumer, selectVerb, theme, actionExecutor));
        revalidate();
        repaint();
    }
}
