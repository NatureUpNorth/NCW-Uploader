import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ThankYouPanel extends JPanel {
    /*
					"Congratulations! You have successfully uploaded your images to Nature Up North's Dropbox!\nYou may either continue"
                            + " on the app and submit more photos, or close out of the app if you are done.\n\n"
                            + "Thank you for your contributions to science!");
*/
    private JButton quitButton;
    private JButton continueButton;
    public ThankYouPanel(ActionListener target) {
        super.setLayout(new BorderLayout());
        super.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel thankYouMessage = new JLabel(
                "<htm" +
                        "l><p>Congratulations! You have successfully uploaded your images to Nature Up North's Dropbox!</p><p>You may either continue"
                        + " on the app and submit more photos, or close out of the app if you are done.</p><p>"
                        + "Thank you for your contributions to science!</p></html>"
        );

        super.add(thankYouMessage, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        continueButton = new JButton("Upload More");
        continueButton.addActionListener(target);
        buttonPanel.add(continueButton);
        quitButton = new JButton("Done");
        quitButton.addActionListener(target);
        buttonPanel.add(quitButton);
        super.add(buttonPanel, BorderLayout.PAGE_END);
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public JButton getContinueButton() {
        return continueButton;
    }

    public static void main(String[] args) {
        JFrame myFrame = new JFrame("Thank You Test");

        myFrame.getContentPane().add(new ThankYouPanel(null));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setVisible(true);
    }
}
