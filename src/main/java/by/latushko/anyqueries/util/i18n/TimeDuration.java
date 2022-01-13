package by.latushko.anyqueries.util.i18n;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static by.latushko.anyqueries.util.i18n.MessageKey.*;
import static by.latushko.anyqueries.util.i18n.MessageManager.SPACE_CHARACTER;

public class TimeDuration {
    public static String format(LocalDateTime fromDate, MessageManager manager) {
        LocalDateTime toDate = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(fromDate, toDate);
        long minutes = ChronoUnit.MINUTES.between(fromDate, toDate);
        long hours = ChronoUnit.HOURS.between(fromDate, toDate);
        long days = ChronoUnit.DAYS.between(fromDate, toDate);
        long weeks = ChronoUnit.WEEKS.between(fromDate, toDate);
        long months = ChronoUnit.MONTHS.between(fromDate, toDate);
        long years = ChronoUnit.YEARS.between(fromDate, toDate);

        String result = "";
        if(years > 0) {
            result = manager.getMessageInPlural(LABEL_YEAR, years);
        } else if(months > 0) {
            result = manager.getMessageInPlural(LABEL_MONTH, months);
        } else if(weeks > 0) {
            result = manager.getMessageInPlural(LABEL_WEEK, weeks);
        } else if(days > 0) {
            result = manager.getMessageInPlural(LABEL_DAY, days);
        } else if(hours > 0) {
            result = manager.getMessageInPlural(LABEL_HOUR, hours);
        } else if(minutes > 0) {
            result = manager.getMessageInPlural(LABEL_MINUTE, minutes);
        } else if(seconds > 0) {
            result = manager.getMessageInPlural(LABEL_SECOND, seconds);
        }

        if (result.isEmpty()) {
            return manager.getMessage(LABEL_NOW);
        } else {
            result += SPACE_CHARACTER + manager.getMessage(LABEL_AGO);
        }

        return result;
    }
}
