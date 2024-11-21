package com.valdisdot.sqlexecutor.ui.gui;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Represents a theme configuration for the application, including UI elements like backgrounds, font sizes,
 * button colors, and other properties that define the visual appearance of the interface.
 * The theme values can be initialized from a Properties object, and default values are provided for all properties.
 */
public class Theme {
    private final Color applicationBackground;
    private final Dimension applicationSize;
    private final int gapX;
    private final int gapY;
    private final int buttonHeight;
    private final Font buttonFont;
    private final Color textFieldsBackground;
    private final Color textFieldsForeground;
    private final Font menuTitleFont;
    private final int menuWidth;
    private final Color menuSelectButtonBackground;
    private final Color menuSelectButtonForeground;
    private final List<Color> badgeColors;
    private final Color badgeForeground;
    private final Font badgeFont;
    private final int badgeLines;
    private final int badgePadding;
    private final int editorWidth;
    private final Font editorTitleFont;
    private final Font editorSectionTitleFont;
    private final Font editorSectionLabelFont;
    private final Font editorSectionTextFieldFont;
    private final Font editorSectionTextAreaFont;
    private final int editorSectionTextAreaHeight;
    private final Color editorOpenButtonBackground;
    private final Color editorOpenButtonForeground;
    private final Color editorSaveButtonBackground;
    private final Color editorSaveButtonForeground;
    private final Color editorPreviewButtonBackground;
    private final Color editorPreviewButtonForeground;
    private final Color editorExecuteButtonBackground;
    private final Color editorExecuteButtonForeground;
    private final int dialogWidth;
    private final Font dialogMessageFont;
    private final Font dialogSubMessageFont;
    private final int dialogSubMessageHeight;
    private final Font dialogQuestionFont;
    private final Color dialogAcceptButtonBackground;
    private final Color dialogAcceptButtonForeground;
    private final Color dialogDeclineButtonBackground;
    private final Color dialogDeclineButtonForeground;

