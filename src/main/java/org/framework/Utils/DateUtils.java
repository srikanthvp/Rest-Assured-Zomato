package org.framework.Utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    SimpleDateFormat formatter;
    Date date;


    public String getTodaysdate(String format) {
        String time = null;

        formatter = new SimpleDateFormat("dd/MM/yy");

        date = new Date();
        time = formatter.format(date);

        if (format.equalsIgnoreCase("dd")) {
            return time.split("/")[0];
        }
        if (format.equalsIgnoreCase("mm")) {
            return time.split("/")[1];
        } else {
            return time.split("/")[2];
        }


    }
}

