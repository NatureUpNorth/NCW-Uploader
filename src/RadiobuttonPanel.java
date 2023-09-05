import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RadiobuttonPanel extends GenericPanel implements ActionListener {
    private JRadioButton[] radioButtons;

    public RadiobuttonPanel(JSONObject panelInfo) {
        super(panelInfo);
        JSONArray values = panelInfo.getJSONArray("values");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(values.length(), 1));

        ButtonGroup radioGroup = new ButtonGroup();

        for (int index = 0; index < values.length(); index++) {
            JRadioButton radioButton = new JRadioButton(values.getString(index));
            centerPanel.add(radioButton);
            radioButton.addActionListener(this);
            radioGroup.add(radioButton);
            radioButton.setSelected(index == 0);
            if (index == 0) {
                Uploader.getInstance().getResponses().replaceKeyValue(getKey(), values.getString(index));
            }
        }

        super.add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() instanceof JRadioButton) {
            JRadioButton selectedButton = (JRadioButton)event.getSource();
            Uploader.getInstance().getResponses().replaceKeyValue(getKey(), selectedButton.getText());
        }
    }
}
