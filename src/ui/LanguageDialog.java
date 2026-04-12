package ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class LanguageDialog extends JDialog {

    private boolean saved = false;
    private String selectedLanguage;

    private final Map<String, JRadioButton> languageButtons = new LinkedHashMap<>();

    private static final String[] LANGUAGES = {
            "Assembler",
            "Ada 95",
            "C",
            "C++",
            "C#",
            "COBOL",
            "FORTRAN",
            "HTML",
            "Java",
            "JavaScript",
            "VBScript",
            "Visual Basic"
    };

    public LanguageDialog(Window owner, String currentLanguage) {
        super(owner, "Select Language", ModalityType.APPLICATION_MODAL);

        this.selectedLanguage = currentLanguage;

        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel heading = new JLabel("Select one language");
        heading.setFont(new Font("Arial", Font.BOLD, 12));
        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        ButtonGroup group = new ButtonGroup();

        for (String language : LANGUAGES) {
            JRadioButton radio = new JRadioButton(language);
            radio.setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(radio);
            listPanel.add(radio);
            languageButtons.put(language, radio);

            if (language.equals(currentLanguage)) {
                radio.setSelected(true);
                selectedLanguage = language;
            }

            radio.addActionListener(e -> selectedLanguage = language);
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setPreferredSize(new Dimension(180, 260));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton doneButton = new JButton("Done");
        doneButton.addActionListener(e -> {
            if (selectedLanguage == null || selectedLanguage.isBlank()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a language.",
                        "Selection Required",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            saved = true;
            setVisible(false);
            dispose();
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.add(doneButton);

        add(mainPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(owner);
        setResizable(false);
    }

    public boolean isSaved() {
        return saved;
    }

    public String getSelectedLanguage() {
        return selectedLanguage;
    }
}