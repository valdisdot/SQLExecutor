package com.valdisdot.sqlexecutor.ui.gui.element;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

/**
 * A panel for arranging {@link Button} components in a horizontal layout, supporting right-to-left positioning.
 * The buttons are added in reverse order, and the layout is configured with a specified gap and background color.
 */
public class ButtonPanel extends JPanel {

    /**
     * Creates a {@link ButtonPanel} with the specified width, gap, background color, and a varargs list of buttons.
     * The buttons are laid out from right to left.
     *
     * @param width     the width of the panel.
     * @param gapX       the gap between buttons in the line.
     * @param background the background color of the panel.
     * @param buttons   the buttons to be added to the panel.
     */
    public ButtonPanel(int width, int gapX, Color background, Button... buttons) {
        this(width, gapX, background, buttons.length == 0 ? List.of() : Arrays.asList(buttons));
    }

    /**
     * Creates a {@link ButtonPanel} with the specified width, gap, background color, and a collection of buttons.
     * The buttons are added in reverse order and arranged from right to left.
     *
     * @param width     the width of the panel.
     * @param gapX       the gap between buttons in the line.
     * @param background the background color of the panel.
     * @param buttons   the collection of buttons to be added to the panel.
     */
    public ButtonPanel(int width, int gapX, Color background, Collection<Button> buttons) {
        setLayout(
                new MigLayout(new LC()
                        .gridGap(String.valueOf(gapX), "0")  // Sets the horizontal gap between buttons.
                        .insets("0")             // Sets no insets around the panel.
                        .rightToLeft()           // Arranges buttons from right to left.
                )
        );
        setBackground(background);  // Set the panel's background color.

        // Create a stack to reverse the order of button additions.
        Deque<Button> stack = new LinkedList<>(buttons);

        // Add each button from the end of the stack (reverse order).
        while (!stack.isEmpty()) {
            add(stack.pollLast());
        }

        // Set the preferred width of the panel, maintaining its original height.
        setPreferredSize(new Dimension(width, getPreferredSize().height));
    }
}
