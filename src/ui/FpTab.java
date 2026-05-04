package ui;

import model.ProjectData;

import javax.swing.*;
        import java.awt.*;

public class FpTab extends JPanel {
    private final FunctionPointPanel functionPointPanel;

    public FpTab(FunctionPointPanel functionPointPanel) {
        super(new BorderLayout());
        this.functionPointPanel = functionPointPanel;
        add(functionPointPanel, BorderLayout.CENTER);
    }

    public ProjectData getProjectData() {
        return functionPointPanel.getProjectData();
    }

    public FunctionPointPanel getFunctionPointPanel() {
        return functionPointPanel;
    }
}
