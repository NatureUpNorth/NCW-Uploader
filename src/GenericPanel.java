import org.json.JSONException;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class GenericPanel extends JPanel implements MouseListener {
    private String key;
    private String url;
    private JLabel descLabel;
    private String desc;

    public GenericPanel(JSONObject panelInfo) {
        super.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        try {
            descLabel = new JLabel();
            desc = panelInfo.getString("desc");
            augmentDescLabel("");
            topPanel.add(descLabel);
            if (panelInfo.has("url")) {
                url = panelInfo.getString("url");
                descLabel.addMouseListener(this);
            }
        } catch (JSONException e) {
        }
        try {
            topPanel.add(new Hint(panelInfo.getString("hint")));
        } catch (JSONException e) {
            // no hint
        }
        super.add(topPanel, BorderLayout.PAGE_START);
        try {
            super.setBorder(BorderFactory.createTitledBorder(panelInfo.getString("title")));
        } catch (JSONException e) {
            super.setBorder(BorderFactory.createTitledBorder(""));
        }

        try {
            key = panelInfo.getString("key");
            Uploader.getInstance().getResponses().addKey(key);
        } catch (JSONException e) {
            // there is no key for response for this panel
        }

    }

    public String getKey() {
        return key;
    }

    public void augmentDescLabel(String extra) {
        descLabel.setText("<html>" + desc + extra +  "</html>");
    }
    @Override
    public void mouseClicked(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (URISyntaxException | IOException e) {

        }
    }

    @Override
    public void mousePressed(MouseEvent event) {

    }

    @Override
    public void mouseReleased(MouseEvent event) {

    }

    @Override
    public void mouseEntered(MouseEvent event) {

    }

    @Override
    public void mouseExited(MouseEvent event) {

    }
}
