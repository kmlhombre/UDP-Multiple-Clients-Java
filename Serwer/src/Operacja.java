import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operacja {

    private String message;
    private String id_klient;

    private static String OPER = "oper#";
    private static String STAT = "stat#";
    private static String IDEN = "iden#";
    private static String RESU = "resu#";
    private static String TIME = "time#";

    private static int NUMS_V[] = new int[3];
    private static int RESU_V;
    private static long TIME_V;
    private static String KOMUNIKAT;


    public Operacja(String KOMUNIKAT) {
        Operacja.KOMUNIKAT = KOMUNIKAT;
        message = "";
        RESU_V = 0;

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(KOMUNIKAT);

        if (m.find()) {
            id_klient = m.group();
        }
    }

    private void setDefaultTextOfStatement() { //zresetowanie pola operacja i iden
        OPER = "oper#";
        STAT = "stat#";
        IDEN = "iden#";
        RESU = "result#";
        TIME = "time#";
    }

    private void calculateResultAndGetOPERACJAString() {
        int counter = 0;

        //regex wykrywajacy 3 liczby w otrzymanym komunikacie od klienta
        Pattern p = Pattern.compile("(\\d+\\.\\d+#){2}\\d+\\.\\d+@");
        Matcher m = p.matcher(Operacja.KOMUNIKAT);

        if (m.find()) {
            String temp = m.group();
            p = Pattern.compile("-*\\d+\\.*\\d+");
            m = p.matcher(temp);

            while (m.find()) {
                NUMS_V[counter] = Integer.parseInt(m.group());
                counter++;
            }
        }

        if (Pattern.compile("mnozenie").matcher(Operacja.KOMUNIKAT).find()) {
            RESU_V = NUMS_V[0] * NUMS_V[1] * NUMS_V[2];
            OPER += "mnozenie@";
        } else if (Pattern.compile("dzielenie").matcher(Operacja.KOMUNIKAT).find()) {
            RESU_V = NUMS_V[0] / NUMS_V[1] / NUMS_V[2];
            OPER += "dzielenie@";
        } else if (Pattern.compile("dodawanie").matcher(Operacja.KOMUNIKAT).find()) {
            RESU_V = NUMS_V[0] + NUMS_V[1] + NUMS_V[2];
            OPER += "dodawanie@";
        } else if (Pattern.compile("odejmowanie").matcher(Operacja.KOMUNIKAT).find()) {
            RESU_V = NUMS_V[0] - NUMS_V[1] - NUMS_V[2];
            OPER += "odejmowanie@";
        } else if (Pattern.compile("getid").matcher(Operacja.KOMUNIKAT).find()) {
            OPER += "setid@";
        } else if (Pattern.compile("close").matcher(Operacja.KOMUNIKAT).find()) {
            OPER += "releaseid@";
        } else if (Pattern.compile("error").matcher(Operacja.KOMUNIKAT).find()) {
            OPER += "agree@";
        }
        RESU += RESU_V + "@";
    }

    String createMessage() {
        STAT += "ok@";
        if(Pattern.compile("getid").matcher(Operacja.KOMUNIKAT).find()) {
            IDEN += UDPSerwer.getIdForUser() + "@";
            message = OPER + STAT + IDEN + TIME + Czas.getGodzina() + "@";
        }
        else if(Pattern.compile("close").matcher(Operacja.KOMUNIKAT).find()) {
            IDEN += "null@";
            message = OPER + STAT + IDEN + TIME + Czas.getGodzina() + "@";
        }
        else if(Pattern.compile("error").matcher(Operacja.KOMUNIKAT).find()) {
            IDEN += id_klient + "@";
            message = OPER + STAT + IDEN + TIME + Czas.getGodzina() + "@";
        }
        else {
            calculateResultAndGetOPERACJAString();
            IDEN += id_klient + "@";
            message = OPER + STAT + IDEN + RESU + TIME + Czas.getGodzina() + "@";
        }
        setDefaultTextOfStatement();
        return message;
    }
}
