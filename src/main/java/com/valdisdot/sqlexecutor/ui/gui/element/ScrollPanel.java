package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ScrollPanel extends JScrollPane {
    public ScrollPanel(JComponent component, Dimension size, int scrollBarThickness, boolean enableHorizontalScrollBar, Border border) {
        super(component, ScrollPanel.VERTICAL_SCROLLBAR_AS_NEEDED, enableHorizontalScrollBar ? ScrollPanel.HORIZONTAL_SCROLLBAR_AS_NEEDED : ScrollPanel.HORIZONTAL_SCROLLBAR_NEVER);
        size = new Dimension(size.width + (component.getPreferredSize().height > size.height ? scrollBarThickness : 0), size.height + (enableHorizontalScrollBar && component.getPreferredSize().width > size.width ? scrollBarThickness : 0));
        JScrollBar verticalScrollBar = this.getVerticalScrollBar();
        JScrollBar horizontalScrollBar = this.getHorizontalScrollBar();
        int verticalBarHeight = verticalScrollBar.getPreferredSize().height;
        int horizontalBarWidth = horizontalScrollBar.getPreferredSize().width;
        verticalScrollBar.setPreferredSize(new Dimension(scrollBarThickness, verticalBarHeight));
        verticalScrollBar.setUnitIncrement(verticalBarHeight);
        verticalScrollBar.setBlockIncrement(verticalBarHeight);
        horizontalScrollBar.setPreferredSize(new Dimension(horizontalBarWidth, scrollBarThickness));
        horizontalScrollBar.setUnitIncrement(verticalBarHeight);
        horizontalScrollBar.setBlockIncrement(verticalBarHeight);
        if (border == null) setBorder(BorderFactory.createEmptyBorder());
        else setBorder(border);
        setPreferredSize(size);
    }

    public ScrollPanel(JComponent component, Dimension size, int scrollBarWidth) {
        this(component, size, scrollBarWidth, false, null);
    }

    public ScrollPanel(JComponent component, Dimension size, int scrollBarWidth, Border border) {
        this(component, size, scrollBarWidth, false, border);
    }
}
