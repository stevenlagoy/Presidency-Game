package main.core;

import java.util.Calendar;
import java.util.Date;

public class TimeManager extends Manager {

    // STATIC VARIABLES ---------------------------------------------------------------------------

    /** Minimum safe year to use with methods in DateManager. */
    public static final int MIN_SAFE_YEAR = 1583;
    /** Maximum safe year to use with methods in DateManager. */
    public static final int MAX_SAFE_YEAR = 292_278_994;
    
    /** Date on which the game starts. Wednesday, January 20, 2027 12:00:00 PM GMT-05:00 */
    public static final Date startDate = new Date(1800464400000L);
    /** Date on which the game ends. Saturday, January 20, 2029 12:00:00 PM GMT-05:00 */
    public static final Date endDate = new Date(1863622800000L);
    
    /** The Epoch, 1970. */
    public static final int epochYear = 1970;
    /** Milliseconds since the year zero corresponding to the epoch. */
    public static final long epochMillis = epochYear * TimeManager.yearDuration;

    /**
     * Time Zones define the number of hours before or ahead of UTC an area of the world is. Use the correction value to get the number of hours to correct for.
     */
    public static enum TimeZone {
        EST(5L), // Default, since it is the time in Washington, DC.
        EDT(4L),
        CST(6L),
        CDT(5L),
        MST(7L),
        MDT(6L),
        PST(8L),
        PDT(7L),
        AKST(9L),
        AKDT(8L),
        HST(10L),
        HDT(9L);

        public final long correction;
        private TimeZone(long correction) { this.correction = correction; }
    }

    /** Duration in milliseconds of one second. */
    public static final long secondDuration = 1000L;
    /** Duration in milliseconds of one minute. */
    public static final long minuteDuration = secondDuration * 60;
    /** Duration in milliseconds of one hour. */
    public static final long hourDuration = minuteDuration * 60;
    /** Duration in milliseconds of one day. */
    public static final long dayDuration = hourDuration * 24;
    /** Duration in milliseconds of one week. */
    public static final long weekDuration = dayDuration * 7;
    // there is no single month duration because months vary in length
    /** Duration in milliseconds of one leap year. */
    public static final long leapYearDuration = 366 * dayDuration;
    /** Duration in milliseconds of one standard year. */
    public static final long yearDuration = 365 * dayDuration;
    /** Number of days in a year. TODO */
    public static final int daysInYear = 366;

