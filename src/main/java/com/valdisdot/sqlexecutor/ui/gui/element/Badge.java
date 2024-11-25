package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * A customizable badge component that displays a label with specific styling.
 * The badge includes configurable background and foreground colors, font, and padding.
 */
public class Badge extends JLabel {

    /**
     * Creates a new badge with the specified label, colors, font, and padding.
     *
     * @param label         the text to display on the badge.
     * @param background    the background color of the badge.
     * @param foreground    the foreground (text and border) color of the badge.
     * @param font          the font to use for the badge text.
     * @param borderPadding the padding inside the badge's border.
     */
    public Badge(String label, Color background, Color foreground, Font font, int borderPadding) {
        super(label);
        initializeBadge(background, foreground, font, borderPadding);
    }

    /**
     * Initializes the badge's appearance and styling.
     *
     * @param background    the background color of the badge.
     * @param foreground    the foreground (text and border) color of the badge.
     * @param font          the font to use for the badge text.
     * @param borderPadding the padding inside the badge's border.
     */
    private void initializeBadge(Color background, Color foreground, Font font, int borderPadding) {
        setOpaque(true); // Ensures background color is visible
        setBackground(background);
        setForeground(foreground);
        setFont(font);

        // Creates a compound border with an inner border for padding
        setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(foreground), // Outer border (1px wide)
                BorderFactory.createEmptyBorder(borderPadding / 2, borderPadding, borderPadding / 2, borderPadding) // Inner padding
        ));
    }
}
