package com.asiainfo.projectmg.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 *
 * @author king-pan
 * Date: 2018/9/13
 * Time: 上午10:55
 * Description: No Description
 */
public class DateUtil {

    public static final String FORMAT = "yyyy-MM-dd";

    public static Date getDateFromFormat(String dateText, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateText);
        return dateTime.toDate();
    }

    public static Date getDateFromFormat(String dateText) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateText);
        return dateTime.toDate();
    }

    public static String getDateText(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(FORMAT);
    }
}
