package ui;

import javax.swing.*;
        import java.awt.*;

public class LanguageDialog extends JDialog {

    private final JComboBox<String> languageBox;
    private boolean saved = false;

    public LanguageDialog(Window owner, String currentLanguage) {
        super(owner, "Select Language", ModalityType.APPLICATION_MODAL);

        String[] languages = {
                "Java", "C++", "C#", "Python", "Ruby", "Objective C"
        };

        setLayout(new BorderLayout(10, 10));

        JPanel center = new JPanel(new FlowLayout());
        center.add(new JLabel("Programming Language:"));

        languageBox = new JComboBox<>(languages);
        languageBox.setSelectedItem(currentLanguage);
        center.add(languageBox);

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

        add(center, BorderLayout.CENTER);
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
