package com.valdisdot.sqlexecutor.ui;

import com.valdisdot.sqlexecutor.configuration.ApplicationConfig;
import com.valdisdot.sqlexecutor.configuration.ConfigLoader;
import com.valdisdot.sqlexecutor.configuration.ConnectionConfig;
import com.valdisdot.sqlexecutor.ui.gui.GraphicalUI;
import com.valdisdot.sqlexecutor.ui.gui.Localization;
import com.valdisdot.sqlexecutor.ui.gui.Theme;
import com.valdisdot.sqlexecutor.ui.gui.element.Label;
import com.valdisdot.sqlexecutor.ui.gui.notification.PopUpWindow;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Application {
    public void run(Mode mode, File configFolder) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String version = "ver. 0.1-alpha";
        ApplicationConfig applicationConfig = null;
        List<ConnectionConfig> connectionConfigs = null;
        Exception exception = null;
        if (configFolder == null) {
            logger.info("Config folder is not specified, default 'config' folder is used");
            configFolder = new File("config");
        }
        try {
            List<File> files = Arrays.asList(configFolder.listFiles());
            ConfigLoader configLoader = new ConfigLoader(
                    files.stream().filter(f -> f.getName().equalsIgnoreCase("application.json")).findFirst().orElseThrow(),
                    files.stream().filter(f -> f.getName().equalsIgnoreCase("connection.json")).findFirst().orElseThrow()
            );
            applicationConfig = configLoader.loadApplicationConfig();
            connectionConfigs = configLoader.loadConnectionConfigs();
        } catch (Exception e) {
            logger.error("Error during application initialization", e);
            exception = e;
        }

        if (Mode.CLI.equals(mode) || GraphicsEnvironment.isHeadless()) {
            System.out.printf("SQLExecutor CLI (%s)%n", version);
            logger.info(String.format("SQLExecutor CLI (%s)", version));
            System.out.println("Initialization, please wait...");
            if (applicationConfig == null || connectionConfigs == null)
                System.out.println("Error during application initialization. Check log-file and 'config' directory.");
            else try {
                CommandLineUI commandLineUI = new CommandLineUI(applicationConfig, connectionConfigs);
                Thread cliThread = new Thread(commandLineUI, "cli-execution-thread");
                cliThread.start();
                Runtime.getRuntime().addShutdownHook(new Thread(cliThread::interrupt, "cli-execution-shutdown-hook"));
            } catch (Exception e) {
                logger.error("Error during database initialization", e);
                System.out.println("Error during database initialization. Check log-file and 'config/connection.json' file.");
            }
        } else {
            logger.info(String.format("SQLExecutor GUI (%s)", version));
            JFrame logo = new JFrame();
            JPanel panel = new JPanel();
            panel.setLayout(new MigLayout("wrap"));
            panel.add(new Label("SQL Executor", new Font("Arial", Font.BOLD, 45), Color.BLACK, 300));
            panel.add(new Label(version, new Font("Arial", Font.PLAIN, 12), Color.BLACK, 300));
            logo.add(panel);
            logo.setUndecorated(true);
            logo.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            logo.pack();
            logo.setLocationRelativeTo(null);
            logo.setVisible(true);

            Theme theme = new Theme();
            if (exception != null) {
                new PopUpWindow(
                        "Error during application initialization.",
                        ExceptionUtils.getStackTrace(exception),
                        "Check log-file and 'config' directory.",
                        null,
                        null,
                        "Exit",
                        () -> System.exit(0),
                        theme,
                        logo
                ).show();
            } else {
                try {
                    Localization localization = new Localization(Locale.getDefault());
                    GraphicalUI graphicalUI = new GraphicalUI(applicationConfig, connectionConfigs, localization, theme);
                    SwingUtilities.invokeLater(graphicalUI);
                } catch (Exception e) {
                    logger.error("Error during database initialization", e);
                    new PopUpWindow(
                            "Error during database initialization.",
                            ExceptionUtils.getStackTrace(e),
                            "Check log-file and 'config/connection.json' file.",
                            null,
                            null,
                            "Exit",
                            () -> System.exit(0),
                            theme,
                            logo
                    ).show();
                } finally {
                    logo.dispose();
                }
            }
        }
    }

    public enum Mode {GUI, CLI}
}
