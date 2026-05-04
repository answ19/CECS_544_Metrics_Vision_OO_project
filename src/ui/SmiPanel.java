package ui;

import model.SmiData;
import model.SmiRow;
import service.SmiService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SmiPanel extends JPanel {

    private final SmiData data;
    private final SmiService service;

    private final DefaultTableModel model = new DefaultTableModel(
             new Object[]{"SMI", "Modules Added", "Modules Changed", "Modules Deleted", "Total Modules"},0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 1 || column == 2 || column == 3;
        }

    };
    private final JTable table = new JTable(model);
    private final JButton addRowBtn = new JButton("Add Row");
    private final JButton computeBtn = new JButton("Compute Index");

    public SmiPanel(SmiData data, SmiService service) {
        this.data = data;
        this.service = service;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.add(addRowBtn);
        buttons.add(computeBtn);
        add(buttons, BorderLayout.SOUTH);

        loadFromData();
        wireButtons();
    }

    private void wireButtons() {
        addRowBtn.addActionListener(e -> {
            model.addRow(new Object[]{0, 0, 0, 0, ""});
        });

        computeBtn.addActionListener(e -> {
            try {
                saveToData();
                service.compute(data);
                loadFromData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter valid non-negative numbers.",
                        "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadFromData() {
        model.setRowCount(0);

        for (SmiRow row : data.getRows()) {
            model.addRow(new Object[]{
                    String.format("%.2f", row.getSmi()),
                    row.getAddedModules(),
                    row.getChangedModules(),
                    row.getDeletedModules(),
                    row.getTotalModules()
            });
        }

    }

    private void saveToData() {
        data.getRows().clear();

        for (int i = 0; i < model.getRowCount(); i++) {
            SmiRow row = new SmiRow();

            row.setSmi(0.0);
            row.setAddedModules(readInt(i, 1));
            row.setChangedModules(readInt(i, 2));
            row.setDeletedModules(readInt(i, 3));
            row.setTotalModules(0); // computed later by service

            data.getRows().add(row);
        }
    }

    public SmiData getSmiData() {
        saveToData();
        service.compute(data);
        return data;
    }

    private int readInt(int row, int col) {
        Object value = model.getValueAt(row, col);
        int number = Integer.parseInt(value.toString().trim());

        if (number < 0) throw new NumberFormatException();

        return number;
    }
}