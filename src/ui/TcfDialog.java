package ui;

import javax.swing.*;
import java.awt.*;

public class TcfDialog extends JDialog {

    private final JComboBox<Integer>[] combos = new JComboBox[13];
    private final int[] values;

    private static final String[] QUESTIONS = {
            "Distributed system",
            "Response or throughput performance objectives",
            "End-user efficiency",
            "Complex internal processing",
            "Code must be reusable",
            "Easy to install",
            "Easy to use",
            "Portable",
            "Easy to change",
            "Concurrent",
            "Includes special security features",
            "Provides direct access for third parties",
            "Special user training required"
    };

    @SuppressWarnings("unchecked")
    public TcfDialog(Window owner, int[] values) {
        super(owner, "Technical Complexity Factor", ModalityType.APPLICATION_MODAL);
        this.values = values;

        setLayout(new BorderLayout(10, 10));

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        Integer[] options = {0,1,2,3,4,5};

        for (int i = 0; i < 13; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            grid.add(new JLabel(QUESTIONS[i]), gc);

            gc.gridx = 1;
            combos[i] = new JComboBox<>(options);
            combos[i].setSelectedItem(values[i]);
            grid.add(combos[i], gc);
        }

        JButton doneBtn = new JButton("Done");
        doneBtn.addActionListener(e -> {
            for (int i = 0; i < 13; i++) {
                values[i] = (Integer) combos[i].getSelectedItem();
            }
            setVisible(false);
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancelBtn);
        bottom.add(doneBtn);

        add(new JScrollPane(grid), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(700, 450);
        setLocationRelativeTo(owner);
    }
}