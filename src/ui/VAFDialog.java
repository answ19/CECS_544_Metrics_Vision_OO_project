package ui;

import javax.swing.*;
        import java.awt.*;
        import java.util.Arrays;

public class VAFDialog extends JDialog {

    private final JComboBox<Integer>[] combos = new JComboBox[14];
    private final int[] vafValues; // reference to shared array in ProjectData

    @SuppressWarnings("unchecked")
    public VAFDialog(Window owner, int[] vafValues) {
        super(owner, "Value Adjustment Factors (VAF)", ModalityType.APPLICATION_MODAL);
        this.vafValues = vafValues;

        setLayout(new BorderLayout(10, 10));
        JPanel grid = new JPanel(new GridLayout(14, 2, 8, 6));

        Integer[] options = {0,1,2,3,4,5};
        for (int i = 0; i < 14; i++) {
            grid.add(new JLabel("Factor " + (i + 1) + ":"));
            combos[i] = new JComboBox<>(options);
            combos[i].setSelectedItem(vafValues[i]); // restore previous values
            grid.add(combos[i]);
        }

        JButton ok = new JButton("OK");
        ok.addActionListener(e -> {
            for (int i = 0; i < 14; i++) {
                vafValues[i] = (Integer) combos[i].getSelectedItem();
            }
            setVisible(false);
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancel);
        bottom.add(ok);

        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public static int sum(int[] vaf) {
        return Arrays.stream(vaf).sum();
    }
}