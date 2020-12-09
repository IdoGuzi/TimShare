package classes;

public class Date {
    private int year, month, day, hour ,min ,dayOfWeek, timeZone;

    public Date(){
        year=0;
        month=0;
        day=0;
        hour=0;
        min=0;
        dayOfWeek=0;
        timeZone=0;
    }

    public Date(int year, int month, int day){
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=0;
        this.min=0;
        this.dayOfWeek=0;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }
}
