import java.util.Calendar;
import java.util.Date;

public class DateManager
{
    
    public static final Date startDate = new Date(1800464400000L); // Wednesday, January 20, 2027 12:00:00 PM GMT-05:00
    public static final Date endDate = new Date(1863622800000L); // Saturday, January 20, 2029 12:00:00 PM GMT-05:00
    public static Date currentGameDate = new Date(startDate.getTime()); // Updated throughout gameplay
    
    public static final long epochMillis = 1970 * DateManager.yearDuration;
    public static final int epochYear = 1970;
    public static final long timezoneCorrectionEST = 5L; // use this as the default / universal, since it is the time in Washington, DC.
    public static final long timezoneCorrectionEDT = 4L;
    public static final long timezoneCorrectionCST = 6L;
    public static final long timezoneCorrectionCDT = 5L;
    public static final long timezoneCorrectionMST = 7L;
    public static final long timezoneCorrectionMDT = 6L;
    public static final long timezoneCorrectionPST = 8L;
    public static final long timezoneCorrectionPDT = 7L;
    public static final long timezoneCorrectionAKST = 9L;
    public static final long timezoneCorrectionAKDT = 8L;
    public static final long timezoneCorrectionHST = 10L;
    public static final long timezoneCorrectionHDT = 9L;
    public static enum TimeZone
    {
        EST, EDT, CST, CDT, MST, MDT, PST, PDT, AKST, AKDT, HST, HDT
    }

    public static final long secondDuration = 1000L;
    public static final long minuteDuration = secondDuration * 60;
    public static final long hourDuration = minuteDuration * 60;
    public static final long dayDuration = hourDuration * 24;
    public static final long weekDuration = dayDuration * 7;
    // there is no single month duration because months vary in length
    public static final long leapYearDuration = 366 * dayDuration;
    public static final long yearDuration = 365 * dayDuration;
    public static final int daysInYear = 366;

