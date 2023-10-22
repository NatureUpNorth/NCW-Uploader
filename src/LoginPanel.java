import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel {
    private JButton loginButton, resetButton, signUpButton;
    private JTextField displayNameField;
    private JPasswordField passwordField;

    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    // add sign up option
    public JButton getSignUpButton() {
        return signUpButton;
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
        resetButton.addActionListener(target)

        // add sign up button
        signUpButton = new JButton("Sign Up")
        signUpButton.addActionListener(new ActionListener() {

            try {
                String url = "https://www.natureupnorth.org/user/register"

                if(Desktop.isDesktopSupported()){
                    Desktop go = Desktop.getDesktop();

                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(url);
                    }
                }
            } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                
        });

        bottomPanel.add(loginButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(signUpButton);

        super.add(bottomPanel, BorderLayout.PAGE_END);

        super.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    // make enter work
    private class EnterListener implements KeyListener{
        public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                loginButton.doClick();
            }
        }
    }
}
