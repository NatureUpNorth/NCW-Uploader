import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * The Uploader App main class
 *
 * @author Choong-Soo Lee
 * @version 1.0
 */
public class Uploader extends JFrame  implements ActionListener {
    private TitlePanel titlePanel;
    private LoginPanel loginPanel;
    private InputPanel inputPanel;
    private UploadPanel uploadPanel;
    private UserAuthentication userAuthenticator;
    private ThankYouPanel thankYouPanel;
    private Responses responses;
    private static Uploader theUploader;

    private JFrame helpFrame;

    public Uploader() {
        super("North Country Wild Uploader");

        if (theUploader != null) {
            throw new RuntimeException("The uploader is already running!");
        }
        theUploader = this;
        responses = new Responses();

        titlePanel = new TitlePanel(this);
        userAuthenticator = DrupalJSONAuth.getInstance();

        super.getContentPane().add(titlePanel);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.pack();
        super.setVisible(true);
    }

    public static Uploader getInstance() {
        if (theUploader == null) {
            theUploader = new Uploader();
        }
        return theUploader;
    }

    public Responses getResponses() {
        return responses;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        boolean refresh = false;
        if (event.getSource() == titlePanel.getStartButton()) {
            loginPanel = new LoginPanel(this);
            super.getContentPane().remove(titlePanel);
            super.getContentPane().add(loginPanel);
            getResponses().addKey("displayName");
            refresh = true;
        }

        if (loginPanel != null && event.getSource() == loginPanel.getLoginButton()) {
            String displayName = loginPanel.getDisplayName();
            String password = loginPanel.getPassword();
            if (displayName.length() > 0 && password.length() > 0 && userAuthenticator.authenticate(displayName, password)) {
                inputPanel = new InputPanel(this);

                super.getContentPane().remove(loginPanel);
                super.getContentPane().add(inputPanel);

                getResponses().replaceKeyValue("displayName", displayName);
                refresh = true;
            } else {
                JOptionPane.showMessageDialog(this,
                        "Unrecognized display name or password.\n\nPlease make sure you are using the display name instead of the username or email address.",
                        "Authentication failed!",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (loginPanel != null && event.getSource() == loginPanel.getResetButton()) {
            try {
                Desktop.getDesktop().browse(new URI("https://www.natureupnorth.org/user/password"));
            } catch (URISyntaxException | IOException e) {
                System.err.println("URI Error");
            }
        }

        if (inputPanel != null && event.getSource() == inputPanel.getUploadButton()) {
            Map<String, String> response = getResponses().validate();
            if (response.get("valid").equals("true")) {
                // start the upload
                try {
                    uploadPanel = new UploadPanel(this);
                    super.getContentPane().remove(inputPanel);
                    super.getContentPane().add(uploadPanel);
                    refresh = true;
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Please contact Nature Up North for support. Sorry for your inconvenience!",
                            "Error Uploading",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        response.get("message"),
                        response.get("title"),
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        if (inputPanel != null && event.getSource() == inputPanel.getHelpButton()) {
            if (helpFrame == null) {
                helpFrame = new JFrame();
                helpFrame.getContentPane().add(new TitlePanel(null));
                helpFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                helpFrame.pack();
            }
            helpFrame.setVisible(true);
        }

        if (thankYouPanel != null && event.getSource() == thankYouPanel.getQuitButton()) {
            System.exit(0);
        }

        if (thankYouPanel != null && event.getSource() == thankYouPanel.getContinueButton()) {
            inputPanel = new InputPanel(this);

            super.getContentPane().remove(thankYouPanel);
            super.getContentPane().add(inputPanel);

            refresh = true;
        }

        if (refresh) {
            super.pack();
            super.invalidate();
            super.validate();
            super.repaint();
        }
    }

    public void thankyou() {
        thankYouPanel = new ThankYouPanel(this);
        super.getContentPane().remove(uploadPanel);
        super.getContentPane().add(thankYouPanel);
        super.pack();
        super.invalidate();
        super.validate();
        super.repaint();
    }

    public static void main(String[] args) {
        Uploader myUploader = new Uploader();
    }
}
