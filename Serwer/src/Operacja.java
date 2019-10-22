import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operacja {
    private static String KOMUNIKAT;
    private static String message;

    public Operacja(String KOMUNIKAT) {
        Operacja.KOMUNIKAT = KOMUNIKAT;
        String temp = "";
        message = "oper#";
    }

    private static float getResult() {

        float result = 0;
        int liczby[] = new int[3];
        int counter = 0;


        //mechanizm wykrywajacy inty w ciagu znakow
        Pattern p = Pattern.compile("\\d+#\\d+#\\d+@");
        Matcher m = p.matcher(Operacja.KOMUNIKAT);
        while(m.find()) {
            if(counter>3) {
                liczby[counter-3] = Integer.parseInt(m.group());
            }
            counter++;
        }
        //if(temp.equals("mn")) {
        if((Pattern.compile("mnozenie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] * liczby[1] * liczby[2];
            message += "mnozenie@";
        }
        else if((Pattern.compile("dzielenie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = (float) (liczby[0] * 1.0 / liczby[1] / liczby[2]);
            message += "dzielenie@";
        }
        else if((Pattern.compile("dodawanie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] + liczby[1] + liczby[2];
            message += "dodawanie@";
        }
        else if((Pattern.compile("odejmowanie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] - liczby[1] - liczby[2];
            message += "odejmowanie@";
        }
        return result;
    }

    public static String createMessage() {
        message += "stat#OK@iden#" + Czas.getGodzina() + "##";
        message += getResult() + "@";
        return message;
    }
}
