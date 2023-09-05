import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FileSelectPanel extends GenericPanel implements ActionListener {
    private JButton browseButton;
    private JTextField filepathField;
    private JLabel filesFound;
    private FileProcessor fileProcessor;
    private JLabel progress;
    private JLabel[] previewImages;
    private final int PREVIEW_IMAGES = 5;

    public FileSelectPanel(JSONObject panelInfo) {
        super(panelInfo);

        fileProcessor = new FileProcessor(this);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1));

        filepathField = new JTextField();
        filepathField.setEditable(false);
        centerPanel.add(filepathField);

        JPanel files = new JPanel();
        files.add(new JLabel("Files found:"));
        filesFound = new JLabel("--");
        files.add(filesFound);

        progress = new JLabel();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ImageIcon spinnerIcon = new ImageIcon(classLoader.getResource("spinner.gif"));
        progress.setIcon(spinnerIcon);
        setProgresVisible(false);
        files.add(progress);
        centerPanel.add(files);

        JPanel previewPanel = new JPanel();
        previewPanel.setBorder(BorderFactory.createTitledBorder("Preview of First " + PREVIEW_IMAGES + " Images"));
        previewPanel.setLayout(new GridLayout(1, PREVIEW_IMAGES));
        previewImages = new JLabel[PREVIEW_IMAGES];
        for (int index = 0; index < previewImages.length; index++) {
            previewImages[index] = new JLabel();
            previewPanel.add(previewImages[index]);
        }
        centerPanel.add(previewPanel);

        super.add(centerPanel, BorderLayout.CENTER);


        browseButton = new JButton("Browse");
        browseButton.addActionListener(this);
        super.add(browseButton, BorderLayout.PAGE_END);
    }

    public void setProgresVisible(boolean visible) {
        progress.setVisible(visible);
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fileChooser.showOpenDialog(this);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                filepathField.setText(file.getAbsolutePath());
                fileProcessor.setFilepath(file.getAbsolutePath());
                setProgresVisible(true);
                setBrowseButtonEnable(false);
                fileProcessor.start();
            } else{
//                filepathField.setText("");
//                resetFileCount();
            }
        }
    }

    public void resetFileCount() {
        filesFound.setText("--");
    }

    public void setFileCount(int count) {
        filesFound.setText("" + count);
    }

    public void setBrowseButtonEnable(boolean enabled) {
        browseButton.setEnabled(enabled);
    }
    public String getFilepath() {
        return filepathField.getText();
    }

    public void updatePreviewImages() {
        Set<String> files = fileProcessor.getFiles();

        int count = 0;
        for (String file : files) {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(file));
                Image resizedImage = image.getScaledInstance(previewImages[count].getWidth(), previewImages[count].getHeight(),
                        Image.SCALE_SMOOTH);
                previewImages[count].setIcon(new ImageIcon(resizedImage));
            } catch (IOException e) {
            }
            count++;
            if (count >= PREVIEW_IMAGES) {
                break;
            }
        }
        while (count < PREVIEW_IMAGES) {
            previewImages[count].setIcon(null);
            count++;
        }
    }
}