    /**
     * Creates a Theme from the given properties, initializing all fields with default values.
     * If a value is provided in the properties, it will override the default.
     *
     * @param themeProperties a Properties object containing theme configuration values.
     */
    public Theme(Properties themeProperties) {
        applicationBackground = getColor(themeProperties.getProperty("application.background"), Color.WHITE);
        String applicationFontName = getFontName(themeProperties.getProperty("application.font.name"), "Arial");
        applicationSize = new Dimension(
                getInteger(themeProperties.getProperty("application.size.width"), (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.666)),
                getInteger("application.size.height", (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8))
        );
        gapX = getInteger(themeProperties.getProperty("application.components.gap.x"), 5);
        gapY = getInteger(themeProperties.getProperty("application.components.gap.y"), 4);
        buttonHeight = getInteger(themeProperties.getProperty("application.button.size.height"), 25);
        buttonFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("application.button.font.size"), 12));
        textFieldsBackground = getColor(themeProperties.getProperty("application.textfields.background"), new Color(0xE3EFED));
        textFieldsForeground = getColor(themeProperties.getProperty("application.textfields.foreground"), Color.BLACK);
        menuTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("menu.title.font.size"), 13));
        menuWidth = getIntegerFromProportion(applicationSize.width - (3 * gapX), themeProperties.getProperty("application.panels.proportions"), 1, 3);
        menuSelectButtonBackground = getColor(themeProperties.getProperty("menu.button.select.background"), Color.GREEN);
        menuSelectButtonForeground = getColor(themeProperties.getProperty("menu.button.select.foreground"), Color.BLACK);
        badgeColors = makeBadgeColors(themeProperties.getProperty("badge.colors"), Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW, Color.PINK);
        badgeForeground = getColor(themeProperties.getProperty("badge.foreground"), Color.BLACK);
        badgeFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("badge.font.size"), 12));
        badgeLines = getInteger(themeProperties.getProperty("badge.lines"), 1);
        badgePadding = getInteger(themeProperties.getProperty("badge.padding"), 5);
        editorWidth = applicationSize.width - (3 * gapX) - menuWidth;
        editorTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("editor.title.font.size"), 14));
        editorSectionTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("editor.section.title.font.size"), 13));
        editorSectionLabelFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.label.font.size"), 12));
        editorSectionTextFieldFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.field.single.font.size"), 13));
        editorSectionTextAreaFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.field.area.font.size"), 13));
        editorSectionTextAreaHeight = getInteger(themeProperties.getProperty("editor.section.field.area.size.height"), 350);
        editorOpenButtonBackground = getColor(themeProperties.getProperty("editor.button.open.background"), Color.GREEN);
        editorOpenButtonForeground = getColor(themeProperties.getProperty("editor.button.open.foreground"), Color.BLACK);
        editorSaveButtonBackground = getColor(themeProperties.getProperty("editor.button.save.background"), Color.ORANGE);
        editorSaveButtonForeground = getColor(themeProperties.getProperty("editor.button.save.foreground"), Color.BLACK);
        editorPreviewButtonBackground = getColor(themeProperties.getProperty("editor.button.preview.background"), Color.CYAN);
        editorPreviewButtonForeground = getColor(themeProperties.getProperty("editor.button.preview.foreground"), Color.BLACK);
        editorExecuteButtonBackground = getColor(themeProperties.getProperty("editor.button.execute.background"), Color.YELLOW);
        editorExecuteButtonForeground = getColor(themeProperties.getProperty("editor.button.execute.foreground"), Color.BLACK);
        dialogWidth = getInteger(themeProperties.getProperty("dialog.size.width"), 400);
        dialogMessageFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("dialog.message.font.size"), 13));
        dialogSubMessageFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("dialog.submessage.font.size"), 12));
        dialogSubMessageHeight = getInteger(themeProperties.getProperty("dialog.submessage.size.height"), 300);
        dialogQuestionFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("dialog.question.font.size"), 13));
        dialogAcceptButtonBackground = getColor(themeProperties.getProperty("dialog.button.accept.background"), Color.GREEN);
        dialogAcceptButtonForeground = getColor(themeProperties.getProperty("dialog.button.accept.foreground"), Color.BLACK);
        dialogDeclineButtonBackground = getColor(themeProperties.getProperty("dialog.button.decline.background"), Color.ORANGE);
        dialogDeclineButtonForeground = getColor(themeProperties.getProperty("dialog.button.decline.foreground"), Color.BLACK);
    }

    private Color getColor(String hexValue, Color defaultColor) {
        if (hexValue == null || hexValue.isBlank()) return defaultColor;
        try {
            return new Color(Integer.parseInt(hexValue.replaceAll("#", ""), 16));
        } catch (Exception e) {
            return defaultColor;
        }
    }

    private String getFontName(String fontName, String defaultFontName) {
        if (fontName == null || fontName.isBlank()) return defaultFontName;
        return Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts())
                .map(Font::getFontName)
                .filter(name -> name.equalsIgnoreCase(fontName))
                .findAny()
                .orElse(defaultFontName);
    }

    private int getInteger(String value, int defaultInteger) {
        if (value == null || value.isBlank()) return defaultInteger;
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return defaultInteger;
        }
    }

    private int getIntegerFromProportion(int baseInteger, String proportion, int defaultNumerator, int defaultDenominator) {
        try {
            String[] proportions = proportion.split(":");
            return Integer.parseInt(proportions[0].trim()) * baseInteger / Integer.parseInt(proportions[1].trim());
        } catch (Exception e) {
            return defaultNumerator * baseInteger / defaultDenominator;
        }
    }

    private List<Color> makeBadgeColors(String colorList, Color... defaultColors) {
        List<Color> badgeColors = new LinkedList<>();
        if (colorList != null && !colorList.isBlank()) {
            String[] values = colorList.split(",");
            Color color = null;
            for (String value : values) {
                color = getColor(value, null);
                if (color != null) badgeColors.add(color);
            }
        }
        return badgeColors.isEmpty() ? Arrays.asList(defaultColors) : List.copyOf(badgeColors);
    }

    public Color getApplicationBackground() {
        return applicationBackground;
    }

    public Dimension getApplicationSize() {
        return applicationSize;
    }

    public int getGapX() {
        return gapX;
    }

    public int getGapY() {
        return gapY;
    }

    public int getButtonHeight() {
        return buttonHeight;
    }

    public Font getButtonFont() {
        return buttonFont;
    }

    public Color getTextFieldsBackground() {
        return textFieldsBackground;
    }

    public Color getTextFieldsForeground() {
        return textFieldsForeground;
    }

    public Font getMenuTitleFont() {
        return menuTitleFont;
    }

    public int getMenuWidth() {
        return menuWidth;
    }

    public Color getMenuSelectButtonBackground() {
        return menuSelectButtonBackground;
    }

    public Color getMenuSelectButtonForeground() {
        return menuSelectButtonForeground;
    }

    public List<Color> getBadgeColors() {
        return badgeColors;
    }

    public Color getBadgeForeground() {
        return badgeForeground;
    }

    public Font getBadgeFont() {
        return badgeFont;
    }

    public int getBadgeLines() {
        return badgeLines;
    }

    public int getBadgePadding() {
        return badgePadding;
    }

    public int getEditorWidth() {
        return editorWidth;
    }

    public Font getEditorTitleFont() {
        return editorTitleFont;
    }

    public Font getEditorSectionTitleFont() {
        return editorSectionTitleFont;
    }

    public Font getEditorSectionLabelFont() {
        return editorSectionLabelFont;
    }

    public Font getEditorSectionTextFieldFont() {
        return editorSectionTextFieldFont;
    }

    public Font getEditorSectionTextAreaFont() {
        return editorSectionTextAreaFont;
    }

    public int getEditorSectionTextAreaHeight() {
        return editorSectionTextAreaHeight;
    }

    public Color getEditorOpenButtonBackground() {
        return editorOpenButtonBackground;
    }

    public Color getEditorOpenButtonForeground() {
        return editorOpenButtonForeground;
    }

    public Color getEditorSaveButtonBackground() {
        return editorSaveButtonBackground;
    }

    public Color getEditorSaveButtonForeground() {
        return editorSaveButtonForeground;
    }

    public Color getEditorPreviewButtonBackground() {
        return editorPreviewButtonBackground;
    }

    public Color getEditorPreviewButtonForeground() {
        return editorPreviewButtonForeground;
    }

    public Color getEditorExecuteButtonBackground() {
        return editorExecuteButtonBackground;
    }

    public Color getEditorExecuteButtonForeground() {
        return editorExecuteButtonForeground;
    }

    public int getDialogWidth() {
        return dialogWidth;
    }

    public Font getDialogMessageFont() {
        return dialogMessageFont;
    }

    public Font getDialogSubMessageFont() {
        return dialogSubMessageFont;
    }

    public int getDialogSubMessageHeight() {
        return dialogSubMessageHeight;
    }

    public Font getDialogQuestionFont() {
        return dialogQuestionFont;
    }

    public Color getDialogAcceptButtonBackground() {
        return dialogAcceptButtonBackground;
    }

    public Color getDialogAcceptButtonForeground() {
        return dialogAcceptButtonForeground;
    }

    public Color getDialogDeclineButtonBackground() {
        return dialogDeclineButtonBackground;
    }

    public Color getDialogDeclineButtonForeground() {
        return dialogDeclineButtonForeground;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "applicationBackground=" + applicationBackground +
                ", applicationSize=" + applicationSize +
                ", insetX=" + gapX +
                ", insetY=" + gapY +
                ", buttonHeight=" + buttonHeight +
                ", buttonFont=" + buttonFont +
                ", textFieldsBackground=" + textFieldsBackground +
                ", textFieldsForeground=" + textFieldsForeground +
                ", menuTitleFont=" + menuTitleFont +
                ", menuWidth=" + menuWidth +
                ", menuSelectButtonBackground=" + menuSelectButtonBackground +
                ", menuSelectButtonForeground=" + menuSelectButtonForeground +
                ", badgeColors=" + badgeColors +
                ", badgeForeground=" + badgeForeground +
                ", badgeFont=" + badgeFont +
                ", badgeLines=" + badgeLines +
                ", badgePadding=" + badgePadding +
                ", editorWidth=" + editorWidth +
                ", editorTitleFont=" + editorTitleFont +
                ", editorSectionTitleFont=" + editorSectionTitleFont +
                ", editorSectionLabelFont=" + editorSectionLabelFont +
                ", editorSectionTextFieldFont=" + editorSectionTextFieldFont +
                ", editorSectionTextAreaFont=" + editorSectionTextAreaFont +
                ", editorSectionTextAreaHeight=" + editorSectionTextAreaHeight +
                ", editorOpenButtonBackground=" + editorOpenButtonBackground +
                ", editorOpenButtonForeground=" + editorOpenButtonForeground +
                ", editorSaveButtonBackground=" + editorSaveButtonBackground +
                ", editorSaveButtonForeground=" + editorSaveButtonForeground +
                ", editorPreviewButtonBackground=" + editorPreviewButtonBackground +
                ", editorPreviewButtonForeground=" + editorPreviewButtonForeground +
                ", editorExecuteButtonBackground=" + editorExecuteButtonBackground +
                ", editorExecuteButtonForeground=" + editorExecuteButtonForeground +
                ", dialogWidth=" + dialogWidth +
                ", dialogMessageFont=" + dialogMessageFont +
                ", dialogSubMessageFont=" + dialogSubMessageFont +
                ", dialogSubMessageHeight=" + dialogSubMessageHeight +
                ", dialogQuestionFont=" + dialogQuestionFont +
                ", dialogAcceptButtonBackground=" + dialogAcceptButtonBackground +
                ", dialogAcceptButtonForeground=" + dialogAcceptButtonForeground +
                ", dialogDeclineButtonBackground=" + dialogDeclineButtonBackground +
                ", dialogDeclineButtonForeground=" + dialogDeclineButtonForeground +
                '}';
    }
}