    /** Duration in milliseconds of each month between January 2027 and January 2029. Index by ordinal month value. */
    public static final long[] monthsDurationsMillis = {
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 29L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration
    };
    /** Lengths in days of each month between January 2027 and January 2029. Index by ordinal month value. */
    public static final int[] monthsDurationsDays = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    };
    /** Names of each month. Lookup from LANG_system_text. */
    public static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
    /** Abbreviations of each month. Lookup from LANG_system_text. */
    public static final String[] monthAbbreviations = {"jan_abbreviation", "feb_abbreviation", "mar_abbreviation", "apr_abbreviation", "may_abbreviation", "jun_abbreviation", "jul_abbreviation", "aug_abbreviation", "sep_abbreviation", "oct_abbreviation", "nov_abbreviation", "dec_abbreviation"};
    /** Numbers of each year between 2027 and 2029. */
    public static final String[] yearNumbers = {"2027", "2028", "2029"};

    // STATIC CLASS FUNCTIONS ---------------------------------------------------------------------

    /**
     * Calculates and returns the number of milliseconds since epoch for Jan 1 00:00:00 in a given year, adjusted to the EST timezone.
     * @param year The year to calculate (1900, 1970, 2000)
     * @return The number of milliseconds since epoch.
     */
    public static long yearToMillis(int year) {
        if (year < MIN_SAFE_YEAR || year > MAX_SAFE_YEAR) {
            Engine.log("DATE OUT OF BOUNDS", String.format("The year requested, %s, is out of the accurate bounds of [1583,292278994].", year), new Exception());
            return -1L;
        }
        long millis = ((long) year) * yearDuration - epochMillis;
        // calculate for leap years
        int numberLeapYears = 0;
        if (year > epochYear)
        for(int i = epochYear; i < year; i++) {
            if (isLeapYear(i)) numberLeapYears++;
        }
        else if (year < epochYear)
        for(int i = year; i <= epochYear; i++) {
            if (isLeapYear(i)) numberLeapYears--;
        }
        // no calculation required if year == epochYear
        millis += (numberLeapYears * dayDuration) + (TimeZone.EST.correction * hourDuration);
        return millis;
    }

    /** Calculates the number of seconds in the given number of milliseconds. */
    public static double timeToSeconds(long time) {
        return time * 1.0 / secondDuration;
    }
    /** Calculates the number of days in the given number of milliseconds. */
    public static double timeToDays(long time) {
        return time * 1.0 / dayDuration;
    }
    /** Calculates the number of weeks in the given number of milliseconds. */
    public static double timeToWeeks(long time) {
        return time * 1.0 / weekDuration;
    }
    /** Calculates the number of years in the given number of milliseconds. */
    public static double timeToYears(long time) {
        return time * 1.0 / yearDuration;
    }

    /** Determines whether a given year is a leap year. */
    public static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            return year % 400 == 0;
        }
        return year % 4 == 0;
    }

    /**
     * Creates a date from a formatted string.
     * @param dateString The String to parse into a Date.
     * @return Parsed date, or {@code null} if unsuccessful.
     */
    public static Date dateFromString(String dateString) {
        String[] dateParts = dateString.split("[-//]", 3);
        if (dateParts.length < 3) return null;
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar months are 0-indexed
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Returns the date in the format MM/DD for the given day ordinal. Includes leap years (i = 59).
     * @param dayOrdinal Day ordinal to convert to a date.
     * @return Date in the format MM/DD.
     */
    public static String ordinalToDateFormat(int dayOrdinal) {
        if (dayOrdinal < 0 || dayOrdinal > daysInYear-1) {
            Engine.log("INVALID DAY ORDINAL", String.format("The day ordinal %d is out of bounds. Must be between 0 and %d.", dayOrdinal, daysInYear), new Exception());
            return null;
        }
        if (dayOrdinal == 59) return "02/29"; // Leap year
        if (dayOrdinal > 59) dayOrdinal--; // Leap year

        int day = 0, month = 0;
        int elapsed = 0;
        for(int i = 0; i < monthsDurationsDays.length; i++) {
            elapsed += monthsDurationsDays[i];
            if (elapsed > dayOrdinal) {
                month = i+1;
                break;
            }
        }
        // get the day part
        day = monthsDurationsDays[month-1] - (elapsed - dayOrdinal) + 1;

        if (month == 0 || day == 0) {
            Engine.log("DATE CALCULATION ERROR", String.format("The date calculation failed to produce a valid date for day ordinal %d.", dayOrdinal), new Exception());
            return null;
        }

        return String.format("%02d/%02d", month, day);
    }

    /**
     * Returns the day ordinal for the given date in the format MM/DD. Includes leap years (i = 59).
     * @param dateFormat Date in the format MM/DD to convert to a day ordinal.
     * @return Day ordinal for the given date.
     */
    public static int dateFormatToOrdinal(String dateFormat) {
        String[] parts = dateFormat.split("[-//]");
        if (parts.length != 2) {
            Engine.log("INVALID DATE FORMAT", String.format("The date format \"%s\" is invalid. Must be in the format MM/DD.", dateFormat), new Exception());
            return -1;
        }
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);

        if (month < 1 || month > 12 || day < 1 || day > 31) {
            Engine.log("INVALID DATE FORMAT", String.format("The date \"%s\" is invalid. Months must be between 1 and 12, and days must be between 1 and 31.", dateFormat), new Exception());
            return -1;
        }
        int result = 0;
        for(int i = 0; i < month-1; i++) {
            result += monthsDurationsDays[i];
        }
        result += day - 1;
        if (month == 2 && day == 29) return 59; // Leap year
        else if (result >= 59) result++; // Leap year

        return result;
    }

    // INSTANCE VARIABLES -------------------------------------------------------------------------

    /** State of the Manager. */
    private ManagerState currentState; 

    /** Current Date of Gameplay. */
    private Date currentGameDate;

    // CONSTRUCTORS -------------------------------------------------------------------------------

    /** Create a new inactive DateManager. */
    public TimeManager() {
        currentState = ManagerState.INACTIVE;
    }

    // MANAGER METHODS ----------------------------------------------------------------------------

    /** Initialize and Activate this DateManager. */
    @Override
    public boolean init() {
        currentGameDate = new Date(startDate.getTime());
        currentState = ManagerState.ACTIVE;
        return true;
    }

    /** Get the current State of this DateManager. */
    @Override
    public ManagerState getState() {
        return currentState;
    }

    /** Deactivate and clean up the data of this DateManager. */
    @Override
    public boolean cleanup() {
        currentGameDate = null;
        currentState = ManagerState.INACTIVE;
        return true;
    }

    // GETTERS AND SETTERS ------------------------------------------------------------------------

    // Current Date : Date
    public Date getCurrentDate() {
        return currentGameDate;
    }
    public String getFormattedCurrentDate() {
        StringBuilder dateString = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);
        
        dateString.append(getCurrentYear()).append("-").append(getCurrentMonth()).append("-").append(getCurrentDay()).append("-").append(getFormattedCurrentTime().replace(":","-"));
        return dateString.toString();
    }

    // Current Year : int
    public int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);
        return calendar.get(Calendar.YEAR);
    }

    // Current Month : int
    public int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);
        return calendar.get(Calendar.MONTH) + 1; // Adding 1 since Calendar months are 0-based
    }

    // Current Day : int
    public int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    // Current Time
    public String getFormattedCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);
        return String.format("%02d:%02d:%02d", 
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND));
    }

    // INCREMENT METHODS --------------------------------------------------------------------------

    /**
     * Increments the current game date by one second.
     */
    public boolean incrementSecond() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + secondDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by a quarter minute (15 secs).
     */
    public boolean incrementQuarterMinute() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 15*secondDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by half a minute (30 secs).
     */
    public boolean incrementHalfMinute() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 30*secondDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by one minute (60 secs).
     */
    public boolean incrementMinute() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + minuteDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by a quarter hour (15 mins).
     */
    public boolean incrementQuarterHour() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 15*minuteDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by half an hour (30 mins).
     */
    public boolean incrementHalfHour() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 30*minuteDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by one hour (60 mins).
     */
    public boolean incrementHour() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + hourDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by a quarter day (6 hours).
     */
    public boolean incrementQuarterDay() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 3*hourDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by half a day (12 hours).
     */
    public boolean incrementHalfDay() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 6*hourDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    /**
     * Increments the current game date by one day (24 hours).
     */
    public boolean incrementDay() {
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + dayDuration;

        if (newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }

    // MATCHING METHODS ---------------------------------------------------------------------------

    /**
     * Matches the given time zone string to a time zone object.
     * @param timeZoneString The time zone string to match.
     * @return The time zone object that matches the given string.
     */
    public TimeZone matchTimeZone(String timeZoneString) {
        for(TimeZone tz : TimeZone.values()) {
            if (timeZoneString.equals(tz.toString())) return tz;
        }
        Engine.log("INVALID TIMEZONE", String.format("The supplied time zone name, %s, does not match any accepted time zone.", timeZoneString), new Exception());
        return null;
    }

    /**
     * Calculates the month index for the given time. (1-12)
     * @param time The time to calculate the month index for.
     * @return The month index for the given time.
     */
    public static int calculateMonthIndex(long time) {
        long elapsed = time - startDate.getTime();
        long totalMonths = 0;

        for(int i = 0; i < monthsDurationsMillis.length; i++) {
            if (elapsed < monthsDurationsMillis[i]) {
                return i;
            }
            elapsed -= monthsDurationsMillis[i];
            totalMonths++;
        }

        return totalMonths >= monthsDurationsMillis.length ? monthsDurationsMillis.length - 1 : (int) totalMonths;
    }
    /**
     * Calculates the amount of time in between two dates.
     * @param startDate The start date to calculate the time from.
     * @param endDate The end date to calculate the time unil.
     * @return The amount of time in milliseconds between the two dates.
     */
    public static long millisecondsBetween(Date startDate, Date endDate) {
        return Math.abs(endDate.getTime() - startDate.getTime());
    }

    /**
     * Calculates the amount of time in between the current game date and the given date.
     * @param date The date to calculate the time since.
     * @return The amount of time in milliseconds between the current game date and the given date.
     */
    public long millisecondsAgo(Date date) {
        return millisecondsBetween(currentGameDate, date);
    }

    /**
     * Calculates the number of years between the current game date and the given date.
     * @param date A past date to use in the calculation.
     * @return The number of years (whole number) ago which the date represents.
     * @see #millisecondsAgo(Date)
     */
    public int yearsAgo(Date date) {
        return (int) (millisecondsAgo(date) / yearDuration);
    }

    /**
     * Calculates the number of full years in between the current game date and the given year.
     * @param yearsAgo The year to calculate the time since.
     * @return The number of years between the current game date and the given year.
     */
    public int yearYearsAgo(double yearsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentGameDate);

        // Subtract the number of years (rounded to an integer)
        int targetYear = calendar.get(Calendar.YEAR) - (int) Math.floor(yearsAgo);
        if (targetYear < MIN_SAFE_YEAR || targetYear > MAX_SAFE_YEAR) {
            Engine.log("DATE OUT OF BOUNDS", String.format("The year requested, %s, is out of the accurate bounds of [1583,292278994].", targetYear), new Exception());
            return -1;
        }
        return targetYear;
    }

    // REPRESENTATION METHODS ---------------------------------------------------------------------

    @Override
    public String toRepr() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toRepr'");
    }

    @Override
    public Manager fromRepr(String repr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromRepr'");
    }
}
