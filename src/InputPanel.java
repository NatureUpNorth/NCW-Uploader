import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class InputPanel extends JPanel {
    private ArrayList<Tab> tabs;
    private JSONObject configObj;
    private JTabbedPane tabbedPane;
    private JButton uploadButton;
    private JButton helpButton;

    public InputPanel(ActionListener target) {
        processJSON();
        setupPanel();
        uploadButton.addActionListener(target);
        helpButton.addActionListener(target);
    }

    public JButton getUploadButton() {
        return uploadButton;
    }

    public JButton getHelpButton() {
        return helpButton;
    }

    private void setupPanel() {
        super.setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        for (int index = 0; index < tabs.size(); index++) {
            Tab tab = tabs.get(index);
            tabbedPane.add(tab.getPanel(), tab.getTitle());
        }

        super.add(tabbedPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        uploadButton = new JButton("Upload");
        buttonPanel.add(uploadButton);

        helpButton = new JButton("Help");
        buttonPanel.add(helpButton);

        super.add(buttonPanel,  BorderLayout.PAGE_END);
    }

    private void processJSON() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream configInput = classLoader.getResourceAsStream("config.json");
        Scanner configScanner = new Scanner(configInput).useDelimiter("\\A");
        configObj = new JSONObject(configScanner.hasNext() ? configScanner.next() : "{}");

        JSONArray tabArray = configObj.getJSONArray("tabs");
        tabs = new ArrayList<Tab>();

        for (int index = 0; index < tabArray.length(); index++) {
            JSONObject tabObj = (JSONObject)tabArray.get(index);
            Tab tab = new Tab(tabObj.getString("title"));
            tabs.add(tab);
            JSONArray subpanels = tabObj.getJSONArray("panels");
            for (int idx = 0; idx < subpanels.length(); idx++) {
                JSONObject subpanel = (JSONObject)subpanels.get(idx);
                tab.addSubPanel(subpanel);
            }
        }
    }

    public static void main(String[] args) {
        JFrame myFrame = new JFrame("InputPanel Test");

        myFrame.getContentPane().add(new InputPanel(null));
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.pack();
        myFrame.setVisible(true);
    }
}
