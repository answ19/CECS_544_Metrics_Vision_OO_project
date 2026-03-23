package ui;

import javax.swing.*;
        import javax.swing.border.TitledBorder;
import java.awt.*;
        import java.util.LinkedHashMap;

import java.util.Map;
import model.*;
import service.FPService;
import service.CodeSizeService;



public class FunctionPointPanel extends JPanel {

    // Keep references so Day 3 wiring is easy
    private final Map<String, JTextField> countFields = new LinkedHashMap<>();
    private final Map<String, ButtonGroup> complexityGroups = new LinkedHashMap<>();

    private final JLabel totalCountValue = new JLabel("—");
    private final JLabel vafSumValue = new JLabel("—");
    private final JLabel finalFpValue = new JLabel("—");

    private final JLabel currentLanguageValue = new JLabel("Java");

    private final JButton computeBtn = new JButton("Compute");
    private final JButton vafBtn = new JButton("VAF...");
    private final JButton resetBtn = new JButton("Reset");
    private final JButton changeLanguageBtn = new JButton("Change Language");

    private final ProjectData projectData;
    private final FPService fpService;

    private final JLabel codeSizeValue = new JLabel("—");
    private final JButton computeCodeSizeBtn = new JButton("Compute Code Size");
    private final CodeSizeService codeSizeService = new CodeSizeService();

