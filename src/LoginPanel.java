import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JButton loginButton, resetButton;
    private JTextField displayNameField;
    private JPasswordField passwordField;

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public String getDisplayName() {
        return displayNameField.getText().trim();
    }

    public String getPassword() {
        char[] passwordData = passwordField.getPassword();
        return String.valueOf(passwordData);
    }

    public LoginPanel(ActionListener target) {
        super.setLayout(new BorderLayout());
        JTextArea instruction = new JTextArea("Please sign in using your Nature Up North website (natureupnorth.org) credentials.\n\nPlease make sure that you use the display name instead of the username!");
        instruction.setOpaque(false);
        instruction.setEditable(false);
        super.add(instruction, BorderLayout.PAGE_START);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(2, 2));
        centerPanel.add(new JLabel("Display Name", SwingConstants.RIGHT));
        displayNameField = new JTextField();
        centerPanel.add(displayNameField);
        centerPanel.add(new JLabel("Password", SwingConstants.RIGHT));
        passwordField = new JPasswordField();
        centerPanel.add(passwordField);
        centerPanel.setBorder(new EmptyBorder(10, 10, 10, 100));
        super.add(centerPanel);

        JPanel bottomPanel = new JPanel();
        loginButton = new JButton("Log In");
        loginButton.addActionListener(target);
        resetButton = new JButton("Reset Password");
        resetButton.addActionListener(target);
        bottomPanel.add(loginButton);
        bottomPanel.add(resetButton);
        super.add(bottomPanel, BorderLayout.PAGE_END);

        super.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
