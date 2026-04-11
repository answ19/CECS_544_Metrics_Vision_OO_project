package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class VAFDialog extends JDialog {

    private final JComboBox<Integer>[] combos = new JComboBox[14];
    private final int[] vafValues; // shared array from ProjectData

    private static final String[] QUESTIONS = {
            "Does the system require reliable backup and recovery processes?",
            "Are specialized data communications required to transfer information to or from the application?",
            "Are there distributed processing functions?",
            "Is performance critical?",
            "Will the system run in an existing, heavily utilized operational environment?",
            "Does the system require online data entry?",
            "Does the online data entry require the input transaction to be built over multiple screens or operations?",
            "Are the internal logical files updated online?",
            "Are the input, output, files or inquiries complex?",
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

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel("Assign a value from 0 to 5 for each of the following Value Adjustment Factors:");
        heading.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 4, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Integer[] options = {0, 1, 2, 3, 4, 5};

        for (int i = 0; i < 14; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 1.0;
            formPanel.add(new JLabel(QUESTIONS[i]), gbc);

            combos[i] = new JComboBox<>(options);
            combos[i].setSelectedItem(vafValues[i]); // restore previous saved values

            gbc.gridx = 1;
            gbc.weightx = 0.0;
            formPanel.add(combos[i], gbc);
        }

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(1000, 550));

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton doneBtn = new JButton("Done");
        doneBtn.addActionListener(e -> {
            for (int i = 0; i < 14; i++) {
                vafValues[i] = (Integer) combos[i].getSelectedItem();
            }
            setVisible(false);
            dispose();
        });

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            setVisible(false);
            dispose();
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        bottomPanel.add(doneBtn);
        bottomPanel.add(cancelBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(owner);
        setResizable(true);
    }

    public static int sum(int[] vaf) {
        return Arrays.stream(vaf).sum();
    }
}