import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class FormPanel extends GenericPanel implements DocumentListener  {
    private JTextArea comments;
    public FormPanel(JSONObject panelInfo) {
        super(panelInfo);

        comments = new JTextArea(10, 50);
        comments.setLineWrap(true);

        JScrollPane commentScroll = new JScrollPane( comments,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        super.add(commentScroll, BorderLayout.CENTER);

        comments.getDocument().addDocumentListener(this);

        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), "");
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), comments.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), comments.getText());
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), comments.getText());
    }
}
