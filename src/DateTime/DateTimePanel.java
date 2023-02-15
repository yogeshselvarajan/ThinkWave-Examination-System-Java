package DateTime;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateTimePanel  extends JFrame
{
    public JPanel addTimeFooter(JPanel contentPane) {
        JPanel panellow = new JPanel();
        panellow.setBounds(0, 732, 1366, 30);
        panellow.setBackground(new Color(255, 49, 49));
        panellow.setLayout(null);
        contentPane.add(panellow);

        Calendar now = Calendar.getInstance();
        String labeltime;
        final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        if(now.get(Calendar.AM_PM) == Calendar.AM)
            labeltime = dateFormat.format(now.getTime()) + " AM IST";
        else
            labeltime = dateFormat.format(now.getTime()) + " PM IST";
        JLabel time = new JLabel("  " + labeltime);
        time.setFont(new Font("Segoe Print", Font.BOLD, 18));
        time.setForeground(Color.WHITE);
        time.setBounds(1045, 0, 320, 30);
        Border border1 = BorderFactory.createLineBorder(Color.BLACK, 3);
        time.setBorder(border1);
        panellow.add(time);
        time.setVisible(true);
        new Timer(1000, e -> {
            Calendar now1 = Calendar.getInstance();
            if (now1.get(Calendar.AM_PM) == Calendar.AM)
                time.setText("  " + dateFormat.format(now1.getTime()) + " AM IST");
            else
                time.setText("  " + dateFormat.format(now1.getTime()) + " PM IST");
        }).start();

        JLabel lblDevelopedBy = new JLabel("Developed By: Yogesh S and Sakthipriyan S , CSE Department, Mepco Schlenk Engineering College, Sivakasi");
        lblDevelopedBy.setFont(new Font("Segoe Print", Font.BOLD, 18));
        lblDevelopedBy.setForeground(Color.WHITE);
        lblDevelopedBy.setBounds(35, 0, 1078, 30);
        panellow.add(lblDevelopedBy);

        return panellow;
    }

}
