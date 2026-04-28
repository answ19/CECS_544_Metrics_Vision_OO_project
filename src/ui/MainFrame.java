package ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import model.Complexity;
import model.FPType;
import model.ProjectData;
import service.FPService;
import service.FileService;
import model.UcpData;
import service.UcpService;

public class MainFrame extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final FPService fpService = new FPService();
    private final FileService fileService = new FileService();

    private ProjectData pendingProjectData = null;

    private final UcpService ucpService = new UcpService();

    public MainFrame() {
        setTitle("CECS 544 Metrics Suite");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createMenuBar();

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        tabbedPane.addChangeListener(e -> updateTitleFromSelectedTab());

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu metricsMenu = new JMenu("Metrics");
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenu helpMenu = new JMenu("Help");
        JMenu projectCodeMenu = new JMenu("Project code");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        exitItem.addActionListener(e -> System.exit(0));

        JMenu fpMenu = new JMenu("Function Points");
        JMenuItem enterFPItem = new JMenuItem("Enter FP Data");
        fpMenu.add(enterFPItem);
        metricsMenu.add(fpMenu);

        JMenuItem enterUcpItem = new JMenuItem("Use Case Points");
        metricsMenu.add(enterUcpItem);
        enterUcpItem.addActionListener(e -> {
            String tabTitle = JOptionPane.showInputDialog(this, "Enter window name:", "UCP Window", JOptionPane.PLAIN_MESSAGE);
            if (tabTitle == null || tabTitle.trim().isEmpty()) tabTitle = "UCP";

            UcpData newUcpData = new UcpData();
            UcpPanel panel = new UcpPanel(newUcpData, ucpService);

            tabbedPane.addTab(tabTitle, panel);
            tabbedPane.setSelectedComponent(panel);
        });

        JMenuItem languageItem = new JMenuItem("Language");
        preferencesMenu.add(languageItem);

        newItem.addActionListener(e -> {
            NewProjectDialog dialog = new NewProjectDialog(this);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                pendingProjectData = createBlankProjectData(
                        dialog.getProjectName(),
                        dialog.getCreatorName()
                );

                String projectName = pendingProjectData.getProjectName();
                if (projectName == null || projectName.isBlank()) {
                    setTitle("CECS 544 Metrics Suite");
                } else {
                    setTitle("CECS 544 Metrics Suite - " + projectName);
                }
            }
        });

        enterFPItem.addActionListener(e -> {
            if (tabbedPane.getTabCount() == 0) {
                if (pendingProjectData == null) {
                    JOptionPane.showMessageDialog(this,
                            "Please create a new project first using File -> New.",
                            "New Project Required",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                FunctionPointPanel panel = new FunctionPointPanel(pendingProjectData, fpService);
                panel.loadFromProjectData();

                String tabName = pendingProjectData.getProjectName().isBlank()
                        ? "Untitled Project"
                        : pendingProjectData.getProjectName();

                tabbedPane.addTab(tabName, panel);
                tabbedPane.setSelectedComponent(panel);
                setTitle("CECS 544 Metrics Suite - " + tabName);

                pendingProjectData = null;
                return;
            }

            NewProjectDialog dialog = new NewProjectDialog(this);
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                ProjectData newProject = createBlankProjectData(
                        dialog.getProjectName(),
                        dialog.getCreatorName()
                );

                FunctionPointPanel panel = new FunctionPointPanel(newProject, fpService);
                panel.loadFromProjectData();

                String tabName = newProject.getProjectName().isBlank()
                        ? "Untitled Project"
                        : newProject.getProjectName();

                tabbedPane.addTab(tabName, panel);
                tabbedPane.setSelectedComponent(panel);

                setTitle("CECS 544 Metrics Suite - " + tabName);
            }
        });

        openItem.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

            int result = chooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    List<ProjectData> loadedProjects = fileService.loadAll(file);

                    tabbedPane.removeAll();

                    for (ProjectData loaded : loadedProjects) {
                        FunctionPointPanel panel = new FunctionPointPanel(loaded, fpService);
                        panel.loadFromProjectData();

                        String tabName = loaded.getProjectName().isBlank()
                                ? "Untitled Project"
                                : loaded.getProjectName();

                        tabbedPane.addTab(tabName, panel);
                    }

                    if (tabbedPane.getTabCount() > 0) {
                        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                        updateTitleFromSelectedTab();
                    } else {
                        setTitle("CECS 544 Metrics Suite");
                    }

                    pendingProjectData = null;

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Open failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        saveItem.addActionListener(e -> {
            if (tabbedPane.getTabCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "No Function Point project tabs are open.",
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<ProjectData> projectsToSave = new ArrayList<>();

            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component comp = tabbedPane.getComponentAt(i);
                if (comp instanceof FunctionPointPanel panel) {
                    projectsToSave.add(panel.getProjectData());
                }
            }

            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

            int result = chooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();

                try {
                    fileService.saveAll(projectsToSave, file);
                    JOptionPane.showMessageDialog(this, "Project(s) saved successfully.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Save failed: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        languageItem.addActionListener(e -> {
            ProjectData targetProject = null;
            FunctionPointPanel selectedPanel = null;

            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof FunctionPointPanel) {
                selectedPanel = (FunctionPointPanel) selected;
                targetProject = selectedPanel.getProjectData();
            } else if (pendingProjectData != null) {
                targetProject = pendingProjectData;
            }

            if (targetProject == null) {
                JOptionPane.showMessageDialog(this,
                        "Please create a new project first using File -> New.",
                        "New Project Required",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            LanguageDialog dialog = new LanguageDialog(this, targetProject.getLanguage());
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                targetProject.setLanguage(dialog.getSelectedLanguage());

                if (selectedPanel != null) {
                    selectedPanel.setCurrentLanguage(targetProject.getLanguage());
                }

                JOptionPane.showMessageDialog(this,
                        "Language changed to: " + targetProject.getLanguage());
            }
        });

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(metricsMenu);
        menuBar.add(projectCodeMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private ProjectData createBlankProjectData(String projectName, String creatorName) {
        ProjectData data = new ProjectData();

        data.setProjectName(projectName);
        data.setCreatorName(creatorName);
        data.setLanguage("Java");

        for (FPType type : FPType.values()) {
            data.getEntry(type).setCount(0);
            data.getEntry(type).setComplexity(Complexity.AVERAGE);
        }

        for (int i = 0; i < data.getVaf().length; i++) {
            data.getVaf()[i] = 0;
        }

        return data;
    }

    private void updateTitleFromSelectedTab() {
        Component selected = tabbedPane.getSelectedComponent();
        if (selected instanceof FunctionPointPanel panel) {
            String projectName = panel.getProjectData().getProjectName();
            if (projectName == null || projectName.isBlank()) {
                setTitle("CECS 544 Metrics Suite");
            } else {
                setTitle("CECS 544 Metrics Suite - " + projectName);
            }
        } else {
            setTitle("CECS 544 Metrics Suite");
        }
    }
}