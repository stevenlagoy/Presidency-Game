import java.util.Calendar;
import java.util.Date;

public class DateManager
{
    
    public static final Date startDate = new Date(1800464400000L); // Wednesday, January 20, 2027 12:00:00 PM GMT-05:00
    public static final Date endDate = new Date(1863622800000L); // Saturday, January 20, 2029 12:00:00 PM GMT-05:00
    public static Date currentGameDate = new Date(startDate.getTime()); // Updated throughout gameplay

    public static final long secondDuration = 1000L;
    public static final long minuteDuration = secondDuration * 60;
    public static final long hourDuration = minuteDuration * 60;
    public static final long dayDuration = hourDuration * 24;

    public static final long yearDuration = 31_557_600_000L; // 365.25 days
    public static final long[] monthDurations = {
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 29L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration,
        31L*dayDuration, 28L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration, 30L*dayDuration, 31L*dayDuration
    }; // List of the lengths of all months from Jan 2027 to Jan 2029
    public static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"}; // Will use lookup table in LANG_system_text to determine Month names
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
        long newTime = currentTime + minuteDuration / 4;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHalfMinute(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + minuteDuration / 2;

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
        long newTime = currentTime + hourDuration / 4;

        if(newTime > endDate.getTime()) return false;

        currentGameDate.setTime(newTime);
        return true;
    }
    public static boolean incrementHalfHour(){
        long currentTime = currentGameDate.getTime();
        long newTime = currentTime + hourDuration / 2;

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

        long newTime = currentTime + monthDurations[monthIndex];

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
    private static int calculateMonthIndex(long time){
        long elapsed = time - startDate.getTime();
        long totalMonths = 0;

        for(int i = 0; i < monthDurations.length; i++){
            if(elapsed < monthDurations[i]){
                return i;
            }
            elapsed -= monthDurations[i];
            totalMonths++;
        }

        return totalMonths >= monthDurations.length ? monthDurations.length - 1 : (int) totalMonths;
    }

    public static long millisecondsBetween(Date startDate, Date endDate){
        return Math.abs(endDate.getTime() - startDate.getTime());
    }

    public static long millisecondsAgo(Date date){
        return millisecondsBetween(DateManager.currentGameDate, date);
    }

    public static int calculateYear(double yearsAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateManager.currentGameDate);

        // Subtract the number of years (rounded to an integer)
        int targetYear = calendar.get(Calendar.YEAR) - (int) Math.floor(yearsAgo);
        return targetYear;
    }


    public static boolean isLeapYear(int year){
        if(year % 100 == 0){
            if(year % 400 != 0) return false;
            return true;
        }
        if(year % 4 == 0){
            return true;
        }
        return false;
    }
}
