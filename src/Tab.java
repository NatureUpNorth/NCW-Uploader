import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Tab {
	private String title;
	private JPanel panel;

	public Tab(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void addSubPanel(JSONObject panelInfo) {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new GridLayout());
		}
		panel.add(createPanel(panelInfo));
	}

	public JPanel getPanel() {
		return panel;
	}

	public static JPanel createPanel(JSONObject panelInfo) {
		String type = panelInfo.getString("type");
		switch (type) {
			case "fileSelect":
				return new FileSelectPanel(panelInfo);
			case "latlongPanel":
				return new LatLongPanel(panelInfo);
			case "multiPanel":
				return new MultiPanel(panelInfo);
			case "dropdown":
				return new DropdownPanel(panelInfo);
			case "date":
				return new DatePanel(panelInfo);
			case "slider":
				return new SliderPanel(panelInfo);
			case "multiChoiceList":
				return new CheckboxPanel(panelInfo);
			case "singleChoiceList":
				return new RadiobuttonPanel(panelInfo);
			case "form":
				return new FormPanel(panelInfo);
			default:
				throw new RuntimeException("Error: unrecognized panel type: " + type);
		}
	}
}
