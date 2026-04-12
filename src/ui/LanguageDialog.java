package ui;

import javax.swing.*;
import java.awt.*;

public class LanguageDialog extends JDialog {

    private JComboBox<String> languageBox;
    private boolean saved = false;

    public LanguageDialog(Window owner, String currentLanguage) {
        super(owner, "Select Language", ModalityType.APPLICATION_MODAL);

        setLayout(new BorderLayout(10,10));

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(8,8,8,8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx = 0;
        gc.gridy = 0;
        center.add(new JLabel("Programming Language:"), gc);

        gc.gridx = 1;

        String[] languages = {
                "Assembler", "Ada 95", "C", "C++", "C#", "COBOL",
                "FORTRAN", "HTML", "Java", "JavaScript", "VBScript", "Visual Basic"
        };

        JList<String> languageList = new JList<>(languages);
        languageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        languageList.setSelectedValue(currentLanguage, true);

        JScrollPane scrollPane = new JScrollPane(languageList);
        add(scrollPane, BorderLayout.CENTER);
        languageBox = new JComboBox<>(languages);
        languageBox.setSelectedItem(currentLanguage);

        center.add(languageBox, gc);

        add(center, BorderLayout.CENTER);

        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");

        ok.addActionListener(e -> {
            saved = true;
            setVisible(false);
        });

        cancel.addActionListener(e -> setVisible(false));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(cancel);
        bottom.add(ok);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSelectedLanguage() {
        return (String) languageBox.getSelectedItem();
    }
}