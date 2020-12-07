package gui.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility for converting between a string and LocalDateTime objects
 */
public class DateParser {

    private String dateTimeFormat = "MM-dd-yyyy HH:mm";

    /**
     * Method to convert a string to a LocalDateTime object
     *
     * @param inputDateTime string to convert
     * @return date time object
     */
    public LocalDateTime stringToDateTime(String inputDateTime) {
        return stringToDateTime(inputDateTime, dateTimeFormat);
    }

    /**
     * Method to convert a string to a LocalDateTime object
     *
     * @param inputDateTime string to convert
     * @param template      date format template
     * @return date time object
     */
    public LocalDateTime stringToDateTime(String inputDateTime, String template) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(template);
        return LocalDateTime.parse(inputDateTime, formatter);
    }

    /**
     * Method to convert a LocalDateTime object to a string
     *
     * @param localDateTime LocalDateTime object to convert
     * @return date time object
     */
    public String dateTimeToString(LocalDateTime localDateTime) {
        return dateTimeToString(localDateTime, dateTimeFormat);
    }

    /**
     * Method to convert a LocalDateTime object to a string
     *
     * @param localDateTime LocalDateTime object to convert
     * @param template      date format template
     * @return date time object
     */
    public String dateTimeToString(LocalDateTime localDateTime, String template) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(template);
        return localDateTime.format(formatter);
    }

    public String getFormat() {
        return dateTimeFormat;
    }
}
