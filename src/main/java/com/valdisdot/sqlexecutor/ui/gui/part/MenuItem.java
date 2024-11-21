package com.valdisdot.sqlexecutor.ui.gui.part;

import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Button;
import com.valdisdot.sqlexecutor.ui.gui.element.ButtonPanel;
import com.valdisdot.sqlexecutor.ui.gui.element.IdentifiersPanel;
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
public class MenuItem extends JPanel {
    /**
     * Constructs a new MenuItem instance.
     *
     * @param sequenceHolder the sequence this menu item represents
     * @param sequenceHolderConsumer a consumer to handle user interaction with the sequence
     * @param selectVerb the verb to display on the select button (e.g., "Run", "View")
     * @param theme the theme to use for styling
     * @param actionExecutor the executor service used for executing actions on the sequence
     */
    public MenuItem(
            SequenceHolder sequenceHolder,
            Consumer<SequenceHolder> sequenceHolderConsumer,
            String selectVerb,
            Theme theme,
            ExecutorService actionExecutor
    ) {
        initializeLayout(theme);
        setBackground(theme.getApplicationBackground());
        add(createTitleLabel(sequenceHolder, theme));
        add(createIdentifiersPanel(sequenceHolder, theme));
        add(createButtonPanel(sequenceHolder, sequenceHolderConsumer, selectVerb, theme, actionExecutor));
    }

    /**
     * Initializes the layout of the menu item based on the provided theme.
     *
     * @param theme the theme to use for styling
     */
    private void initializeLayout(Theme theme) {
        setLayout(new MigLayout(
                new LC()
                        .wrap()
                        .insets("0") //let Menu control that
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
    }

    /**
     * Creates a JLabel to display the sequence name.
     *
     * @param sequenceHolder the sequence this menu item represents
     * @param theme the theme to use for styling
     * @return a JLabel displaying the sequence name
     */
    private JLabel createTitleLabel(SequenceHolder sequenceHolder, Theme theme) {
        JLabel menuTitle = new JLabel(sequenceHolder.getName());
        menuTitle.setForeground(theme.getTextFieldsForeground());
        menuTitle.setFont(theme.getMenuTitleFont());
        menuTitle.setMaximumSize(new Dimension(
                theme.getMenuWidth(),
                menuTitle.getPreferredSize().height
        ));
        return menuTitle;
    }

    /**
     * Creates an IdentifiersPanel to display identifiers associated with the sequence.
     *
     * @param sequenceHolder the sequence this menu item represents
     * @param theme the theme to use for styling
     * @return an IdentifiersPanel displaying sequence identifiers
     */
    private IdentifiersPanel createIdentifiersPanel(SequenceHolder sequenceHolder, Theme theme) {
        return new IdentifiersPanel(
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
        );
    }

    /**
     * Creates a ButtonPanel containing a button to interact with the sequence.
     *
     * @param sequenceHolder the sequence this menu item represents
     * @param sequenceHolderConsumer a consumer to handle user interaction with the sequence
     * @param selectVerb the verb to display on the select button (e.g., "Run", "View")
     * @param theme the theme to use for styling
     * @param actionExecutor the executor service used for executing actions on the sequence
     * @return a ButtonPanel containing the select button
     */
    private ButtonPanel createButtonPanel(
            SequenceHolder sequenceHolder,
            Consumer<SequenceHolder> sequenceHolderConsumer,
            String selectVerb,
            Theme theme,
            ExecutorService actionExecutor
    ) {
        Button selectButton = new Button(
                selectVerb,
                () -> sequenceHolderConsumer.accept(sequenceHolder),
                actionExecutor,
                theme.getMenuSelectButtonBackground(),
                theme.getMenuSelectButtonForeground(),
                theme.getButtonHeight(),
                theme.getButtonFont()
        );

        return new ButtonPanel(
                theme.getMenuWidth(),
                theme.getGapX(),
                theme.getApplicationBackground(),
                selectButton
        );
    }
}