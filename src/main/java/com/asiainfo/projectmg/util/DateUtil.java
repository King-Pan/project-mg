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

    public static Date getDateFromFormat(String dateText, String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateText);
        return dateTime.toDate();
    }
}
