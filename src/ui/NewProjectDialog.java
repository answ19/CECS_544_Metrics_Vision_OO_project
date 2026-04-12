package ui;

import javax.swing.*;
import java.awt.*;

public class NewProjectDialog extends JDialog {

    private final JTextField projectNameField = new JTextField(20);
    private final JTextField creatorNameField = new JTextField(20);
    private final JTextField productNameField = new JTextField(20);
    private final JTextArea commentsArea = new JTextArea(4, 20);
    private boolean saved = false;

    public NewProjectDialog(Window owner) {
        super(owner, "New Project", ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        form.add(new JLabel("Project Name:"));
        form.add(projectNameField);

        form.add(new JLabel("Creator Name:"));
        form.add(creatorNameField);

        JButton okBtn = new JButton("OK");
        JButton cancelBtn = new JButton("Cancel");

        okBtn.addActionListener(e -> {
            saved = true;
            setVisible(false);
        });

        cancelBtn.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancelBtn);
        bottom.add(okBtn);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getProjectName() {
        return projectNameField.getText().trim();
    }

    public String getCreatorName() {
        return creatorNameField.getText().trim();
    }
}


