package seedu.address.commons.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateTimeUtil {
    
    public static LocalDate changeDateToLocalDate(Date value) {
        Instant instant = value.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        return date;
    }
    
    public static LocalTime changeDateToLocalTime(Date value) {
        Instant instant = value.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalTime time = zdt.toLocalTime();
        return time;
    }
}