    public FunctionPointPanel(ProjectData data, FPService service) {
        this.projectData = data;
        this.fpService = service;
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(buildTopHeader(), BorderLayout.NORTH);
        add(buildCenterForm(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        wireResetOnlyForNow();// Day 2: only reset is wired
        wireCompute();
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
        String cmd = (selection == null) ? "AVERAGE" : selection.getActionCommand(); // SIMPLE/AVERAGE/COMPLEX

        projectData.getEntry(type).setCount(count);
        projectData.getEntry(type).setComplexity(Complexity.valueOf(cmd));
    }

    private void updateProjectDataFromUI() {
        setEntryFromUI(FPType.EI,  "EI",  "EI");
        setEntryFromUI(FPType.EO,  "EO",  "EO");
        setEntryFromUI(FPType.EQ,  "EQ",  "EQ");
        setEntryFromUI(FPType.ILF, "ILF", "ILF");
        setEntryFromUI(FPType.EIF, "EIF", "EIF");
    }
    public void loadFromProjectData() {
            loadOne(FPType.EI, "EI");
            loadOne(FPType.EO, "EO");
            loadOne(FPType.EQ, "EQ");
            loadOne(FPType.ILF, "ILF");
            loadOne(FPType.EIF, "EIF");

            int vafSum = fpService.computeVafSum(projectData);
            vafSumValue.setText(String.valueOf(vafSum));

            int total = fpService.computeTotal(projectData);
            totalCountValue.setText(String.valueOf(total));

            double fp = fpService.computeFinalFP(projectData);
            finalFpValue.setText(String.format("%.2f", fp));

            setCurrentLanguage(projectData.getLanguage());}

            private void loadOne(FPType type, String key) {
            FPEntry entry = projectData.getEntry(type);

            JTextField field = countFields.get(key);
            if (field != null) {
                field.setText(String.valueOf(entry.getCount()));
            }

            ButtonGroup group = complexityGroups.get(key);
            if (group != null) {
                String complexity = entry.getComplexity().name();

                for (java.util.Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
                    AbstractButton button = buttons.nextElement();
                    if (button.getActionCommand().equals(complexity)) {
                        button.setSelected(true);
                        break;
                    }
                }
            }
    }
    private void wireCompute() {
        computeBtn.addActionListener(e -> {
            try {
                updateProjectDataFromUI();

                int total = fpService.computeTotal(projectData);
                int vafSum = fpService.computeVafSum(projectData);
                double finalFp = fpService.computeFinalFP(projectData);

                totalCountValue.setText(String.valueOf(total));
                vafSumValue.setText(String.valueOf(vafSum));
                finalFpValue.setText(String.format("%.2f", finalFp));

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid non-negative integers.\n" + ex.getMessage(),
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Something went wrong: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
    private void wireLanguageDialog() {
        changeLanguageBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            LanguageDialog dialog = new LanguageDialog(w, projectData.getLanguage());
            dialog.setVisible(true);

            if (dialog.isSaved()) {
                projectData.setLanguage(dialog.getSelectedLanguage());
                setCurrentLanguage(projectData.getLanguage());
            }
        });
    }
    private void wireCodeSize() {
        computeCodeSizeBtn.addActionListener(e -> {
            try {
                updateProjectDataFromUI();

                double finalFp = fpService.computeFinalFP(projectData);
                String language = projectData.getLanguage();

                if (language == null || language.isBlank()) {
                    JOptionPane.showMessageDialog(this,
                            "Please select a language first.",
                            "Language Required",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int codeSize = codeSizeService.computeCodeSize(language, finalFp);
                codeSizeValue.setText(String.format("%,d", codeSize));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not compute code size: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    private JComponent buildTopHeader() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Function Point Data Entry");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        top.add(title, BorderLayout.WEST);

        JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        langPanel.add(new JLabel("Current Language:"));
        currentLanguageValue.setFont(new Font("Arial", Font.BOLD, 12));
        langPanel.add(currentLanguageValue);
        langPanel.add(changeLanguageBtn); // wiring later
        top.add(langPanel, BorderLayout.EAST);

        return top;
    }

    private JComponent buildCenterForm() {
        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Enter Function Point Counts",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // Header row
        addHeader(center, gbc);

        // Rows: (Key, Label)
        addFpRow(center, gbc, 1, "EI", "External Inputs (EI)");
        addFpRow(center, gbc, 2, "EO", "External Outputs (EO)");
        addFpRow(center, gbc, 3, "EQ", "External Inquiries (EQ)");
        addFpRow(center, gbc, 4, "ILF", "Internal Logical Files (ILF)");
        addFpRow(center, gbc, 5, "EIF", "External Interface Files (EIF)");

        return center;
    }

    private void addHeader(JPanel panel, GridBagConstraints gbc) {
        gbc.gridy = 0;

        gbc.gridx = 0; gbc.weightx = 2;
        panel.add(boldLabel("Category"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(boldLabel("Count"), gbc);

        gbc.gridx = 2; gbc.weightx = 0.6;
        panel.add(boldLabel("Simple"), gbc);

        gbc.gridx = 3; gbc.weightx = 0.6;
        panel.add(boldLabel("Average"), gbc);

        gbc.gridx = 4; gbc.weightx = 0.6;
        panel.add(boldLabel("Complex"), gbc);
    }

    private void addFpRow(JPanel panel, GridBagConstraints gbc, int row, String key, String label) {
        gbc.gridy = row;

        // Category label
        gbc.gridx = 0; gbc.weightx = 2;
        panel.add(new JLabel(label), gbc);

        // Count field
        JTextField countField = new JTextField("0", 8);
        countFields.put(key, countField);

        gbc.gridx = 1; gbc.weightx = 0.6;
        panel.add(countField, gbc);

        // Complexity radios
        JRadioButton simple = new JRadioButton();
        JRadioButton avg = new JRadioButton();
        JRadioButton complex = new JRadioButton();

        ButtonGroup group = new ButtonGroup();
        group.add(simple);
        group.add(avg);
        group.add(complex);
        complexityGroups.put(key, group);

        // Default = Average selected
        avg.setSelected(true);

        gbc.gridx = 2; gbc.weightx = 0.6;
        panel.add(simple, gbc);

        gbc.gridx = 3;
        panel.add(avg, gbc);

        gbc.gridx = 4;
        panel.add(complex, gbc);

        // Helpful: set action commands so Day 3 wiring is clean
        simple.setActionCommand("SIMPLE");
        avg.setActionCommand("AVERAGE");
        complex.setActionCommand("COMPLEX");
    }

    private JComponent buildBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(12, 12));

        // Results panel
        JPanel results = new JPanel(new GridLayout(3, 2, 10, 8));
        results.setBorder(BorderFactory.createTitledBorder("Results"));

        results.add(new JLabel("Total Count:"));
        results.add(totalCountValue);

        results.add(new JLabel("VAF Sum:"));
        results.add(vafSumValue);

        results.add(new JLabel("Final FP:"));
        finalFpValue.setFont(new Font("Arial", Font.BOLD, 12));
        results.add(finalFpValue);

        results.add(new JLabel("Code Size:"));
        results.add(codeSizeValue);

        bottom.add(results, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.add(vafBtn);     // wiring Day 4
        buttons.add(resetBtn);   // wired today
        buttons.add(computeBtn); // wiring Day 3
        bottom.add(buttons, BorderLayout.SOUTH);
        buttons.add(computeCodeSizeBtn);

        return bottom;
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Arial", Font.BOLD, 12));
        return l;
    }

    // Day 2: only reset works; other actions come later
    private void wireResetOnlyForNow() {
        resetBtn.addActionListener(e -> resetForm());
    }


    private void resetForm() {
        // Reset counts
        for (JTextField tf : countFields.values()) {
            tf.setText("0");
        }
        // Reset complexity to Average
        for (ButtonGroup g : complexityGroups.values()) {
            // Find the AVERAGE button and select it
            // We used action commands above, so we can select by iterating
            for (var btnEnum = g.getElements(); btnEnum.hasMoreElements(); ) {
                AbstractButton b = btnEnum.nextElement();
                if ("AVERAGE".equals(b.getActionCommand())) {
                    b.setSelected(true);
                    break;
                }
            }
        }
        // Reset results
        totalCountValue.setText("—");
        vafSumValue.setText("—");
        finalFpValue.setText("—");

    }

    // Getters for Day 3 wiring
    public Map<String, JTextField> getCountFields() { return countFields; }
    public Map<String, ButtonGroup> getComplexityGroups() { return complexityGroups; }

    public JLabel getTotalCountValue() { return totalCountValue; }
    public JLabel getVafSumValue() { return vafSumValue; }
    public JLabel getFinalFpValue() { return finalFpValue; }

    public JButton getComputeBtn() { return computeBtn; }
    public JButton getVafBtn() { return vafBtn; }
    public JButton getChangeLanguageBtn() { return changeLanguageBtn; }

    public void setCurrentLanguage(String lang) {
        currentLanguageValue.setText(lang);
    }
}
