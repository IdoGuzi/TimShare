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
    public Date(int year, int month, int day,int hour,int min){
        this.year=year;
        this.month=month;
        this.day=day;
        this.hour=hour;
        this.min=min;
        this.dayOfWeek=0;
    }

    public boolean before(Date d){
        if (year<d.getYear()) {
            return true;
        }else if (year==d.getYear()){
            if (month<d.getMonth()) {
                return true;
            }else if (month==d.getMonth()){
                if (day<d.getDay()){
                    return true;
                }else if (day==d.getDay()){
                    if (hour<d.getHour()){
                        return true;
                    }else if (hour==d.getHour()){
                        if (min<=d.getMin()){
                            return true;
                        }else return false;
                    }else return false;
                }else return false;
            }else return false;
        }else return false;
    }

    public boolean after(Date d){
        return !this.before(d);
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


    @Override
    public String toString(){
        return year+"/"+month+"/"+day+" "+hour+":"+min;
    }
}
