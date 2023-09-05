/**
 * The splash title screen
 *
 * @author Choong-Soo Lee
 * @version 1.0
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class TitlePanel extends JPanel {

    private JButton startButton;
    private JEditorPane introduction;
    private Image logoImage, titleImage, gamecamImage;
    private Font font;

    public JButton getStartButton() {
        return startButton;
    }

    public TitlePanel(ActionListener target) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream logoInput = classLoader.getResourceAsStream("nun_SLU.jpg");
        InputStream titleInput = classLoader.getResourceAsStream("title.jpg");
        InputStream gameInput = classLoader.getResourceAsStream("game_camera_pic.jpg");
        InputStream fontInput = classLoader.getResourceAsStream("Roadgeek 2005 New Parks.otf");
        URL introductionURL = Uploader.class.getResource("introduction.html");

        try {
            logoImage = ImageIO.read(logoInput);
            titleImage = ImageIO.read(titleInput);
            gamecamImage = ImageIO.read(gameInput);
        } catch (IOException e) {
            System.err.println("Error reading the image resources");
            System.exit(0);
        }

        try {
            font = Font.createFont(Font.TRUETYPE_FONT, fontInput);
        } catch (IOException | FontFormatException e) {
            System.err.println("Error loading the font");
            System.exit(0);
        }

        super.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel(new ImageIcon(logoImage)));
        topPanel.add(new JLabel(new ImageIcon(titleImage)));
        topPanel.add(new JLabel(new ImageIcon(gamecamImage)));
        super.add(topPanel, BorderLayout.PAGE_START);

        introduction = new JEditorPane();
        introduction.setFont(font);
        introduction.setFont(introduction.getFont().deriveFont(16f));
        introduction.setOpaque(false);
        introduction.setEditable(false);
        introduction.setPreferredSize(new Dimension(900, 500));

        try {
            introduction.setPage(introductionURL);
        } catch (IOException e) {
            introduction.setContentType("text/html");
            introduction.setText("<html>Error loading the introduction text.</html>");
        }

        introduction.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if(event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    if(Desktop.isDesktopSupported()) {
                        try {
                            Desktop.getDesktop().browse(event.getURL().toURI());
                        } catch (URISyntaxException | IOException e) {
                            System.err.println("URI Error");
                        }
                    }
                }
            }
        });

        super.add(introduction, BorderLayout.CENTER);

        if (target != null) {
            startButton = new JButton("Start");
            startButton.addActionListener(target);
            super.add(startButton, BorderLayout.PAGE_END);
        }

        super.setBorder(new EmptyBorder(10, 10, 10, 10));
    }
}
