package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class VAFDialog extends JDialog {

    private final JComboBox<Integer>[] combos = new JComboBox[14];
    private final int[] vafValues;

    private static final String[] QUESTIONS = {
            "Does the system require reliable backup and recovery processes?",
            "Are specialized data communications required to transfer information to or from the application?",
            "Are there distributed processing functions?",
            "Is performance critical?",
            "Will the system run in an existing, heavily utilized operational environment?",
            "Does the system require online data entry?",
            "Does the online data entry require the input transaction to be built over multiple screens or operations?",
            "Are the internal logical files updated online?",
            "Are the input, output, files, or inquiries complex?",
            "Is the internal processing complex?",
            "Is the code designed to be reusable?",
            "Are conversion and installation included in the design?",
            "Is the system designed for multiple installations in different organizations?",
            "Is the application designed to facilitate change and for ease of use by the user?"
    };

    @SuppressWarnings("unchecked")
    public VAFDialog(Window owner, int[] vafValues) {
        super(owner, "Value Adjustment Factors", ModalityType.APPLICATION_MODAL);
        this.vafValues = vafValues;

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "Assign a value from 0 to 5 for each of the following Value Adjustment Factors:"
        );
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.NORTHWEST;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;

        Integer[] options = {0, 1, 2, 3, 4, 5};

        for (int i = 0; i < 14; i++) {
            gc.gridx = 0;
            gc.gridy = i;
            gc.weightx = 1.0;

            JLabel questionLabel = new JLabel(
                    "<html><body style='width: 520px'>" + QUESTIONS[i] + "</body></html>"
            );
            grid.add(questionLabel, gc);

            gc.gridx = 1;
            gc.weightx = 0;

            combos[i] = new JComboBox<>(options);
            combos[i].setSelectedItem(vafValues[i]); // restore previous values
            grid.add(combos[i], gc);
        }

        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setPreferredSize(new Dimension(760, 420));
        add(scrollPane, BorderLayout.CENTER);

        JButton doneBtn = new JButton("Done");
        doneBtn.addActionListener(e -> {
            for (int i = 0; i < 14; i++) {
                vafValues[i] = (Integer) combos[i].getSelectedItem();
            }
            setVisible(false);
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(doneBtn);
        bottom.add(cancelBtn);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public static int sum(int[] vaf) {
        return Arrays.stream(vaf).sum();
    }
}