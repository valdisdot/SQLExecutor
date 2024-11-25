package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    public Label(String label, Font font, Color foreground, int maxWidth) {
        super(label);
        setFont(font);
        setForeground(foreground);
        setMaximumSize(new Dimension(maxWidth, this.getPreferredSize().height));
    }
}
