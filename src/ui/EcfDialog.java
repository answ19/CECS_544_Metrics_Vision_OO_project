package ui;

import javax.swing.*;
import java.awt.*;

public class EcfDialog extends JDialog {

    private final JComboBox<Integer>[] combos = new JComboBox[8];
    private final int[] values;

    private static final String[] QUESTIONS = {
            "Familiar with the development process",
            "Application experience",
            "Object-oriented experience",
            "Lead analyst capability",
            "Motivation",
            "Stable requirements",
            "Part-time staff",
            "Difficult programming language"
    };

    @SuppressWarnings("unchecked")
    public EcfDialog(Window owner, int[] values) {
        super(owner, "Environmental Complexity Factor", ModalityType.APPLICATION_MODAL);
        this.values = values;

        setLayout(new BorderLayout(10, 10));

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        Integer[] options = {0,1,2,3,4,5};

        for (int i = 0; i < 8; i++) {
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
            for (int i = 0; i < 8; i++) {
                values[i] = (Integer) combos[i].getSelectedItem();
            }
            setVisible(false);
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancelBtn);
        bottom.add(doneBtn);

        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }
}