package ui;

import model.UcpData;
import model.UcpResult;
import service.UcpService;

import javax.swing.*;
import java.awt.*;

public class UcpPanel extends JPanel {

    private final UcpData data;
    private final UcpService service;

    private final JTextField simpleActorsField = new JTextField("0", 6);
    private final JTextField averageActorsField = new JTextField("0", 6);
    private final JTextField complexActorsField = new JTextField("0", 6);

    private final JTextField simpleUseCasesField = new JTextField("0", 6);
    private final JTextField averageUseCasesField = new JTextField("0", 6);
    private final JTextField complexUseCasesField = new JTextField("0", 6);

    private final JTextField productivityField = new JTextField("20", 6);
    private final JTextField locPmField = new JTextField("700", 6);
    private final JTextField locPerUcpField = new JTextField("120", 6);

    private final JTextField uawField = new JTextField(8);
    private final JTextField uucwField = new JTextField(8);
    private final JTextField uucpField = new JTextField(8);
    private final JTextField totalCountField = new JTextField(8);
    private final JTextField tcfField = new JTextField(8);
    private final JTextField ecfField = new JTextField(8);
    private final JTextField totalUcpField = new JTextField(8);
    private final JTextField hoursField = new JTextField(10);
    private final JTextField locField = new JTextField(10);
    private final JTextField pmField = new JTextField(10);

    private final JButton tcfBtn = new JButton("Technical Complexity...");
    private final JButton ecfBtn = new JButton("Environmental Complexity...");
    private final JButton computeBtn = new JButton("Compute UCP");

    public UcpPanel(UcpData data, UcpService service) {
        this.data = data;
        this.service = service;

        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        for (JTextField f : new JTextField[]{uawField, uucwField, uucpField, totalCountField,
                tcfField, ecfField, totalUcpField, hoursField, locField, pmField}) {
            f.setEditable(false);
        }

        add(buildForm(), BorderLayout.CENTER);
        wireButtons();
        loadDefaults();
    }

