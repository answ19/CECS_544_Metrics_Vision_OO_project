package ui;

import javax.swing.*;
        import java.awt.*;
import model.ProjectData;
import service.FPService;
import model.*;
import service.FileService;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MainFrame extends JFrame {

    private JPanel mainPanel;

    private final ProjectData projectData = new ProjectData();

    private final FPService fpService = new FPService();

    private final FileService fileService = new FileService();

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
            FunctionPointPanel panel = new FunctionPointPanel(projectData, fpService);
            panel.loadFromProjectData();

            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.revalidate();
            mainPanel.repaint();
        });

        fpMenu.add(enterFPItem);
        metricsMenu.add(fpMenu);

        // Preferences Menu
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem languageItem = new JMenuItem("Language");
        preferencesMenu.add(languageItem);
        languageItem.addActionListener(e -> {
            LanguageDialog dialog = new LanguageDialog(this, projectData.getLanguage());
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                projectData.setLanguage(dialog.getSelectedLanguage());
                JOptionPane.showMessageDialog(this,
                        "Language changed to: " + projectData.getLanguage());
            }
        });

        saveItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

            int result = chooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    fileService.save(projectData, file);
                    JOptionPane.showMessageDialog(this, "Project saved successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Save failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        openItem.addActionListener(e -> {

            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    ProjectData loaded = fileService.load(file);
                    projectData.setProjectName(loaded.getProjectName());
                    projectData.setCreatorName(loaded.getCreatorName());
                    projectData.setLanguage(loaded.getLanguage());

                    for (FPType type : FPType.values()) {
                        projectData.getEntry(type).setCount(loaded.getEntry(type).getCount());
                        projectData.getEntry(type).setComplexity(loaded.getEntry(type).getComplexity());
                    }

                    System.arraycopy(loaded.getVaf(), 0, projectData.getVaf(), 0, 14);

                    mainPanel.removeAll();

                    FunctionPointPanel panel = new FunctionPointPanel(projectData, fpService);
                    panel.loadFromProjectData();

                    mainPanel.add(panel, BorderLayout.CENTER);

                    mainPanel.revalidate();
                    mainPanel.repaint();

                    setTitle("CECS 544 Metrics Suite - " + projectData.getProjectName());

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Open failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        // Help Menu
        JMenu helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(metricsMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
        newItem.addActionListener(e -> {
            NewProjectDialog dialog = new NewProjectDialog(this);
            dialog.setVisible(true);

            if (dialog.isSaved()) {

                projectData.setProjectName(dialog.getProjectName());
                projectData.setCreatorName(dialog.getCreatorName());
                projectData.setLanguage("Java");

                for (FPType type : FPType.values()) {
                    projectData.getEntry(type).setCount(0);
                    projectData.getEntry(type).setComplexity(Complexity.AVERAGE);
                }

                for (int i = 0; i < projectData.getVaf().length; i++) {
                    projectData.getVaf()[i] = 0;
                }

                setTitle("CECS 544 Metrics Suite - " + projectData.getProjectName());

                mainPanel.removeAll();
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });


    }
}