import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashSet;

public class CheckboxPanel extends GenericPanel implements DocumentListener, ItemListener {
    private JCheckBox[] checkBoxes;

    private JTextField otherField;

    public CheckboxPanel(JSONObject panelInfo) {
        super(panelInfo);
        JSONArray values = panelInfo.getJSONArray("values");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(values.length(), 1));

        for (int index = 0; index < values.length(); index++) {
            String choice = values.getString(index);
            if (choice.equals("Other")) {
                otherField = new JTextField();
                otherField.setEnabled(false);
                otherField.setBackground(Color.GRAY);
                continue;
            }
            JCheckBox checkBox = new JCheckBox(choice);
            checkBox.addItemListener(this);
            centerPanel.add(checkBox);
        }


        if (otherField != null) {
            JCheckBox checkBox = new JCheckBox("Other");
            checkBox.addItemListener(this);
            centerPanel.add(checkBox);
            super.add(otherField, BorderLayout.PAGE_END);
        }

        super.add(centerPanel, BorderLayout.CENTER);

        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), new HashSet<String>());
    }

    private void replaceOtherValue(String value) {
        HashSet<String> currentSelection = (HashSet<String>) Uploader.getInstance().getResponses().get(getKey());
        HashSet<String> copy = new HashSet<String>(currentSelection);
        for (String aValue: copy) {
            if (aValue.startsWith("Other: ")) {
                currentSelection.remove(aValue);
                break;
            }
        }
        if (value.length() > 0) {
            currentSelection.add("Other: " + value);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent event) {
        replaceOtherValue(otherField.getText().trim());
    }

    @Override
    public void removeUpdate(DocumentEvent event) {
        replaceOtherValue(otherField.getText().trim());
    }

    @Override
    public void changedUpdate(DocumentEvent event) {
        replaceOtherValue(otherField.getText().trim());
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox)event.getSource();
            HashSet<String> currentSelection = (HashSet<String>) Uploader.getInstance().getResponses().get(getKey());
            boolean selected = checkBox.isSelected();
            if (checkBox.getText().equals("Other")) {
                otherField.setText("");
                otherField.setEnabled(selected);
                otherField.setBackground(selected ? Color.WHITE : Color.GRAY);
                if (selected) {
                    otherField.getDocument().addDocumentListener(this);
                } else {
                    otherField.getDocument().removeDocumentListener(this);
                }
            } else {
                if (selected) {
                    currentSelection.add(checkBox.getText());
                } else {
                    currentSelection.remove(checkBox.getText());
                }
            }
        }
    }
}
