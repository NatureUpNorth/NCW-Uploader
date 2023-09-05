import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UploadPanel extends JPanel {
    private DataProcessor dataProcessor;

    private JProgressBar progressBar;
    private JLabel descLabel;
    private JLabel imageLabel;

    private DropboxUploader dropboxUploader;

    private int textThreshold;

    public UploadPanel(ActionListener target) {
        textThreshold = 40;

        super.setPreferredSize(new Dimension(600, 600));
        super.setBorder(new EmptyBorder(10, 10, 10, 10));
        super.setLayout(new BorderLayout());

        descLabel = new JLabel();
        descLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        super.add(descLabel, BorderLayout.PAGE_START);

        imageLabel = new JLabel();
        imageLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        super.add(imageLabel, BorderLayout.CENTER);

        progressBar = new JProgressBar();
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBorder(new EmptyBorder(10, 10, 10, 10));

        super.add(progressBar, BorderLayout.PAGE_END);

        dataProcessor = new DataProcessor();

        dropboxUploader = new DropboxUploader(this, dataProcessor);
        dropboxUploader.execute();
    }

    public void setImage(String imageFile) {
        if (imageFile == null) {
            descLabel.setText("Uploading the meta data");
            imageLabel.setIcon(null);
            return;
        }
        String displayText = imageFile.substring(imageFile.length() - textThreshold < 0 ? 0 : imageFile.length() - textThreshold);
        displayText = (imageFile.length() > textThreshold ? "..." : "") + displayText;
        descLabel.setText("Uploading " + displayText);

        BufferedImage image = null;
        try {
            File file = new File(imageFile);
            image = ImageIO.read(file);
            dataProcessor.addFile(imageFile, file);
            Image resizedImage = image.getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(),
                    Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(resizedImage));
        } catch (IOException e) {
        }

    }
    public JProgressBar getProgressBar() {
        return progressBar;
    }

}
