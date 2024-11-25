package com.valdisdot.sqlexecutor.ui.gui.element;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * A customizable button with support for synchronous and asynchronous actions.
 * Provides constructors for different use cases, including:
 * - Running an action on the current thread.
 * - Running an action asynchronously using an {@link ExecutorService}.
 */
public class Button extends JButton {
    /**
     * Creates a button that executes a {@link Runnable} asynchronously using an {@link ExecutorService}.
     *
     * @param label           the label of the button.
     * @param onClickAction   the action to execute on click.
     * @param actionExecutor  the executor service to handle the action asynchronously.
     * @param backgroundColor the background color of the button.
     * @param foregroundColor the foreground (text) color of the button.
     * @param height          the height of the button.
     * @param font            the font of the button label.
     */
    public Button(
            String label,
            Runnable onClickAction,
            ExecutorService actionExecutor,
            Color backgroundColor,
            Color foregroundColor,
            int height,
            Font font
    ) {
        this(label, backgroundColor, foregroundColor, height, font);
        addAction(onClickAction, actionExecutor);
    }

    /**
     * Creates a button that executes a {@link Runnable} synchronously on the current thread.
     *
     * @param label           the label of the button.
     * @param onClickAction   the action to execute on click.
     * @param backgroundColor the background color of the button.
     * @param foregroundColor the foreground (text) color of the button.
     * @param height          the height of the button.
     * @param font            the font of the button label.
     */
    public Button(
            String label,
            Runnable onClickAction,
            Color backgroundColor,
            Color foregroundColor,
            int height,
            Font font
    ) {
        this(label, backgroundColor, foregroundColor, height, font);
        addAction(onClickAction);
    }

    /**
     * Creates a button with the specified label and styling.
     *
     * @param label           the label of the button.
     * @param backgroundColor the background color of the button.
     * @param foregroundColor the foreground (text) color of the button.
     * @param height          the height of the button.
     * @param font            the font of the button label.
     */
    public Button(
            String label,
            Color backgroundColor,
            Color foregroundColor,
            int height,
            Font font
    ) {
        super(label);
        initializeButton(backgroundColor, foregroundColor, height, font);
    }

    /**
     * Adds a synchronous action to the button.
     *
     * @param action the {@link Runnable} to execute on click.
     */
    public void addAction(Runnable action) {
        addActionListener(e -> action.run());
    }

    /**
     * Adds an asynchronous action to the button using an {@link ExecutorService}.
     *
     * @param action          the {@link Runnable} to execute on click.
     * @param executorService the executor service to handle the action asynchronously.
     */
    public void addAction(Runnable action, ExecutorService executorService) {
        addActionListener(e -> executorService.submit(action));
    }

    public void addAction(Runnable action, Consumer<Runnable> actionExecutor) {
        addActionListener(e -> actionExecutor.accept(action));
    }

    /**
     * Initializes the button's appearance and styling.
     *
     * @param backgroundColor the background color of the button.
     * @param foregroundColor the foreground (text) color of the button.
     * @param height          the height of the button.
     * @param font            the font of the button label.
     */
    private void initializeButton(Color backgroundColor, Color foregroundColor, int height, Font font) {
        setMargin(new Insets(2, 4, 2, 4));
        setPreferredSize(new Dimension(getPreferredSize().width, height));
        setBackground(backgroundColor);
        setForeground(foregroundColor);
        setFocusPainted(false);
        setFont(font);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }
}