    private JComponent buildForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // actors
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Simple Actors"), gc);
        gc.gridx = 1; panel.add(simpleActorsField, gc);
        gc.gridx = 2; panel.add(new JLabel("Average Actors"), gc);
        gc.gridx = 3; panel.add(averageActorsField, gc);
        gc.gridx = 4; panel.add(new JLabel("Complex Actors"), gc);
        gc.gridx = 5; panel.add(complexActorsField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Simple Use Cases"), gc);
        gc.gridx = 1; panel.add(simpleUseCasesField, gc);
        gc.gridx = 2; panel.add(new JLabel("Average Use Cases"), gc);
        gc.gridx = 3; panel.add(averageUseCasesField, gc);
        gc.gridx = 4; panel.add(new JLabel("Complex Use Cases"), gc);
        gc.gridx = 5; panel.add(complexUseCasesField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(tcfBtn, gc);
        gc.gridx = 1; panel.add(tcfField, gc);
        gc.gridx = 2; panel.add(ecfBtn, gc);
        gc.gridx = 3; panel.add(ecfField, gc);
        gc.gridx = 4; panel.add(computeBtn, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Productivity Factor"), gc);
        gc.gridx = 1; panel.add(productivityField, gc);
        gc.gridx = 2; panel.add(new JLabel("LOC / pm"), gc);
        gc.gridx = 3; panel.add(locPmField, gc);
        gc.gridx = 4; panel.add(new JLabel("LOC / UCP"), gc);
        gc.gridx = 5; panel.add(locPerUcpField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("UAW"), gc);
        gc.gridx = 1; panel.add(uawField, gc);
        gc.gridx = 2; panel.add(new JLabel("UUCW"), gc);
        gc.gridx = 3; panel.add(uucwField, gc);
        gc.gridx = 4; panel.add(new JLabel("UUCP"), gc);
        gc.gridx = 5; panel.add(uucpField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Total Count"), gc);
        gc.gridx = 1; panel.add(totalCountField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Total UCP"), gc);
        gc.gridx = 1; panel.add(totalUcpField, gc);
        gc.gridx = 2; panel.add(new JLabel("Estimated Hours"), gc);
        gc.gridx = 3; panel.add(hoursField, gc);
        gc.gridx = 4; panel.add(new JLabel("Estimated LOC"), gc);
        gc.gridx = 5; panel.add(locField, gc);

        y++;
        gc.gridx = 0; gc.gridy = y; panel.add(new JLabel("Estimated PM"), gc);
        gc.gridx = 1; panel.add(pmField, gc);

        return panel;
    }

    private void loadDefaults() {
        simpleActorsField.setText(String.valueOf(data.getSimpleActors()));
        averageActorsField.setText(String.valueOf(data.getAverageActors()));
        complexActorsField.setText(String.valueOf(data.getComplexActors()));

        simpleUseCasesField.setText(String.valueOf(data.getSimpleUseCases()));
        averageUseCasesField.setText(String.valueOf(data.getAverageUseCases()));
        complexUseCasesField.setText(String.valueOf(data.getComplexUseCases()));

        productivityField.setText(String.valueOf(data.getProductivityFactor()));
        locPmField.setText(String.valueOf(data.getLocPerPm()));
        locPerUcpField.setText(String.valueOf(data.getLocPerUcp()));
    }
    private void wireButtons() {
        tcfBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            new TcfDialog(w, data.getTechnicalFactors()).setVisible(true);
            tcfField.setText(String.format("%.2f", service.computeTcf(data)));
        });

        ecfBtn.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            new EcfDialog(w, data.getEnvironmentalFactors()).setVisible(true);
            ecfField.setText(String.format("%.2f", service.computeEcf(data)));
        });

        computeBtn.addActionListener(e -> {
            try {
                data.setSimpleActors(readInt(simpleActorsField));
                data.setAverageActors(readInt(averageActorsField));
                data.setComplexActors(readInt(complexActorsField));

                data.setSimpleUseCases(readInt(simpleUseCasesField));
                data.setAverageUseCases(readInt(averageUseCasesField));
                data.setComplexUseCases(readInt(complexUseCasesField));

                data.setProductivityFactor(readDouble(productivityField));
                data.setLocPerPm(readDouble(locPmField));
                data.setLocPerUcp(readDouble(locPerUcpField));

                UcpResult r = service.compute(data);

                uawField.setText(String.valueOf(r.getUaw()));
                uucwField.setText(String.valueOf(r.getUucw()));
                uucpField.setText(String.valueOf(r.getUucp()));
                totalCountField.setText(String.valueOf(r.getUaw() + r.getUucw()));
                tcfField.setText(String.format("%.2f", r.getTcf()));
                ecfField.setText(String.format("%.2f", r.getEcf()));
                totalUcpField.setText(String.format("%.2f", r.getTotalUcp()));
                hoursField.setText(String.format("%.2f", r.getEstimatedHours()));
                locField.setText(String.format("%.2f", r.getEstimatedLoc()));
                pmField.setText(String.format("%.2f", r.getEstimatedPm()));

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid numeric values.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private int readInt(JTextField f) {
        int v = Integer.parseInt(f.getText().trim());
        if (v < 0) throw new NumberFormatException("negative");
        return v;
    }

    private double readDouble(JTextField f) {
        double v = Double.parseDouble(f.getText().trim());
        if (v < 0) throw new NumberFormatException("negative");
        return v;
    }
    public UcpData getUcpData() {
        data.setSimpleActors(readInt(simpleActorsField));
        data.setAverageActors(readInt(averageActorsField));
        data.setComplexActors(readInt(complexActorsField));

        data.setSimpleUseCases(readInt(simpleUseCasesField));
        data.setAverageUseCases(readInt(averageUseCasesField));
        data.setComplexUseCases(readInt(complexUseCasesField));

        data.setProductivityFactor(readDouble(productivityField));
        data.setLocPerPm(readDouble(locPmField));
        data.setLocPerUcp(readDouble(locPerUcpField));

        return data;
    }
}