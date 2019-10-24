import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operacja {

    private String message;
    private float result;

    private static String OPERACJA = "oper#";
    private static String ID = "";
    private static String KOMUNIKAT;


    public Operacja(String KOMUNIKAT) {
        Operacja.KOMUNIKAT = KOMUNIKAT;
        message = "";
        result = 0;

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(KOMUNIKAT);

        if (m.find()) {
            ID = m.group();
        }
    }

    private void setDefaultTextOfStatement() { //zresetowanie pola operacja i iden
        OPERACJA = "oper#";
    }

    private void calculateResultAndGetOPERACJAString() {
        float[] liczby = new float[3];
        int counter = 0;

        //regex wykrywajacy 3 liczby w otrzymanym komunikacie od klienta
        Pattern p = Pattern.compile("\\d+\\.*\\d*#\\d+\\.*\\d*#\\d\\.*\\d*@");
        Matcher m = p.matcher(Operacja.KOMUNIKAT);

        if (m.find()) {
            String temp = m.group();
            p = Pattern.compile("\\d+\\.*\\d*");
            m = p.matcher(temp);

            while (m.find()) {
                liczby[counter] = Float.parseFloat(m.group());
                counter++;
            }
        }

        if (Pattern.compile("mnozenie").matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] * liczby[1] * liczby[2];
            OPERACJA += "mnozenie@";
        } else if (Pattern.compile("dzielenie").matcher(Operacja.KOMUNIKAT).find()) {
            result = (float) (liczby[0] * 1.0 / liczby[1] / liczby[2]);
            OPERACJA += "dzielenie@";
        } else if (Pattern.compile("dodawanie").matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] + liczby[1] + liczby[2];
            OPERACJA += "dodawanie@";
        } else if (Pattern.compile("odejmowanie").matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] - liczby[1] - liczby[2];
            OPERACJA += "odejmowanie@";
        }

    }

    String createMessage() {
        if(KOMUNIKAT.equals("oper#id@")) {
            message = "oper#id#" + UDPSerwer.getIdForUser() + "@iden#" + Czas.getGodzina() + "@";
        }
        else if(Pattern.compile("oper#ERROR@").matcher(KOMUNIKAT).find()) {
            message = "oper#ERROR@stat#null@iden#" + ID + "#" + Czas.getGodzina() + "@";
        }
        else {
            calculateResultAndGetOPERACJAString();
            message += OPERACJA + "stat#OK@iden#" + ID + "#" + Czas.getGodzina() + "#";
            message += result + "@";
        }
        setDefaultTextOfStatement();
        return message;
    }
}
