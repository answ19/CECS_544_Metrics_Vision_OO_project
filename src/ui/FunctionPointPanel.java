package ui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import model.*;
import service.FPService;
import service.CodeSizeService;

public class FunctionPointPanel extends JPanel {

    private final Map<String, JTextField> countFields = new LinkedHashMap<>();
    private final Map<String, ButtonGroup> complexityGroups = new LinkedHashMap<>();
    private final Map<String, JTextField> rowTotalFields = new LinkedHashMap<>();

    // Total count shown under the 5 FP asset rows
    private final JTextField totalCountField = new JTextField(8);

    // Bottom/right output fields
    private final JTextField finalFpField = new JTextField(10);
    private final JTextField vafSumField = new JTextField(10);
    private final JTextField currentLanguageField = new JTextField(10);
    private final JTextField codeSizeField = new JTextField(12);

    // Keep old label in case something else still references it
    private final JLabel totalCountValue = new JLabel("—");

    private final JButton computeFpBtn = new JButton("Compute FP");
    private final JButton valueAdjustmentsBtn = new JButton("Value Adjustments");
    private final JButton computeCodeSizeBtn = new JButton("Compute Code Size");
    private final JButton changeLanguageBtn = new JButton("Change Language");

    private final ProjectData projectData;
    private final FPService fpService;
    private final CodeSizeService codeSizeService = new CodeSizeService();

