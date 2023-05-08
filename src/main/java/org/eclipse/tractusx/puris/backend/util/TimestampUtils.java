package org.eclipse.tractusx.puris.backend.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimestampUtils {

    public static final TimeZone tz = TimeZone.getTimeZone("GMT+1:00");
    // Quoted "Z" to indicate UTC, no timezone offset
    public static final DateFormat isoDateFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");


    public static String getCurrentIsoTimestamp() {

        TimestampUtils.isoDateFormat.setTimeZone(tz);
        return TimestampUtils.isoDateFormat.format(new Date());


    }

}
