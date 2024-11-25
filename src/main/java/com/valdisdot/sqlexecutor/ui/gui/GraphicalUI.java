package com.valdisdot.sqlexecutor.ui.gui;

import com.valdisdot.sqlexecutor.configuration.ApplicationConfig;
import com.valdisdot.sqlexecutor.configuration.ConnectionConfig;
import com.valdisdot.sqlexecutor.executor.SequenceExecutor;
import com.valdisdot.sqlexecutor.executor.database.DatabaseManager;
import com.valdisdot.sqlexecutor.executor.database.DatabaseManagerException;
import com.valdisdot.sqlexecutor.sequence.SequenceHolder;
import com.valdisdot.sqlexecutor.ui.gui.controller.Controller;
import com.valdisdot.sqlexecutor.ui.gui.element.ScrollPanel;
import com.valdisdot.sqlexecutor.ui.gui.notification.Notificator;
import com.valdisdot.sqlexecutor.ui.gui.part.EditorPanel;
import com.valdisdot.sqlexecutor.ui.gui.part.menu.Menu;
import net.miginfocom.layout.LC;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GraphicalUI implements Runnable {
    private final Logger logger;
    private Localization localization;
    private Theme theme;
    private Controller controller;
    private ApplicationConfig applicationConfig;
    private ExecutorService executorService;

    private JFrame frame;
    private JPanel rootPanel;
    private Map<SequenceHolder, EditorPanel> holderEditorPanels;
    private EditorPanel last;

    public GraphicalUI(
            ApplicationConfig applicationConfig,
            List<ConnectionConfig> connectionConfigs,
            Localization localization,
            Theme theme
    ) throws DatabaseManagerException {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.executorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
        this.theme = theme;
        this.localization = localization;
        this.controller = new Controller(new SequenceExecutor(new DatabaseManager(connectionConfigs, applicationConfig), applicationConfig), new Notificator(executorService, theme), localization);
        this.applicationConfig = applicationConfig;
        this.holderEditorPanels = new LinkedHashMap<>();
        this.rootPanel = new JPanel(new MigLayout(
                new LC()
                        .insets(String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()), String.valueOf(theme.getGapX()))
                        .gridGap(String.valueOf(theme.getGapX()), String.valueOf(theme.getGapY()))
        ));
        rootPanel.setBackground(theme.getApplicationBackground());
        this.frame = new JFrame("SQL Executor GUI");
        frame.setIconImage(theme.getApplicationIcon());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(rootPanel);
    }

    private void initUI() {
        File[] files = applicationConfig.getInputDirectory().listFiles();
        if (files == null || files.length == 0) {
            Exception e = new FileNotFoundException(String.format("Directory '%s' is empty", applicationConfig.getInputDirectory()));
            logger.error("Error during GUI initialization", e);
            controller.showErrorNotification(e, () -> System.exit(0));
        }
        List<SequenceHolder> sequenceHolders = controller.loadSequenceHolders(Arrays.asList(files));

        holderEditorPanels.clear();
        rootPanel.removeAll();

        Menu menu = new Menu(
                localization.getTranslation("menu.button"),
                sequenceHolder -> {
                    rootPanel.remove(last);
                    last = holderEditorPanels.get(sequenceHolder);
                    rootPanel.add(last);
                    rootPanel.repaint();
                    rootPanel.revalidate();
                    frame.pack();
                },
                executorService,
                theme
        );

        for (SequenceHolder holder : sequenceHolders) {
            menu.addSequenceHolder(holder);
            last = new EditorPanel(holder, controller, executorService, theme, localization);
            holderEditorPanels.put(holder, last);
        }
        rootPanel.add(new ScrollPanel(
                menu,
                new Dimension(menu.getPreferredSize().width + theme.getGapX(), last.getPreferredSize().height),
                theme.getScrollBarThickness()
        ));
        rootPanel.add(last);
        frame.revalidate();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void run() {
        initUI();
    }
}
