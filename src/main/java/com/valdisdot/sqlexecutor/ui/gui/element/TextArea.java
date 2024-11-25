package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import java.awt.*;

public class TextArea extends JTextArea {
    public TextArea(
            String initialText,
            Font font,
            Color background,
            Color foreground,
            boolean lineWrap
    ) {
        setText(initialText);
        setFont(font);
        setBackground(background);
        setForeground(foreground);
        setWrapStyleWord(true);
        setLineWrap(lineWrap);
    }

    public TextArea(
            String initialText,
            Font font,
            Color background,
            Color foreground
    ) {
        this(initialText, font, background, foreground, true);
    }
}
