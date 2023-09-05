import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;

public class SliderPanel extends GenericPanel implements ChangeListener {
    private JSlider slider;
    private String unit;

    private double scale;

    public SliderPanel(JSONObject panelInfo) {
        super(panelInfo);

        int min = panelInfo.getInt("min");
        int max = panelInfo.getInt("max");

        slider = new JSlider(panelInfo.getBoolean("vertical") ? JSlider.VERTICAL : JSlider.HORIZONTAL,
                min, max,
                panelInfo.has("default") ? panelInfo.getInt("default") : (min + max) / 2
                );

        //slider.setMajorTickSpacing((max - min) / 10);
        //slider.setMinorTickSpacing((max - min) / 20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);

        scale = panelInfo.has("scale") ? panelInfo.getDouble("scale") : 1.0;
        unit = panelInfo.has("unit") ? panelInfo.getString("unit") : "";

        Hashtable labelTable = new Hashtable();
        labelTable.put( min, new JLabel("<html>" + min / scale + unit + "</html>") );
        labelTable.put( (max + min) / 2 , new JLabel("<html>" + (max + min) / 2.0 + unit + "</html>") );
        labelTable.put( max, new JLabel("<html>" + max / scale + unit + "</html>") );
        slider.setLabelTable( labelTable );

        updateDesc();

        super.add(slider, BorderLayout.CENTER);
    }

    private void updateDesc() {
        super.augmentDescLabel(": " + slider.getValue()  / scale + unit);
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), slider.getValue() / scale);
    }
    @Override
    public void stateChanged(ChangeEvent event) {
        if (event.getSource() == slider) {
            updateDesc();
        }
    }
}
