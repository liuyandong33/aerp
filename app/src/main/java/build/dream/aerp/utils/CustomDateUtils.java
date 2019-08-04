package build.dream.aerp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import build.dream.aerp.constants.Constants;

public class CustomDateUtils {
    public static Date parse(String source, String pattern) {
        return parse(new SimpleDateFormat(pattern), source);
    }

    public static Date parse(SimpleDateFormat simpleDateFormat, String source) {
        try {
            return simpleDateFormat.parse(source);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static SimpleDateFormat buildISO8601SimpleDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.ISO8601_DATE_PATTERN, Locale.US);
        simpleDateFormat.setTimeZone(new SimpleTimeZone(0, Constants.GMT));
        return simpleDateFormat;
    }
}
