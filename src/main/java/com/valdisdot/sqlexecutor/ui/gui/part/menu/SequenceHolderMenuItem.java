package com.valdisdot.sqlexecutor.ui.gui.part.menu;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Button;
import com.valdisdot.sqlexecutor.ui.gui.element.ButtonPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.IdentifiersPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * This class represents a single menu item displayed within the user interface.
 * It displays information about a sequence and provides a button to interact with it.
 */
public class SequenceHolderMenuItem extends JPanel {
    /**
     * Constructs a new MenuItem instance.
     *
     * @param sequenceHolder         the sequence this menu item represents
     * @param sequenceHolderConsumer a consumer to handle user interaction with the sequence
     * @param selectVerb             the verb to display on the select button (e.g., "Run", "View")
     * @param theme                  the theme to use for styling
     * @param actionExecutor         the executor service used for executing actions on the sequence
     */
    public SequenceHolderMenuItem(
            SequenceHolder sequenceHolder,
            Consumer<SequenceHolder> sequenceHolderConsumer,
            String selectVerb,
            Theme theme,
            ExecutorService actionExecutor
    ) {
        setLayout(new MigLayout(
                new LC()
                        .wrap()
                        .insets(String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX())) //let Menu control that
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
        setBackground(theme.getApplicationBackground());
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        add(new Label(sequenceHolder.getName(), theme.getMenuTitleFont(), theme.getTextFieldsForeground(), theme.getMenuWidth()));
        add(
                new IdentifiersPanel(
                        theme.getMenuWidth(),
                        theme.getGapX(),
                        theme.getGapY(),
                        theme.getApplicationBackground(),
                        theme.getBadgeColors(),
                        theme.getBadgeForeground(),
                        theme.getBadgeFont(),
                        theme.getBadgeLines(),
                        theme.getBadgePadding(),
                        sequenceHolder.getIdentifiers()
                ));

        add(
                new ButtonPanel(
                        theme.getMenuWidth(),
                        theme.getGapX(),
                        theme.getApplicationBackground(),
                        new Button(
                                selectVerb,
                                () -> sequenceHolderConsumer.accept(sequenceHolder),
                                actionExecutor,
                                theme.getMenuSelectButtonBackground(),
                                theme.getMenuSelectButtonForeground(),
                                theme.getButtonHeight(),
                                theme.getButtonFont()
                        )
                ));
    }
}