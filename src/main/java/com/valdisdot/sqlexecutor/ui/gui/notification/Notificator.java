package com.valdisdot.sqlexecutor.ui.gui.notification;

import com.valdisdot.sqlexecutor.ui.gui.Theme;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class Notificator {
    private ExecutorService executorService;
    private Consumer<Future<?>> executionProcessStatusSubscriber;
    private Theme theme;

    public Notificator(ExecutorService executorService, Theme theme) {
        theme(executorService, null, theme);
    }

    private void theme(ExecutorService executorService, Consumer<Future<?>> executionProcessStatusSubscriber, Theme theme) {
        this.executorService = Objects.requireNonNull(executorService, "Executor service is null");
        this.executionProcessStatusSubscriber = executionProcessStatusSubscriber;
        this.theme = Objects.requireNonNull(theme, "Theme is null");
    }

    public void notification(
            Status status,
            String message,
            String details,
            String buttonLabel
    ) {
        notification(status, message, details, buttonLabel, () -> {
        });
    }

    public void notification(
            Status status,
            String message,
            String details,
            String okButtonLabel,
            Runnable onClickAction
    ) {
        if (status != null) {
            switch (status) {
                case SUCCESS: {
                    option(message, details, null, okButtonLabel, onClickAction, null, null);
                    break;
                }
                case ERROR: {
                    option(message, details, null, null, null, okButtonLabel, onClickAction);
                    break;
                }
            }
        }
    }

    public void option(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel
    ) {
        option(message, details, question, acceptButtonLabel, acceptAction, declineButtonLabel, () -> {
        });
    }

    public void option(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel,
            Runnable declineAction
    ) {
        new PopUpWindow(
                message,
                details,
                question,
                acceptButtonLabel,
                acceptAction,
                declineButtonLabel,
                declineAction,
                executorService,
                executionProcessStatusSubscriber,
                theme,
                null
        ).show();
    }

    public enum Status {
        SUCCESS, ERROR
    }
}
