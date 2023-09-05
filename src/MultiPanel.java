import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;

public class MultiPanel extends GenericPanel {
    public MultiPanel(JSONObject panelInfo) {
        super(panelInfo);

        JPanel centerPanel = new JPanel();
        JSONArray subPanels = panelInfo.getJSONArray("subPanels");
        if (panelInfo.getBoolean("vertical")) {
            centerPanel.setLayout(new GridLayout(subPanels.length(), 1));
        } else {
            centerPanel.setLayout(new GridLayout(1, subPanels.length()));
        }
        for (int index = 0; index < subPanels.length(); index++) {
            JSONObject subPanelInfo = subPanels.getJSONObject(index);
            centerPanel.add(Tab.createPanel(subPanelInfo));
        }
        super.add(centerPanel, BorderLayout.CENTER);
    }
}