    public FunctionPointPanel(ProjectData data, FPService service) {
        this.projectData = data;
        this.fpService = service;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        totalCountField.setEditable(false);
        finalFpField.setEditable(false);
        vafSumField.setEditable(false);
        currentLanguageField.setEditable(false);
        codeSizeField.setEditable(false);

        currentLanguageField.setText(projectData.getLanguage());

        add(buildTopHeader(), BorderLayout.NORTH);
        add(buildCenterForm(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        wireCompute();
        wireVafDialog();
        wireLanguageDialog();
        wireCodeSize();
        loadFromProjectData();
    }

    private int readNonNegativeInt(JTextField field, String label) {
        String text = field.getText().trim();
        if (text.isEmpty()) return 0;

        int val = Integer.parseInt(text);
        if (val < 0) throw new NumberFormatException(label + " cannot be negative");
        return val;
    }

    private void setEntryFromUI(FPType type, String key, String label) {
        int count = readNonNegativeInt(countFields.get(key), label + " count");

        ButtonModel selection = complexityGroups.get(key).getSelection();
        String cmd = (selection == null) ? "AVERAGE" : selection.getActionCommand();

        projectData.getEntry(type).setCount(count);
        projectData.getEntry(type).setComplexity(Complexity.valueOf(cmd));
    }

    private void updateProjectDataFromUI() {
        setEntryFromUI(FPType.EI, "EI", "EI");
        setEntryFromUI(FPType.EO, "EO", "EO");
        setEntryFromUI(FPType.EQ, "EQ", "EQ");
        setEntryFromUI(FPType.ILF, "ILF", "ILF");
        setEntryFromUI(FPType.EIF, "EIF", "EIF");
    }

    public void loadFromProjectData() {
        loadOne(FPType.EI, "EI");
        loadOne(FPType.EO, "EO");
        loadOne(FPType.EQ, "EQ");
        loadOne(FPType.ILF, "ILF");
        loadOne(FPType.EIF, "EIF");

        updateRowTotal("EI", FPType.EI);
        updateRowTotal("EO", FPType.EO);
        updateRowTotal("EQ", FPType.EQ);
        updateRowTotal("ILF", FPType.ILF);
        updateRowTotal("EIF", FPType.EIF);

        int total = fpService.computeTotal(projectData);
        int vafSum = fpService.computeVafSum(projectData);
        double finalFp = fpService.computeFinalFP(projectData);

        totalCountValue.setText(String.valueOf(total));
        totalCountField.setText(String.valueOf(total));
        vafSumField.setText(String.valueOf(vafSum));
        finalFpField.setText(String.format("%.2f", finalFp));
        currentLanguageField.setText(projectData.getLanguage());
    }

    private void loadOne(FPType type, String key) {
        FPEntry entry = projectData.getEntry(type);

        JTextField field = countFields.get(key);
        if (field != null) {
            field.setText(String.valueOf(entry.getCount()));
        }

        ButtonGroup group = complexityGroups.get(key);
        if (group != null) {
            String complexity = entry.getComplexity().name();

            Enumeration<AbstractButton> buttons = group.getElements();
            while (buttons.hasMoreElements()) {
                AbstractButton button = buttons.nextElement();
                if (button.getActionCommand().equals(complexity)) {
                    button.setSelected(true);
                    break;
                }
            }
        }
    }

    private void wireCompute() {
        computeFpBtn.addActionListener(e -> {
            try {
                updateProjectDataFromUI();

                updateRowTotal("EI", FPType.EI);
                updateRowTotal("EO", FPType.EO);
                updateRowTotal("EQ", FPType.EQ);
                updateRowTotal("ILF", FPType.ILF);
                updateRowTotal("EIF", FPType.EIF);

                int total = fpService.computeTotal(projectData);
                int vafSum = fpService.computeVafSum(projectData);
                double finalFp = fpService.computeFinalFP(projectData);

                totalCountValue.setText(String.valueOf(total));
                totalCountField.setText(String.valueOf(total));
                vafSumField.setText(String.valueOf(vafSum));
                finalFpField.setText(String.format("%.2f", finalFp));
                currentLanguageField.setText(projectData.getLanguage());

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid non-negative integers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void wireVafDialog() {
        valueAdjustmentsBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            VAFDialog dialog = new VAFDialog(w, projectData.getVaf());
            dialog.setVisible(true);

            int total = fpService.computeTotal(projectData);
            int vafSum = fpService.computeVafSum(projectData);
            double finalFp = fpService.computeFinalFP(projectData);

            totalCountField.setText(String.valueOf(total));
            vafSumField.setText(String.valueOf(vafSum));
            finalFpField.setText(String.format("%.2f", finalFp));
        });
    }

    private void wireLanguageDialog() {
        changeLanguageBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            LanguageDialog dialog = new LanguageDialog(w, projectData.getLanguage());
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                projectData.setLanguage(dialog.getSelectedLanguage());
                currentLanguageField.setText(projectData.getLanguage());
            }
        });
    }

    private void wireCodeSize() {
        computeCodeSizeBtn.addActionListener(e -> {
            try {
                updateProjectDataFromUI();

                double fp = fpService.computeFinalFP(projectData);
                int codeSize = codeSizeService.computeCodeSize(projectData.getLanguage(), fp);
                codeSizeField.setText(String.format("%,d", codeSize));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Compute FP first before computing code size.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private JComponent buildTopHeader() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Function Point Data Entry");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        top.add(title, BorderLayout.WEST);

        return top;
    }

    private JComponent buildCenterForm() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Weighting Factors",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addHeader(center, gbc);

        addFpRow(center, gbc, 1, "EI", "External Inputs");
        addFpRow(center, gbc, 2, "EO", "External Outputs");
        addFpRow(center, gbc, 3, "EQ", "External Inquiries");
        addFpRow(center, gbc, 4, "ILF", "Internal Logical Files");
        addFpRow(center, gbc, 5, "EIF", "External Interface Files");
        addTotalCountRow(center, gbc, 6);

        return center;
    }

    private void addHeader(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 0;

        gbc.gridx = 0;
        gbc.weightx = 2.0;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        panel.add(boldLabel("Simple"), gbc);

        gbc.gridx = 3;
        gbc.weightx = 0.5;
        panel.add(boldLabel("Average"), gbc);

        gbc.gridx = 4;
        gbc.weightx = 0.5;
        panel.add(boldLabel("Complex"), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.7;
        panel.add(new JLabel(""), gbc);
    }

    private void addFpRow(JPanel panel, GridBagConstraints gbc, int row, String key, String label) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 2.0;
        panel.add(new JLabel(label), gbc);

        JTextField countField = new JTextField("0", 8);
        countFields.put(key, countField);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(countField, gbc);

        JRadioButton simple = new JRadioButton();
        JRadioButton avg = new JRadioButton();
        JRadioButton complex = new JRadioButton();

// Set weights based on FP type
        if (key.equals("EI")) {
            simple.setText("3");
            avg.setText("4");
            complex.setText("6");
        } else if (key.equals("EO")) {
            simple.setText("4");
            avg.setText("5");
            complex.setText("7");
        } else if (key.equals("EQ")) {
            simple.setText("3");
            avg.setText("4");
            complex.setText("6");
        } else if (key.equals("ILF")) {
            simple.setText("7");
            avg.setText("10");
            complex.setText("15");
        } else if (key.equals("EIF")) {
            simple.setText("5");
            avg.setText("7");
            complex.setText("10");
        }

        ButtonGroup group = new ButtonGroup();
        group.add(simple);
        group.add(avg);
        group.add(complex);
        complexityGroups.put(key, group);

        avg.setSelected(true);

        simple.setActionCommand("SIMPLE");
        avg.setActionCommand("AVERAGE");
        complex.setActionCommand("COMPLEX");

        gbc.gridx = 2;
        gbc.weightx = 0.5;
        panel.add(simple, gbc);

        gbc.gridx = 3;
        panel.add(avg, gbc);

        gbc.gridx = 4;
        panel.add(complex, gbc);

        JTextField rowTotalField = new JTextField(6);
        rowTotalField.setEditable(false);
        rowTotalFields.put(key, rowTotalField);

        gbc.gridx = 5;
        gbc.weightx = 0.7;
        panel.add(rowTotalField, gbc);
    }

    private void addTotalCountRow(JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridy = row;

        gbc.gridx = 0;
        gbc.weightx = 2.0;
        panel.add(boldLabel("Total Count"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 2;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 3;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 4;
        panel.add(new JLabel(""), gbc);

        gbc.gridx = 5;
        gbc.weightx = 0.7;
        panel.add(totalCountField, gbc);
    }

    private JComponent buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(20, 10));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 10));
        buttonPanel.add(computeFpBtn);
        buttonPanel.add(valueAdjustmentsBtn);
        buttonPanel.add(computeCodeSizeBtn);
        buttonPanel.add(changeLanguageBtn);

        JPanel valuePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 10, 6, 10);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0;
        gc.gridy = 0;
        valuePanel.add(new JLabel("Final FP"), gc);
        gc.gridx = 1;
        valuePanel.add(finalFpField, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        valuePanel.add(new JLabel("VAF Sum"), gc);
        gc.gridx = 1;
        valuePanel.add(vafSumField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        valuePanel.add(new JLabel("Current Language"), gc);
        gc.gridx = 1;
        valuePanel.add(currentLanguageField, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        valuePanel.add(new JLabel("Code Size"), gc);
        gc.gridx = 1;
        valuePanel.add(codeSizeField, gc);

        bottom.add(buttonPanel, BorderLayout.WEST);
        bottom.add(valuePanel, BorderLayout.CENTER);

        return bottom;
    }

    private void updateRowTotal(String key, FPType type) {
        FPEntry entry = projectData.getEntry(type);
        int rowTotal = entry.getCount() * getWeight(type, entry.getComplexity());
        JTextField rowField = rowTotalFields.get(key);
        if (rowField != null) {
            rowField.setText(String.valueOf(rowTotal));
        }
    }

    private int getWeight(FPType type, Complexity c) {
        return switch (type) {
            case EI -> (c == Complexity.SIMPLE ? 3 : c == Complexity.AVERAGE ? 4 : 6);
            case EO -> (c == Complexity.SIMPLE ? 4 : c == Complexity.AVERAGE ? 5 : 7);
            case EQ -> (c == Complexity.SIMPLE ? 3 : c == Complexity.AVERAGE ? 4 : 6);
            case ILF -> (c == Complexity.SIMPLE ? 7 : c == Complexity.AVERAGE ? 10 : 15);
            case EIF -> (c == Complexity.SIMPLE ? 5 : c == Complexity.AVERAGE ? 7 : 10);
        };
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    public Map<String, JTextField> getCountFields() {
        return countFields;
    }

    public Map<String, ButtonGroup> getComplexityGroups() {
        return complexityGroups;
    }

    public JLabel getTotalCountValue() {
        return totalCountValue;
    }

    public JButton getComputeBtn() {
        return computeFpBtn;
    }

    public JButton getVafBtn() {
        return valueAdjustmentsBtn;
    }

    public JButton getChangeLanguageBtn() {
        return changeLanguageBtn;
    }

    public void setCurrentLanguage(String lang) {
        currentLanguageField.setText(lang);
    }
    public ProjectData getProjectData() {
        return projectData;
    }
}