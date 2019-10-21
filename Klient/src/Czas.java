import java.util.Calendar;

public class Czas {
    public Czas() {
    }
    public static String getGodzina(){
        Calendar now = Calendar.getInstance();
       return (now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
    }
}
