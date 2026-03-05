package ui;

import javax.swing.*;
import java.awt.*;

public class FunctionPointPanel extends JPanel {

    public FunctionPointPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Function Point Data Entry", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        add(title, BorderLayout.NORTH);

        JPanel placeholder = new JPanel();
        placeholder.add(new JLabel("FP Input Fields will go here"));

        add(placeholder, BorderLayout.CENTER);
    }
}
