package com.valdisdot.sqlexecutor.ui.gui.part.menu;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public class Menu extends JPanel {
    private final Map<SequenceHolder, Integer> sequenceHolderComponentPositions;
    private int nextComponentIndex;
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
        this.sequenceHolderComponentPositions = new LinkedHashMap<>();
        this.nextComponentIndex = 0;
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
        sequenceHolderComponentPositions.put(sequenceHolder, nextComponentIndex++);
        revalidate();
        repaint();
    }

    public void updateSequenceHolder(SequenceHolder sequenceHolder) {
        if(!sequenceHolderComponentPositions.containsKey(sequenceHolder)) throw new IllegalArgumentException("Sequence holder is not found, use 'addSequenceHolder' to add a new one");
        int index = sequenceHolderComponentPositions.get(sequenceHolder);
        remove(index);
        add(new MenuItem(sequenceHolder, sequenceHolderConsumer, selectVerb, theme, actionExecutor), index);
        sequenceHolderComponentPositions.put(sequenceHolder, index);
        revalidate();
        repaint();
    }
}
