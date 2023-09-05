import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LatLongPanel extends GenericPanel implements ActionListener, DocumentListener {
    private JTextField urlCopyField;

    private JLabel latitudeLabel, longitudeLabel;

    private JLabel statusLabel;

    private Timer timer;

    private double latitude, longitude;

    private final int timeout = 3000;

    public LatLongPanel(JSONObject panelInfo) {
        super(panelInfo);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        urlCopyField = new JTextField();
        urlCopyField.getDocument().addDocumentListener(this);
        centerPanel.add(urlCopyField, BorderLayout.PAGE_START);

        latitudeLabel = new JLabel();
        longitudeLabel = new JLabel();

        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 2));
        gridPanel.add(new JLabel("Latitude"));
        gridPanel.add(latitudeLabel);
        gridPanel.add(new JLabel("Longitude"));
        gridPanel.add(longitudeLabel);

        centerPanel.add(gridPanel, BorderLayout.CENTER);

        super.add(centerPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("");

        super.add(statusLabel, BorderLayout.PAGE_END);

        timer = new Timer(timeout,this);
    }

    private boolean getParams(String url) {
        try {
            int count = 0;
            URL urlObj = new URL(url);
            String query = urlObj.getQuery();
            if (query == null) {
                return false;
            }
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                int idx = pair.indexOf("=");
                String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
                String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
                if (key.equals("lat")) {
                    latitude = Double.parseDouble(value);
                    count+=2;
                }
                if (key.equals("long")) {
                    longitude = Double.parseDouble(value);
                    count+=3;
                }
            }
            if (count == 5) {
                Uploader.getInstance().getResponses().replaceKeyValue(getKey(), latitude + "," + longitude);
                longitudeLabel.setText("<html>" + longitude + "&deg;</html>");
                latitudeLabel.setText("<html>" + latitude + "&deg;</html>");
                return true;
            } else {
                Uploader.getInstance().getResponses().replaceKeyValue(getKey(), null);
                longitudeLabel.setText("");
                latitudeLabel.setText("");
                return false;
            }
        } catch (UnsupportedEncodingException|MalformedURLException e) {
            return false;
        }
    }

    private void displayStatusMessage(String message) {
        statusLabel.setText(message);
        if (timer.isRunning()) {
            timer.stop();
            timer.restart();
        } else {
            timer.start();
        }
    }
    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == timer) {
            timer.stop();
            statusLabel.setText("");
        }
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        if (getParams(urlCopyField.getText())) {
            displayStatusMessage("Latitude/longitude extracted successfully!");
        } else {
            displayStatusMessage("Invalid URL. Please try again.");
        }
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        if (getParams(urlCopyField.getText())) {
            displayStatusMessage("Latitude/longitude extracted successfully!");
        } else {
            displayStatusMessage("Invalid URL. Please try again.");
        }
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        if (getParams(urlCopyField.getText())) {
            displayStatusMessage("Latitude/longitude extracted successfully!");
        } else {
            displayStatusMessage("Invalid URL. Please try again.");
        }
    }
}
