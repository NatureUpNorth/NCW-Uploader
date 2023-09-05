import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.demo.FullDemo;
import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;

public class DatePanel extends GenericPanel implements DateChangeListener {
    private DatePicker calendar;
    public DatePanel(JSONObject panelInfo) {
        super(panelInfo);

        JPanel centerPanel = new JPanel();

        // Add calendar
        URL dateImageURL = FullDemo.class.getResource("/images/datepickerbutton1.png");
        Image dateImage = Toolkit.getDefaultToolkit().getImage(dateImageURL);
        ImageIcon dateIcon = new ImageIcon(dateImage);

        DatePickerSettings dateSettings = new DatePickerSettings();;
        calendar = new DatePicker(dateSettings);
        calendar.setDateToToday();
        JButton datePickerButton = calendar.getComponentToggleCalendarButton();
        datePickerButton.setText("");
        datePickerButton.setIcon(dateIcon);
        dateSettings.setAllowKeyboardEditing(false);
        calendar.addDateChangeListener(this);

        updateKeyValue();

        super.add(calendar, BorderLayout.CENTER);
    }

    private void updateKeyValue() {
        Uploader.getInstance().getResponses().replaceKeyValue(getKey(), Date.from(calendar.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }
    @Override
    public void dateChanged(DateChangeEvent dateChangeEvent) {
        updateKeyValue();
    }
}
