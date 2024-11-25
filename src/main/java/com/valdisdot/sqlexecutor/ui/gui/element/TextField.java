package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import java.awt.*;

public class TextField extends JTextField {
    public TextField(
            Font font,
            Color background,
            Color foreground,
            int width
    ) {
        setFont(font);
        setBackground(background);
        setForeground(foreground);
        setPreferredSize(new Dimension(width, this.getPreferredSize().height));
    }

    public TextField(
            String initialText,
            Font font,
            Color background,
            Color foreground,
            int width
    ) {
        this(font, background, foreground, width);
        setText(initialText);
    }
}
