import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Hint extends JPanel implements ActionListener {
    private JButton hintButton;

    private JLabel hintLabel;

    private static JFrame hintFrame;

    public Hint(String hint) {
        hintButton = new JButton("?");
        hintButton.addActionListener(this);
//        JLabel hintLabel = new JLabel("?");

        hintLabel = new JLabel("<html><body><div width=\"450\">" + hint + "</div></body></html>");
        hintLabel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (hintFrame == null) {
            hintFrame = new JFrame("Help");
        }
        hintFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        hintFrame.setVisible(false);
//        hintLabel.setToolTipText();
//        super.setBorder(BorderFactory.createLineBorder(Color.black));

//        super.add(hintLabel);
        super.add(hintButton);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == hintButton) {
            hintFrame.getContentPane().removeAll();
            hintFrame.getContentPane().add(hintLabel);
            hintFrame.pack();
            hintFrame.setVisible(true);
        }
    }
}