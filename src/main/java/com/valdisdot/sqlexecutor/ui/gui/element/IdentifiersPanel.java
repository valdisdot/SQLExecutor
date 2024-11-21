package com.valdisdot.sqlexecutor.ui.gui.element;

import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A panel displaying a collection of badges, each representing an identifier.
 * Badges are displayed in a grid with the option to wrap when there isn't enough space in the current row.
 * The badges are styled according to the provided colors, font, and padding.
 */
public class IdentifiersPanel extends JPanel {

    /**
     * Constructs an IdentifiersPanel that arranges the badges (representing identifiers) in a layout.
     * The badges are distributed in lines, wrapping to a new line when the current line is filled.
     * The panel ensures that badges are visually distinct by applying background colors and spacing.
     *
     * @param width the width of the panel, used to determine when to wrap the badges
     * @param gapX the gap on X-axis between badges
     * @param gapX the gap on Y-axis between badges
     * @param panelBackground the background color of the panel
     * @param badgeBackgrounds a collection of colors used for the badges' background, cycled through
     * @param badgeForeground the foreground color (text color) for the badges
     * @param badgeFont the font used for the badge text
     * @param badgeLines the number of lines the badges can occupy
     * @param badgePadding the padding around each badge's content
     * @param identifiers a collection of identifiers to display as badges
     */
    public IdentifiersPanel(
            int width,
            int gapX,
            int gapY,
            Color panelBackground,
            Collection<Color> badgeBackgrounds,
            Color badgeForeground,
            Font badgeFont,
            int badgeLines,
            int badgePadding,
            Collection<String> identifiers
    ) {
        setLayout(new MigLayout(
                new LC()
                        .noGrid()
                        .insets("0")
                        .gridGap(String.valueOf(gapX), String.valueOf(gapY))
        ));
        setBackground(panelBackground);

        // Creates a color wheel for badge backgrounds by cycling through the provided colors
        LinkedList<Color> colorWheel = new LinkedList<>(badgeBackgrounds) {
            @Override
            public Color poll() {
                Color color = super.poll();
                this.offer(color);
                return color;
            }
        };

        // Shuffling the colors for randomness
        Collections.shuffle(colorWheel);

        // Queue to hold the identifiers to be displayed
        Queue<String> identifiersQueue = new LinkedList<>(identifiers);
        int spaceLeftOver = width;
        boolean newLine = false;
        Badge last = null;

        // Loop through the lines and display the badges
        while (badgeLines != 0) {
            if (identifiersQueue.isEmpty()) break;

            // Create and configure the badge
            last = new Badge(identifiersQueue.peek(), colorWheel.poll(), badgeForeground, badgeFont, badgePadding);

            // Check if the badge fits in the remaining space
            if (gapX + last.getPreferredSize().width < spaceLeftOver) {
                add(last);
                spaceLeftOver -= gapX + last.getPreferredSize().width;
                identifiersQueue.poll();
                newLine = false;
            } else {
                spaceLeftOver = width;
                add(new JLabel(""), "wrap, gapx 0"); // Start a new line
                newLine = true;
                badgeLines--;
            }
        }

        // If there are still identifiers left, create a badge showing how many more there are
        if (!identifiersQueue.isEmpty()) {
            last = new Badge("+" + identifiersQueue.size(), colorWheel.poll(), badgeForeground, badgeFont, badgePadding);
            add(last, newLine ? "" : "span, newline");
            last.setToolTipText(String.join(" ", identifiersQueue)); // Tooltip shows the remaining identifiers
        }
    }
}
