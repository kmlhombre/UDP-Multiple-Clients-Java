import java.util.Calendar;

public class Czas {
    public Czas() {
    }
    public static String getGodzina(){
        Calendar now = Calendar.getInstance();
        String time ="";
        int h = now.get(Calendar.HOUR_OF_DAY);
        int m = now.get(Calendar.MINUTE);
        if(h < 10) {
            time += "0" + h;
        }
        else {
            time += h;
        }
        time += ":";
        if(m < 10) {
            time += "0" + m;
        }
        else {
            time += m;
        }
        return time;
    }
}
