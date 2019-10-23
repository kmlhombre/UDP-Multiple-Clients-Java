import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//linia 38 funkcja do poprawy nie wyciąga liczb z regexa

public class Operacja {

    private String message;
    private float result;

    private static String OPERACJA = "oper#";
    private static String STATUS = "stat#OK@";
    private static String IDEN = "iden#";
    private static String ID = "";
    private static String KOMUNIKAT;


    public Operacja(String KOMUNIKAT) {
        Operacja.KOMUNIKAT = KOMUNIKAT;
        message = "";
        result = 0;
    }

    private void setDefaultTextOfStatement(){ //zresetowanie pola operacja i iden
        OPERACJA = "oper#";
        IDEN= "iden#";
    }
    private void calculateResultAndGetOPERACJAString() {
        int[] liczby = new int[3];
        int counter = 0;

        //regex wykrywajacy 3 liczby w otrzymanym komunikacie od klienta
        Pattern p = Pattern.compile("\\d+#\\d+#\\d+@");
        Matcher m = p.matcher(Operacja.KOMUNIKAT);

        if(m.find()) {
            String temp = m.group();
            p = Pattern.compile("\\d+");
            m = p.matcher(temp);

            while(m.find()) {
                liczby[counter] = Integer.parseInt(m.group());
                counter++;
            }
        }

        if((Pattern.compile("mnozenie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] * liczby[1] * liczby[2];
            System.out.println(result);
            OPERACJA += "mnozenie@";
        }
        else if((Pattern.compile("dzielenie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = (float) (liczby[0] * 1.0 / liczby[1] / liczby[2]);
            System.out.println(result);
            OPERACJA += "dzielenie@";
        }
        else if((Pattern.compile("dodawanie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] + liczby[1] + liczby[2];
            System.out.println(result);
            OPERACJA += "dodawanie@";
        }
        else if((Pattern.compile("odejmowanie")).matcher(Operacja.KOMUNIKAT).find()) {
            result = liczby[0] - liczby[1] - liczby[2];
            System.out.println(result);
            OPERACJA += "odejmowanie@";
        }

    }

    public String createMessage() {
        calculateResultAndGetOPERACJAString();

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(KOMUNIKAT);


        if(m.find()) {
            ID = m.group();
        }

        message += OPERACJA + "stat#OK@iden#" + ID + "#" + Czas.getGodzina() + "#";
        message += Float.toString(result) + "@";

        System.out.println(message);
        setDefaultTextOfStatement();
        return message;
    }
}
