package ui;

import model.*;
import service.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private final JTabbedPane tabbedPane = new JTabbedPane();

    private final FPService fpService = new FPService();
    private final FileService fileService = new FileService();

    private final UcpService ucpService = new UcpService();

    private String currentProjectName = "";
    private String currentCreatorName = "";
    private String currentLanguage = "Java";

    public MainFrame() {
        setTitle("CECS 544 Metrics Suite");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);

        createMenuBar();

        setVisible(true);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenu metricsMenu = new JMenu("Metrics");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenuItem languageItem = new JMenuItem("Languages");
        preferencesMenu.add(languageItem);

        JMenu fpMenu = new JMenu("Function Points");
        JMenuItem enterFPItem = new JMenuItem("Enter FP Data");
        fpMenu.add(enterFPItem);

        JMenuItem enterUcpItem = new JMenuItem("Use Case Points");

        metricsMenu.add(fpMenu);
        metricsMenu.add(enterUcpItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(preferencesMenu);
        menuBar.add(metricsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        exitItem.addActionListener(e -> System.exit(0));

        newItem.addActionListener(e -> createNewProject());

        enterFPItem.addActionListener(e -> createFunctionPointTab());

        enterUcpItem.addActionListener(e -> createUcpTab());

        languageItem.addActionListener(e -> changeLanguage());

        saveItem.addActionListener(e -> saveProject());

        openItem.addActionListener(e -> openProject());
    }

    private void createNewProject() {
        NewProjectDialog dialog = new NewProjectDialog(this);
        dialog.setVisible(true);

        if (!dialog.isSaved()) return;

        if (dialog.getProjectName() == null || dialog.getProjectName().isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Project Name is required.",
                    "Missing Required Input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentProjectName = dialog.getProjectName();
        currentCreatorName = dialog.getCreatorName();
        currentLanguage = "Java";

        tabbedPane.removeAll();

        setTitle("CECS 544 Metrics Suite - " + currentProjectName);
    }

    private void createFunctionPointTab() {
        if (currentProjectName == null || currentProjectName.isBlank()) {
            JOptionPane.showMessageDialog(this,
                    "Please create a project first using File -> New.",
                    "Project Required",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String paneName = JOptionPane.showInputDialog(
                this,
                "Enter FP pane name:",
                "New Function Point Pane",
                JOptionPane.PLAIN_MESSAGE
        );

        if (paneName == null || paneName.isBlank()) return;

        ProjectData data = new ProjectData();
        data.setProjectName(currentProjectName);
        data.setCreatorName(currentCreatorName);
        data.setLanguage(currentLanguage);
        data.setPaneName(paneName);

        FunctionPointPanel panel = new FunctionPointPanel(data, fpService);
        panel.loadFromProjectData();

        tabbedPane.addTab(paneName, panel);
        tabbedPane.setSelectedComponent(panel);

        setTitle("CECS 544 Metrics Suite - " + currentProjectName);
    }

    private void createUcpTab() {
        String paneName = JOptionPane.showInputDialog(
                this,
                "Enter UCP pane name:",
                "New UCP Pane",
                JOptionPane.PLAIN_MESSAGE
        );

        if (paneName == null || paneName.isBlank()) {
            paneName = "UCP";
        }

        UcpData data = new UcpData();
        data.setTabName(paneName);

        UcpPanel panel = new UcpPanel(data, ucpService);

        tabbedPane.addTab(paneName, panel);
        tabbedPane.setSelectedComponent(panel);
    }

    private void changeLanguage() {
        LanguageDialog dialog = new LanguageDialog(this, currentLanguage);
        dialog.setVisible(true);

        if (!dialog.isSaved()) return;

        currentLanguage = dialog.getSelectedLanguage();

        Component selected = tabbedPane.getSelectedComponent();

        if (selected instanceof FunctionPointPanel fpPanel) {
            fpPanel.getProjectData().setLanguage(currentLanguage);
            fpPanel.setCurrentLanguage(currentLanguage);
        }

        JOptionPane.showMessageDialog(this,
                "Language changed to: " + currentLanguage);
    }
    private FunctionPointPanel findFunctionPointPanel(Component comp) {
        if (comp instanceof FunctionPointPanel fpPanel) {
            return fpPanel;
        }

        if (comp instanceof Container container) {
            for (Component child : container.getComponents()) {
                FunctionPointPanel result = findFunctionPointPanel(child);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }
    private void saveProject() {
        List<ProjectData> projectsToSave = new ArrayList<>();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            Component comp = tabbedPane.getComponentAt(i);

            if (comp instanceof FunctionPointPanel fpPanel) {
                ProjectData data = fpPanel.getProjectData();

                data.setMetricType("FP");
                data.setProjectName(currentProjectName);
                data.setCreatorName(currentCreatorName);
                data.setLanguage(currentLanguage);
                data.setPaneName(tabbedPane.getTitleAt(i));

                projectsToSave.add(data);
            } else if (comp instanceof UcpPanel ucpPanel) {
                ProjectData data = new ProjectData();

                data.setMetricType("UCP");
                data.setProjectName(currentProjectName);
                data.setCreatorName(currentCreatorName);
                data.setLanguage(currentLanguage);
                data.setPaneName(tabbedPane.getTitleAt(i));
                data.setUcpData(ucpPanel.getUcpData());

                projectsToSave.add(data);
            }
        }
        if (projectsToSave.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No FP or UCP panes found to save.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

        int result = chooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                fileService.saveAll(projectsToSave, file);

                JOptionPane.showMessageDialog(this,
                        "Project saved successfully.");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Save failed: " + ex.getMessage(),
                        "Save Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Metrics Suite Files (*.ms)", "ms"));

        int result = chooser.showOpenDialog(this);

        if (result != JFileChooser.APPROVE_OPTION) return;

        try {
            File file = chooser.getSelectedFile();

            List<ProjectData> loadedProjects = fileService.loadAll(file);

            tabbedPane.removeAll();

            if (loadedProjects.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No project data found in file.",
                        "Open Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProjectData first = loadedProjects.get(0);
            currentProjectName = first.getProjectName();
            currentCreatorName = first.getCreatorName();
            currentLanguage = first.getLanguage();

            for (ProjectData data : loadedProjects) {

                String paneName = data.getPaneName();
                if (paneName == null || paneName.isBlank()) {
                    paneName = "Untitled Pane";
                }

                if ("UCP".equalsIgnoreCase(data.getMetricType())) {
                    UcpData ucpData = data.getUcpData();

                    if (ucpData == null) {
                        ucpData = new UcpData();
                        data.setUcpData(ucpData);
                    }

                    UcpPanel panel = new UcpPanel(ucpData, ucpService);
                    tabbedPane.addTab(paneName, panel);

                } else {
                    FunctionPointPanel panel = new FunctionPointPanel(data, fpService);
                    panel.loadFromProjectData();
                    tabbedPane.addTab(paneName, panel);
                }
            }

            tabbedPane.setSelectedIndex(0);

            if (currentProjectName == null || currentProjectName.isBlank()) {
                setTitle("CECS 544 Metrics Suite");
            } else {
                setTitle("CECS 544 Metrics Suite - " + currentProjectName);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Open failed: " + ex.getMessage(),
                    "Open Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}