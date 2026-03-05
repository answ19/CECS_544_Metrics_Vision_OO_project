package ui;

import javax.swing.*;
        import java.awt.*;

public class MainFrame extends JFrame {

    private JPanel mainPanel;

    public MainFrame() {
        setTitle("CECS 544 Metrics Suite");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMenuBar();

        mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // Metrics Menu
        JMenu metricsMenu = new JMenu("Metrics");
        JMenu fpMenu = new JMenu("Function Points");
        JMenuItem enterFPItem = new JMenuItem("Enter FP Data");

        enterFPItem.addActionListener(e -> {
            mainPanel.removeAll();
            mainPanel.add(new FunctionPointPanel(), BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        fpMenu.add(enterFPItem);
        metricsMenu.add(fpMenu);

        // Preferences Menu
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem languageItem = new JMenuItem("Language");
        preferencesMenu.add(languageItem);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(metricsMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}