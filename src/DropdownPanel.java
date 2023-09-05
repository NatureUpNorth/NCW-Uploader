import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DropdownPanel extends GenericPanel implements ActionListener, DocumentListener {
    private JComboBox<String> topLevelComboBox, lowLevelComboBox;

    private Map<String, ArrayList<String>> choices;

    private JTextField otherField;

    private boolean multiLevel;
    public DropdownPanel(JSONObject panelInfo) {
        super(panelInfo);
        multiLevel = true;
        choices = new HashMap<String, ArrayList<String>>();

        // check if it's a single level dropdown list
        try {
            JSONArray firstLevel = panelInfo.getJSONArray("values");
            for (int index = 0; index < firstLevel.length(); index++) {
                String choice = firstLevel.getString(index);
                choices.put(firstLevel.getString(index), null);
                if (choice.equals("Other")) {
                    otherField = new JTextField();
                }
            }
            multiLevel = false;
        } catch (JSONException e) {
            JSONObject firstLevel = panelInfo.getJSONObject("values");
            for (String aKey : firstLevel.keySet()) {
                if (aKey.equals("Other")) {
                    otherField = new JTextField();
                    otherField.getDocument().addDocumentListener(this);
                }
                choices.put(aKey, new ArrayList<String>());
                JSONArray secondLevel = firstLevel.getJSONArray(aKey);
                for (int index = 0; index < secondLevel.length(); index++) {
                    choices.get(aKey).add(secondLevel.getString(index));
                }
            }
        }

        topLevelComboBox = new JComboBox<String>();
        for (String aKey : choices.keySet()) {
            topLevelComboBox.addItem(aKey);
        }
        topLevelComboBox.addActionListener(this);

        if (multiLevel) {
            lowLevelComboBox = new JComboBox<String>();
            updateSecondLevelChoices();
            lowLevelComboBox.addActionListener(this);
            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new GridLayout(2, 1));
            centerPanel.add(topLevelComboBox, BorderLayout.CENTER);
            centerPanel.add(lowLevelComboBox, BorderLayout.CENTER);
            super.add(centerPanel, BorderLayout.CENTER);
        } else {
            super.add(topLevelComboBox, BorderLayout.CENTER);
        }

        if (otherField != null) {
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new GridLayout(1, 2));
            bottomPanel.add(new JLabel("Other affiliation"));
            bottomPanel.add(otherField);
            super.add(bottomPanel, BorderLayout.PAGE_END);
        }
        setOtherField();
        updateResponse();
    }

    private void updateSecondLevelChoices() {
        lowLevelComboBox.removeAllItems();

        ArrayList<String> newChoices = choices.get(topLevelComboBox.getSelectedItem());

        Collections.sort(newChoices);

        for (String aChoice: newChoices) {
            lowLevelComboBox.addItem(aChoice);
        }
        lowLevelComboBox.setEnabled(lowLevelComboBox.getItemCount() > 0);

    }

    private void updateResponse() {
        if (!multiLevel || lowLevelComboBox == null || !lowLevelComboBox.isEnabled()) {
            if (topLevelComboBox.getSelectedItem().equals("Other")) {
                setOtherField();
            } else {
                Uploader.getInstance().getResponses().replaceKeyValue(getKey(), (String) topLevelComboBox.getSelectedItem());
            }
        } else {
            Uploader.getInstance().getResponses().replaceKeyValue(getKey(), (String) lowLevelComboBox.getSelectedItem());
        }
    }

    private void setOtherField() {
        if (otherField != null) {
            boolean enabled = topLevelComboBox.getSelectedItem().equals("Other");
            otherField.setText("");
            otherField.setEnabled(enabled);
            otherField.setBackground(enabled ? Color.WHITE : Color.GRAY);
            if (enabled) {
                otherField.getDocument().addDocumentListener(this);
            } else {
                otherField.getDocument().removeDocumentListener(this);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == topLevelComboBox) {
            setOtherField();
            if (multiLevel) {
                updateSecondLevelChoices();
            }
            updateResponse();
        }
        if (event.getSource() == lowLevelComboBox) {
            updateResponse();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), otherField.getText().trim());
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), otherField.getText().trim());
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), otherField.getText().trim());
    }
}
