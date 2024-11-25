package com.valdisdot.sqlexecutor.ui.gui.element;

import com.valdisdot.sqlexecutor.ui.gui.Theme;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class InternalPanel extends JPanel {
    public InternalPanel(Theme theme) {
        setLayout(new MigLayout(
                new LC()
                        .wrap()
                        .insets("0")
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
        setBackground(theme.getApplicationBackground());
    }
}
