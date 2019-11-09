import java.util.ArrayList;
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

    private static int[] NUMS_V = new int[3];
    private static long RESU_V;
    private static long TIME_V;
    private static String KOMUNIKAT;

    private static ArrayList<Long> sum_n = new ArrayList<Long>();

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
        sum_n.clear();
    }

    private void calculateResultAndGetOPERACJAString() {
        if (Pattern.compile("dodawanie").matcher(Operacja.KOMUNIKAT).find()) {
            OPER += "dodawanie@";

                Pattern p = Pattern.compile("(num\\d+#-?\\d+@)+");
                Matcher m = p.matcher(Operacja.KOMUNIKAT);

                if(m.find()) {
                    p = Pattern.compile("-?\\d+");
                    m = p.matcher(m.group());

                    int count = 0;
                    while(m.find()) {
                        count++;
                        if (count % 2 == 0) {
                            int temp = Integer.parseInt(m.group());
                            sum_n.add((long) temp);
                        }
                    }
                }
                RESU_V = 0;

            for (Long aLong : sum_n) {
                RESU_V += aLong;
            }
            RESU += RESU_V + "@";
        }
        else {
            int counter = 0;
            int counter_number = 0;

            //regex wykrywajacy 3 liczby w otrzymanym komunikacie od klienta
            Pattern p = Pattern.compile("num1#-?\\d+@num2#-?\\d+@num3#-?\\d+@");
            Matcher m = p.matcher(Operacja.KOMUNIKAT);

            if (m.find()) {
                String temp = m.group();
                p = Pattern.compile("-?\\d+");
                m = p.matcher(temp);

                while (m.find()) {
                    counter_number++;
                    if (counter_number % 2 == 0) {
                        NUMS_V[counter] = Integer.parseInt(m.group());
                        //System.out.println(NUMS_V[counter]);
                        counter++;
                    }
                }
            }
            if (Pattern.compile("mnozenie").matcher(Operacja.KOMUNIKAT).find()) {
                RESU_V = NUMS_V[0] * NUMS_V[1] * NUMS_V[2];
                OPER += "mnozenie@";
            } else if (Pattern.compile("dzielenie").matcher(Operacja.KOMUNIKAT).find()) {
                RESU_V = NUMS_V[0] / NUMS_V[1] / NUMS_V[2];
                OPER += "dzielenie@";
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

        }
        /*przekazanie wyniku do komunikatu*/
        RESU += RESU_V + "@";
        /*przekazanie wyniku do komunikatu*/

    }

    String createMessage() {
        STAT += "ok@";
        TIME+=Czas.getGodzina() + "@";

        calculateResultAndGetOPERACJAString();
        if(Pattern.compile("getid").matcher(Operacja.KOMUNIKAT).find()) {
            IDEN += UDPSerwer.getIdForUser() + "@";
            message = OPER + STAT + IDEN + TIME;
        }
        else if(Pattern.compile("close").matcher(Operacja.KOMUNIKAT).find()) {
            IDEN += "null@";
            message = OPER + STAT + IDEN + TIME;
        }
        else {
            IDEN += id_klient + "@";
            message = OPER + STAT + IDEN + RESU + TIME;
        }
        setDefaultTextOfStatement();
        return message;
    }
}
