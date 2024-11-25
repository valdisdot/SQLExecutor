package com.valdisdot.sqlexecutor.ui.gui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.InputStream;
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
    private final int applicationWidth;
    private final int applicationHeight;
    private final int gapX;
    private final int gapY;
    private final int buttonHeight;
    private final Font buttonFont;
    private final Color textFieldsBackground;
    private final Color textFieldsForeground;
    private final int scrollBarThickness;
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
    private final int editorHeight;
    private final Font editorTitleFont;
    private final Font editorSequencePathFont;
    private final Font editorSectionTitleFont;
    private final Font editorSectionLabelFont;
    private final Font editorSectionTextFieldFont;
    private final Font editorSectionTextAreaFont;
    private final int editorSnippetSectionTextAreaHeight;
    private final int editorSequenceSectionTextAreaHeight;
    private final int editorPostSequenceSectionTextAreaHeight;
    private final Color editorResetButtonBackground;
    private final Color editorResetButtonForeground;
    private final Color editorSaveButtonBackground;
    private final Color editorSaveButtonForeground;
    private final Color editorPreviewButtonBackground;
    private final Color editorPreviewButtonForeground;
    private final Color editorExecuteButtonBackground;
    private final Color editorExecuteButtonForeground;
    private final int popupWidth;
    private final Font popupMessageFont;
    private final Font popupDetailsFont;
    private final int popupDetailsHeight;
    private final Font popupQuestionFont;
    private final Color popupSuccessButtonBackground;
    private final Color popupSuccessButtonForeground;
    private final Color popupErrorButtonBackground;
    private final Color popupErrorButtonForeground;
    private Image applicationIcon;

    public Theme() {
        this(
                new Properties() {
                    {
                        try {
                            this.load(ClassLoader.getSystemResourceAsStream("theme.properties"));
                        } catch (Exception ignored) {
                        }
                    }
                },
                ClassLoader.getSystemResourceAsStream("icon.png")
        );
    }

    /**
     * Creates a Theme from the given properties, initializing all fields with default values.
     * If a value is provided in the properties, it will override the default.
     *
     * @param themeProperties a Properties object containing theme configuration values.
     */
    public Theme(Properties themeProperties, InputStream iconInputStream) {
        applicationBackground = getColor(themeProperties.getProperty("application.background"), Color.WHITE);
        String applicationFontName = getFontName(themeProperties.getProperty("application.font.name"), "Arial");
        applicationWidth = getInteger(themeProperties.getProperty("application.size.width"), (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.666));
        applicationHeight = getInteger(themeProperties.getProperty("application.size.height"), (int) (Toolkit.getDefaultToolkit().getScreenSize().height * 0.8));
        gapX = getInteger(themeProperties.getProperty("application.components.gap.x"), 5);
        gapY = getInteger(themeProperties.getProperty("application.components.gap.y"), 4);
        buttonHeight = getInteger(themeProperties.getProperty("application.button.size.height"), 25);
        buttonFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("application.button.font.size"), 12));
        textFieldsBackground = getColor(themeProperties.getProperty("application.textfields.background"), new Color(0xE3EFED));
        textFieldsForeground = getColor(themeProperties.getProperty("application.textfields.foreground"), Color.BLACK);
        scrollBarThickness = getInteger(themeProperties.getProperty("application.scrollbar.thickness"), 10);
        menuTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("menu.title.font.size"), 13));
        //4 * gapX <- between frame and menu container + 2 * between menu container and menu item + between menu container and editor container
        menuWidth = getIntegerFromProportion(applicationWidth - (4 * gapX) - scrollBarThickness, themeProperties.getProperty("application.panels.proportions"), 1, 3);
        menuSelectButtonBackground = getColor(themeProperties.getProperty("menu.button.select.background"), Color.GREEN);
        menuSelectButtonForeground = getColor(themeProperties.getProperty("menu.button.select.foreground"), Color.BLACK);
        badgeColors = makeBadgeColors(themeProperties.getProperty("badge.colors"), Color.GREEN, Color.ORANGE, Color.CYAN, Color.YELLOW, Color.PINK);
        badgeForeground = getColor(themeProperties.getProperty("badge.foreground"), Color.BLACK);
        badgeFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("badge.font.size"), 12));
        badgeLines = getInteger(themeProperties.getProperty("badge.lines"), 1);
        badgePadding = getInteger(themeProperties.getProperty("badge.padding"), 5);
        //4 * gapX <- between menu container and editor container + 2 * between editor container and its element + between editor container and frame
        //2 * scrollSliderWidth <- scrollSliderWidth of menu container + scrollSliderWidth of editor container
        editorWidth = applicationWidth - (4 * gapX) - menuWidth - (2 * scrollBarThickness);
        editorHeight = applicationHeight - (2 * gapY) - buttonHeight;
        editorTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("editor.title.font.size"), 14));
        editorSequencePathFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.path.font.size"), 11));
        editorSectionTitleFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("editor.section.title.font.size"), 13));
        editorSectionLabelFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.label.font.size"), 12));
        editorSectionTextFieldFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.field.single.font.size"), 13));
        editorSectionTextAreaFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("editor.section.field.area.font.size"), 13));
        editorSnippetSectionTextAreaHeight = getInteger(themeProperties.getProperty("editor.snippet.field.area.size.height"), 70);
        editorSequenceSectionTextAreaHeight = getInteger(themeProperties.getProperty("editor.sequence.field.area.size.height"), 150);
        editorPostSequenceSectionTextAreaHeight = getInteger(themeProperties.getProperty("editor.postsequence.field.area.size.height"), 200);
        editorResetButtonBackground = getColor(themeProperties.getProperty("editor.button.reset.background"), Color.GREEN);
        editorResetButtonForeground = getColor(themeProperties.getProperty("editor.button.reset.foreground"), Color.BLACK);
        editorSaveButtonBackground = getColor(themeProperties.getProperty("editor.button.save.background"), Color.ORANGE);
        editorSaveButtonForeground = getColor(themeProperties.getProperty("editor.button.save.foreground"), Color.BLACK);
        editorPreviewButtonBackground = getColor(themeProperties.getProperty("editor.button.preview.background"), Color.CYAN);
        editorPreviewButtonForeground = getColor(themeProperties.getProperty("editor.button.preview.foreground"), Color.BLACK);
        editorExecuteButtonBackground = getColor(themeProperties.getProperty("editor.button.execute.background"), Color.YELLOW);
        editorExecuteButtonForeground = getColor(themeProperties.getProperty("editor.button.execute.foreground"), Color.BLACK);
        popupWidth = getInteger(themeProperties.getProperty("popup.size.width"), 400);
        popupMessageFont = new Font(applicationFontName, Font.BOLD, getInteger(themeProperties.getProperty("popup.message.font.size"), 13));
        popupDetailsFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("popup.details.font.size"), 12));
        popupDetailsHeight = getInteger(themeProperties.getProperty("popup.details.size.height"), 120);
        popupQuestionFont = new Font(applicationFontName, Font.PLAIN, getInteger(themeProperties.getProperty("popup.question.font.size"), 13));
        popupSuccessButtonBackground = getColor(themeProperties.getProperty("popup.button.success.background"), Color.GREEN);
        popupSuccessButtonForeground = getColor(themeProperties.getProperty("popup.button.success.foreground"), Color.BLACK);
        popupErrorButtonBackground = getColor(themeProperties.getProperty("popup.button.error.background"), Color.ORANGE);
        popupErrorButtonForeground = getColor(themeProperties.getProperty("popup.button.error.foreground"), Color.BLACK);
        try {
            applicationIcon = ImageIO.read(iconInputStream);
        } catch (Exception ignored) {
        }
    }

    private Color getColor(String hexValue, Color defaultColor) {
        if (hexValue == null || hexValue.isBlank()) return defaultColor;
        try {
            return new Color(Integer.parseInt(hexValue.trim().replaceAll("#", ""), 16));
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
            return Integer.parseInt(value.trim());
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

    public int getApplicationWidth() {
        return applicationWidth;
    }

    public int getApplicationHeight() {
        return applicationHeight;
    }

    public int getScrollBarThickness() {
        return scrollBarThickness;
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

    public int getEditorHeight() {
        return editorHeight;
    }

    public Font getEditorTitleFont() {
        return editorTitleFont;
    }

    public Font getEditorSequencePathFont() {
        return editorSequencePathFont;
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

    public int getEditorSnippetSectionTextAreaHeight() {
        return editorSnippetSectionTextAreaHeight;
    }

    public int getEditorSequenceSectionTextAreaHeight() {
        return editorSequenceSectionTextAreaHeight;
    }

    public int getEditorPostSequenceSectionTextAreaHeight() {
        return editorPostSequenceSectionTextAreaHeight;
    }

    public Color getEditorResetButtonBackground() {
        return editorResetButtonBackground;
    }

    public Color getEditorResetButtonForeground() {
        return editorResetButtonForeground;
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

    public int getPopupWidth() {
        return popupWidth;
    }

    public Font getPopupMessageFont() {
        return popupMessageFont;
    }

    public Font getPopupDetailsFont() {
        return popupDetailsFont;
    }

    public int getPopupDetailsHeight() {
        return popupDetailsHeight;
    }

    public Font getPopupQuestionFont() {
        return popupQuestionFont;
    }

    public Color getPopupSuccessButtonBackground() {
        return popupSuccessButtonBackground;
    }

    public Color getPopupSuccessButtonForeground() {
        return popupSuccessButtonForeground;
    }

    public Color getPopupErrorButtonBackground() {
        return popupErrorButtonBackground;
    }

    public Color getPopupErrorButtonForeground() {
        return popupErrorButtonForeground;
    }

    public Image getApplicationIcon() {
        return applicationIcon;
    }

    @Override
    public String toString() {
        return "Theme{" +
                "applicationBackground=" + applicationBackground +
                ", applicationWidth=" + applicationWidth +
                ", applicationHeight=" + applicationHeight +
                ", gapX=" + gapX +
                ", gapY=" + gapY +
                ", buttonHeight=" + buttonHeight +
                ", buttonFont=" + buttonFont +
                ", textFieldsBackground=" + textFieldsBackground +
                ", textFieldsForeground=" + textFieldsForeground +
                ", scrollBarThickness=" + scrollBarThickness +
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
                ", editorSequencePathFont=" + editorSequencePathFont +
                ", editorSectionTitleFont=" + editorSectionTitleFont +
                ", editorSectionLabelFont=" + editorSectionLabelFont +
                ", editorSectionTextFieldFont=" + editorSectionTextFieldFont +
                ", editorSectionTextAreaFont=" + editorSectionTextAreaFont +
                ", editorSnippetSectionTextAreaHeight=" + editorSnippetSectionTextAreaHeight +
                ", editorSequenceSectionTextAreaHeight=" + editorSequenceSectionTextAreaHeight +
                ", editorPostSequenceSectionTextAreaHeight=" + editorPostSequenceSectionTextAreaHeight +
                ", editorResetButtonBackground=" + editorResetButtonBackground +
                ", editorResetButtonForeground=" + editorResetButtonForeground +
                ", editorSaveButtonBackground=" + editorSaveButtonBackground +
                ", editorSaveButtonForeground=" + editorSaveButtonForeground +
                ", editorPreviewButtonBackground=" + editorPreviewButtonBackground +
                ", editorPreviewButtonForeground=" + editorPreviewButtonForeground +
                ", editorExecuteButtonBackground=" + editorExecuteButtonBackground +
                ", editorExecuteButtonForeground=" + editorExecuteButtonForeground +
                ", popupWidth=" + popupWidth +
                ", popupMessageFont=" + popupMessageFont +
                ", popupDetailsFont=" + popupDetailsFont +
                ", popupDetailsHeight=" + popupDetailsHeight +
                ", popupQuestionFont=" + popupQuestionFont +
                ", popupSuccessButtonBackground=" + popupSuccessButtonBackground +
                ", popupSuccessButtonForeground=" + popupSuccessButtonForeground +
                ", popupErrorButtonBackground=" + popupErrorButtonBackground +
                ", popupErrorButtonForeground=" + popupErrorButtonForeground +
                '}';
    }
}
