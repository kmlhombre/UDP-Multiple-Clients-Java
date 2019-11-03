import java.util.Calendar;
//klasa pobiera czas który upłynął od stycznia 1970
public class Czas {
    public Czas() {
    }
    public static long getGodzina(){
        return System.currentTimeMillis();
    }
}
