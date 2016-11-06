package seedu.address.commons.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
//@@author A0141054W

/**
 * Change the data type of date fromt Date to LocalDate and the other way around
 * as well as Date and LocalTime
 */
public class DateTimeUtil {
    
    public static LocalDate changeDateToLocalDate(Date value) {
        if (value == null || value.toInstant() == null)
            return null;
        Instant instant = value.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalDate date = zdt.toLocalDate();
        return date;
    }
    
    public static LocalTime changeDateToLocalTime(Date value) {
        if (value == null || value.toInstant() == null)
            return null;
        Instant instant = value.toInstant();
        ZonedDateTime zdt = instant.atZone(ZoneId.systemDefault());
        LocalTime time = zdt.toLocalTime();
        return time;
    }
    
    public static String changeLocalDateToFormattedString(LocalDate value) {
        if (value == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedString = value.format(formatter);
        return formattedString;
    }
    
    public static String changeLocalTimeToFormattedString(LocalTime value) {
        if (value == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedString = value.format(formatter);
        return formattedString;
    }
}
