package socket.nio.demo3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by smq on 2017/5/26.
 */
public class DateFormatUtil {
    /**
     * 解决线程安全问题
     */
    private static final ThreadLocal<SimpleDateFormat> sdf = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static final String format(Date date) {
        return sdf.get().format(date);
    }

    public static final Date parse(String dateStr) {
        try {
            return sdf.get().parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("input str can not match pattern");
        }
    }

    public static final String format(Date date, String pattern) {
        SimpleDateFormat FORMATTER = new SimpleDateFormat();
        FORMATTER.applyPattern(pattern);
        return FORMATTER.format(date);
    }

    public static final Date parse(String str, String pattern) {
        if (null == str || "".equals(str)) {
            throw new RuntimeException("input str can not empty");
        }
        SimpleDateFormat FORMATTER = new SimpleDateFormat();
        FORMATTER.applyPattern(pattern);

        try {
            return FORMATTER.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException("input str can not match pattern");
        }
    }
}
