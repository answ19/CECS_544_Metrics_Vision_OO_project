package ui;

import javax.swing.*;
import java.awt.*;

public class NewProjectDialog extends JDialog {

    private final JTextField projectNameField = new JTextField(20);
    private final JTextField productNameField = new JTextField(20);
    private final JTextField creatorNameField = new JTextField(20);
    private final JTextArea commentsArea = new JTextArea(5, 20);

    private boolean saved = false;

    public NewProjectDialog(Window owner) {
        super(owner, "New Project", ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("CECS 544 Metrics Suite New Project", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        form.add(new JLabel("Project Name:"), gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        form.add(projectNameField, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 0;
        form.add(new JLabel("Product Name:"), gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        form.add(productNameField, gc);

        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 0;
        form.add(new JLabel("Creator:"), gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        form.add(creatorNameField, gc);

        gc.gridx = 0;
        gc.gridy = 3;
        gc.weightx = 0;
        gc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Comments:"), gc);

        commentsArea.setLineWrap(true);
        commentsArea.setWrapStyleWord(true);
        JScrollPane commentsScrollPane = new JScrollPane(commentsArea);

        gc.gridx = 1;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        form.add(commentsScrollPane, gc);

        mainPanel.add(form, BorderLayout.CENTER);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            StringBuilder missing = new StringBuilder();

            if (getProjectName().isEmpty()) {
                missing.append("- Project Name is required.\n");
            }
            if (getProductName().isEmpty()) {
                missing.append("- Product Name is required.\n");
            }
            if (getCreatorName().isEmpty()) {
                missing.append("- Creator is required.\n");
            }

            if (!missing.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter the missing required input(s):\n\n" + missing,
                        "Missing Required Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            saved = true;
            setVisible(false);
            dispose();
        });

        cancelBtn.addActionListener(e -> {
            saved = false;
            setVisible(false);
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(okBtn);
        bottom.add(cancelBtn);

        add(mainPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setSize(430, 320);
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getProjectName() {
        return projectNameField.getText().trim();
    }

    public String getProductName() {
        return productNameField.getText().trim();
    }

    public String getCreatorName() {
        return creatorNameField.getText().trim();
    }

    public String getComments() {
        return commentsArea.getText().trim();
    }
}