    public static final long[] monthsDurationsMillis = {
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 29L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration
    }; // List of the lengths of all months from Jan 2027 to Jan 2029
    public static final int[] monthsDurationsDays = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31,
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31
    }; // List of the lengths of all months from Jan 2027 to Jan 2029
    public static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"}; // Will use lookup table in LANG_system_text to determine Month names - should remain lower-case
    public static final String[] monthAbbreviations = {"jan_abbreviation", "feb_abbreviation", "mar_abbreviation", "apr_abbreviation", "may_abbreviation", "jun_abbreviation", "jul_abbreviation", "aug_abbreviation", "sep_abbreviation", "oct_abbreviation", "nov_abbreviation", "dec_abbreviation"};
    public static final String[] yearNumbers = {"2027", "2028", "2029"};

    public static boolean incrementSecond(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + secondDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementQuarterMinute(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 15*secondDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHalfMinute(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 30*secondDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementMinute(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + minuteDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementQuarterHour(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 15*minuteDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHalfHour(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 30*minuteDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHour(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + hourDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementQuarterDay(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 3*hourDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHalfDay(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 6*hourDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementDay(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + dayDuration;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementMonth(){
        long currentTime = currentGameDate.getTime();
        int monthIndex = calculateMonthIndex(currentTime);

        long newTime = currentTime + monthsDurationsMillis[monthIndex];

        if(newTime > endDate.getTime()){
            return false; // Exceeded game end date
        }

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementYear(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + 31_557_600_000L; // 365.25 days

        // Check if the new time is within bounds
        if(newTime > endDate.getTime()){
            return false; // Exceeded game end date
        }

        currentGameDate.setTime(newTime);
        return true;
    }

    public static TimeZone matchTimeZone(String timeZoneString){
        for(TimeZone tz : TimeZone.values()){
            if(timeZoneString.equals(tz.toString())) return tz;
        }
        Engine.log("INVALID TIMEZONE", String.format("The supplied time zone name, %s, does not match any accepted time zone.", timeZoneString), new Exception());
        return null;
    }

    /**
     * Calculates the month index for the given time. (1-12)
     * @param time The time to calculate the month index for.
     * @return The month index for the given time.
     */
    private static int calculateMonthIndex(long time){
        long elapsed = time - startDate.getTime();
        long totalMonths = 0;

        for(int i = 0; i < monthsDurationsMillis.length; i++){
            if(elapsed < monthsDurationsMillis[i]){
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
    public static long millisecondsBetween(Date startDate, Date endDate){
        return Math.abs(endDate.getTime() - startDate.getTime());
    }

    /**
     * Calculates the amount of time in between the current game date and the given date.
     * @param date The date to calculate the time since.
     * @return The amount of time in milliseconds between the current game date and the given date.
     */
    public static long millisecondsAgo(Date date){
        return millisecondsBetween(DateManager.currentGameDate, date);
    }

    /**
     * Calculates the amount of time in between the current game date and the given year.
     * @param yearsAgo The year to calculate the time since.
     * @return The number of years between the current game date and the given year.
     */
    public static int yearYearsAgo(double yearsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateManager.currentGameDate);

        // Subtract the number of years (rounded to an integer)
        int targetYear = calendar.get(Calendar.YEAR) - (int) Math.floor(yearsAgo);
        return targetYear;
    }
    /**
     * Calculates and returns the number of milliseconds since epoch for Jan 1 00:00:00 in a given year, adjusted to the EST timezone. Only accurate in the range [1583,292278994].
     * @param year The year to calculate (1900, 1970, 2000)
     * @return The number of milliseconds since epoch.
     */
    public static long yearToMillis(int year){
        if(year < 1583 || year > 292_278_994){
            Engine.log("DATE OUT OF BOUNDS", String.format("The year requested, %s, is out of the accurate bounds of [1583,292278994].", year));
            return Long.MAX_VALUE;
        }
        long millis = ((long) year) * yearDuration - epochMillis;
        // calculate for leap years
        int numberLeapYears = 0;
        if(year > epochYear)
        for(int i = epochYear; i < year; i++){
            if(isLeapYear(i)) numberLeapYears++;
        }
        else if(year < epochYear)
        for(int i = year; i <= epochYear; i++){
            if(isLeapYear(i)) numberLeapYears--;
        }
        // no calculation required if year == epochYear
        millis += (numberLeapYears * dayDuration) + (timezoneCorrectionEST * hourDuration);
        return millis;
    }

    public static double timeToMillis(long time){
        return time / 1.0;
    }
    public static double timeToSeconds(long time){
        return time * 1.0 / secondDuration;
    }
    public static double timeToDays(long time){
        return time * 1.0 / dayDuration;
    }
    public static double timeToWeeks(long time){
        return time * 1.0 / weekDuration;
    }
    public static double timeToYears(long time){
        return time * 1.0 / yearDuration;
    }

    public static boolean isLeapYear(int year){
        if(year % 100 == 0){
            return year % 400 == 0;
        }
        return year % 4 == 0;
    }

    /**
     * Returns the date in the format MM/DD for the given day ordinal. Includes leap years (i = 59).
     * @param dayOrdinal The day ordinal to convert to a date.
     * @return The date in the format MM/DD.
     */
    public static String ordinalToDateFormat(int dayOrdinal){
        if(dayOrdinal < 0 || dayOrdinal > daysInYear-1){
            Engine.log("INVALID DAY ORDINAL", String.format("The day ordinal %d is out of bounds. Must be between 0 and %d.", dayOrdinal, daysInYear), new Exception());
            return null;
        }
        if(dayOrdinal == 59) return "02/29"; // Leap year
        if(dayOrdinal > 59) dayOrdinal--; // Leap year

        int day = 0, month = 0;
        int elapsed = 0;
        for(int i = 0; i < monthsDurationsDays.length; i++){
            elapsed += monthsDurationsDays[i];
            if(elapsed > dayOrdinal){
                month = i+1;
                break;
            }
        }
        // get the day part
        day = monthsDurationsDays[month-1] - (elapsed - dayOrdinal) + 1;

        if(month == 0 || day == 0){
            Engine.log("DATE CALCULATION ERROR", String.format("The date calculation failed to produce a valid date for day ordinal %d.", dayOrdinal), new Exception());
            return null;
        }

        return String.format("%02d/%02d", month, day);
    }
    /**
     * Returns the day ordinal for the given date in the format MM/DD. Includes leap years (i = 59).
     * @param dateFormat The date in the format MM/DD to convert to a day ordinal.
     * @return The day ordinal for the given date.
     */
    public static int dateFormatToOrdinal(String dateFormat){

        String[] parts = dateFormat.split("[-//]");
        if(parts.length != 2){
            Engine.log("INVALID DATE FORMAT", String.format("The date format \"%s\" is invalid. Must be in the format MM/DD.", dateFormat), new Exception());
            return -1;
        }
        int month = Integer.parseInt(parts[0]);
        int day = Integer.parseInt(parts[1]);

        if(month < 1 || month > 12 || day < 1 || day > 31){
            Engine.log("INVALID DATE FORMAT", String.format("The date format \"%s\" is invalid. Must be in the format MM/DD.", dateFormat), new Exception());
            return -1;
        }
        int result = 0;
        for(int i = 0; i < month-1; i++){
            result += monthsDurationsDays[i];
        }
        result += day - 1;
        if(month == 2 && day == 29) return 59; // Leap year
        else if(result >= 59) result++; // Leap year

        return result;
    }
}
