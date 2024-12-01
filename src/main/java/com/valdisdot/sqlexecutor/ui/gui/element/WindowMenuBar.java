package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class WindowMenuBar extends JMenuBar {
    public WindowMenuBar(Color background, Color foreground, Font font) {
        setBackground(background);
        setForeground(foreground);
        setFont(font);
    }

    public WindowMenu addWindowMenu(String label) {
        WindowMenu windowMenu = new WindowMenu(label);
        windowMenu.setBackground(getBackground());
        windowMenu.setForeground(getForeground());
        windowMenu.setFont(getFont());
        add(windowMenu);
        return windowMenu;
    }

    public static class WindowMenu extends JMenu {
        public WindowMenu(String s) {
            super(s);
        }

        public WindowMenu addWindowMenu(String label) {
            WindowMenu windowMenu = new WindowMenu(label);
            windowMenu.setBackground(getBackground());
            windowMenu.setForeground(getForeground());
            windowMenu.setFont(getFont());
            add(windowMenu);
            return windowMenu;
        }

        public JMenuItem addMenuItem(String label, Runnable onClickAction) {
            JMenuItem windowMenuItem = new JMenuItem(label);
            windowMenuItem.setBackground(getBackground());
            windowMenuItem.setForeground(getForeground());
            windowMenuItem.setFont(getFont());
            windowMenuItem.addActionListener(l -> onClickAction.run());
            add(windowMenuItem);
            return windowMenuItem;
        }

        public JCheckBoxMenuItem addCheckBoxMenuItem(String label, boolean isSelected, Consumer<Boolean> stateConsumer) {
            JCheckBoxMenuItem windowMenuItem = new JCheckBoxMenuItem(label, isSelected);
            windowMenuItem.setBackground(getBackground());
            windowMenuItem.setForeground(getForeground());
            windowMenuItem.setFont(getFont());
            windowMenuItem.addActionListener(l -> stateConsumer.accept(windowMenuItem.isSelected()));
            add(windowMenuItem);
            return windowMenuItem;
        }
    }
}
