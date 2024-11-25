package com.valdisdot.sqlexecutor.ui.gui.notification;

import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Button;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import com.valdisdot.sqlexecutor.ui.gui.element.TextArea;
import com.valdisdot.sqlexecutor.ui.gui.element.*;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class PopUpWindow {
    private JDialog dialog;

    //with abstract executor
    public PopUpWindow(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel,
            Runnable declineAction,
            Consumer<Runnable> actionExecutor,
            Theme theme,
            JFrame parent
    ) {
        int maxComponentWidth = theme.getPopupWidth() - (2 * theme.getGapX());
        List<JComponent> components = new ArrayList<>(4);
        if (message != null && !message.isBlank()) {
            JLabel _message = new Label(
                    message,
                    theme.getPopupMessageFont(),
                    theme.getTextFieldsForeground(),
                    maxComponentWidth
            );
            components.add(_message);
        }
        if (details != null && !details.isBlank()) {
            JTextArea subMessageField = new TextArea(
                    details,
                    theme.getPopupDetailsFont(),
                    theme.getTextFieldsBackground(),
                    theme.getTextFieldsForeground()
            );
            subMessageField.setEditable(false);
            int maxComponentHeight = Math.min(theme.getPopupDetailsHeight(), subMessageField.getPreferredSize().height + theme.getGapY() * 2);
            JScrollPane subMessageWrapper = new ScrollPanel(
                    subMessageField,
                    new Dimension(maxComponentWidth, maxComponentHeight),
                    theme.getScrollBarThickness(),
                    new JTextField().getBorder()
            );
            components.add(subMessageWrapper);
        }
        if (question != null && !question.isBlank()) {
            JLabel _question = new Label(
                    question,
                    theme.getPopupQuestionFont(),
                    theme.getTextFieldsForeground(),
                    maxComponentWidth
            );
            components.add(_question);
        }

        if (components.isEmpty()) {
            return;
        }

        List<Button> buttons = new ArrayList<>(2);
        if (acceptButtonLabel != null && !acceptButtonLabel.isBlank()) {
            Button acceptButton = new Button(
                    acceptButtonLabel,
                    theme.getPopupSuccessButtonBackground(),
                    theme.getPopupSuccessButtonForeground(),
                    theme.getButtonHeight(),
                    theme.getButtonFont()
            );
            if(acceptAction != null) acceptButton.addAction(acceptAction, actionExecutor);
            buttons.add(acceptButton);
        }
        if (declineButtonLabel != null && !declineButtonLabel.isBlank()) {
            Button declineButton = new Button(
                    declineButtonLabel,
                    theme.getPopupErrorButtonBackground(),
                    theme.getPopupErrorButtonForeground(),
                    theme.getButtonHeight(),
                    theme.getButtonFont()
            );
            if(declineAction != null) declineButton.addAction(declineAction, actionExecutor);
            buttons.add(declineButton);
        }

        if (buttons.isEmpty()) {
            return;
        }

        buttons.forEach(b -> b.addAction(this::close));

        JPanel buttonsPanel = new ButtonPanel(
                maxComponentWidth,
                theme.getGapX(),
                theme.getApplicationBackground(),
                buttons
        );
        components.add(buttonsPanel);

        JPanel panel = new JPanel(new MigLayout(
                new LC()
                        .wrap()
                        .insets(String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()))
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));

        panel.setBackground(theme.getApplicationBackground());
        panel.setBorder(new JTextField().getBorder());
        components.forEach(panel::add);

        dialog = new JDialog(parent, true);
        dialog.add(panel);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
    }

    //with current thread executor
    public PopUpWindow(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel,
            Runnable declineAction,
            Theme theme,
            JFrame parent
    ) {
        this(message, details, question, acceptButtonLabel, acceptAction, declineButtonLabel, declineAction, Runnable::run, theme, parent);
    }

    //with thread executor service and execution state processing
    public PopUpWindow(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel,
            Runnable declineAction,
            ExecutorService executorService,
            Consumer<Future<?>> executionStateConsumer,
            Theme theme,
            JFrame parent
    ) {
        this(
                message,
                details,
                question,
                acceptButtonLabel,
                acceptAction,
                declineButtonLabel,
                declineAction,
                executionStateConsumer == null ? executorService::submit : r -> executionStateConsumer.accept(executorService.submit(r)),
                theme,
                parent
        );
    }

    //with thread executor service
    public PopUpWindow(
            String message,
            String details,
            String question,
            String acceptButtonLabel,
            Runnable acceptAction,
            String declineButtonLabel,
            Runnable declineAction,
            ExecutorService executorService,
            Theme theme,
            JFrame parent
    ) {
        this(
                message,
                details,
                question,
                acceptButtonLabel,
                acceptAction,
                declineButtonLabel,
                declineAction,
                executorService,
                null,
                theme,
                parent
        );
    }

    public void show() {
        if (dialog != null) dialog.setVisible(true);
    }

    public void close() {
        if (dialog != null) dialog.dispose();
    }
}
