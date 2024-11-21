package com.valdisdot.sqlexecutor.ui.gui.element;

import com.valdisdot.sqlexecutor.util.ActionContainer;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;

/**
 * A customizable button with support for synchronous and asynchronous actions.
 * Provides constructors for different use cases, including:
 * - Running an action on the current thread.
 * - Running an action asynchronously using an {@link ExecutorService}.
 * - Using an {@link ActionContainer} for advanced action management.
 */
public class Button extends JButton {

    /**
     * Creates a button linked to an {@link ActionContainer}, executing its action asynchronously.
     *
     * @param actionContainer the action container providing the action verb and callable.
     * @param actionExecutor  the executor service to handle the action asynchronously.
     * @param backgroundColor the background color of the button.
     * @param foregroundColor the foreground (text) color of the button.
     * @param height          the height of the button.
     * @param font            the font of the button label.
     * @param <T>             the type parameter of the {@link ActionContainer}.
     */
    public <T> Button(
            ActionContainer<T> actionContainer,
            ExecutorService actionExecutor,
            Color backgroundColor,
            Color foregroundColor,
            int height,
            Font font
    ) {
        this(actionContainer.getActionVerb(), backgroundColor, foregroundColor, height, font);
        addAction(actionContainer, actionExecutor);
    }

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

    /**
     * Adds an action from an {@link ActionContainer}, executed asynchronously using an {@link ExecutorService}.
     *
     * @param actionContainer the container providing the callable and consumer for execution.
     * @param actionExecutor  the executor service to handle the callable asynchronously.
     * @param <T>             the type parameter of the {@link ActionContainer}.
     */
    public <T> void addAction(ActionContainer<T> actionContainer, ExecutorService actionExecutor) {
        addActionListener(e -> actionContainer.getActionExecutionConsumer()
                .accept(actionExecutor.submit(actionContainer.getCallable())));
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
    }
}
