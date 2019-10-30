import java.util.Scanner;

public class Operacja {
    private int wybor;
    private static String komunikat;
    private static Scanner userEntry;
    private static boolean dzielenie;
    private static boolean closed;
    //id
    private static String id;

    private static String OPER = "oper#";
    private static String STAT = "stat#NULL@";
    private static String IDEN = "iden#";
    private static String TIME="time#";
    private static String[] NUMS =new String[3];
    private static int[] tablicaLiczby =new int[3];

    public Operacja(String ID) {
        dzielenie = false;
        closed = false;
        komunikat = "";
        id = ID;
        NUMS[0]="num1#";
        NUMS[1]="num2#";
        NUMS[2]="num3#";
    }

    private static void setDefaultTextOfStatement() { //zresetowanie pola operacja i iden
        OPER = "oper#";
        IDEN = "iden#";
        NUMS[0]="num1#";
        NUMS[1]="num2#";
        NUMS[2]="num3#";

    }

    void pokazMenu() { //wyświetlenie menu
        System.out.println("Jaka operacja? ");
        System.out.println("--------------- ");
        System.out.println("0. Zakończ ");
        System.out.println("1. Dodawanie ");
        System.out.println("2. Odejmowanie");
        System.out.println("3. Mnozenie ");
        System.out.println("4. Dzielenie ");

        System.out.println("-----------------");
        System.out.println("Czy zakończyć działanie? Jeśli tak, wpisz 0");
        userEntry = new Scanner(System.in);
        wybor = userEntry.nextInt();
    }

    public int getWybor() { //zwraca numer wybranego działania wybrany przez użytkownika
        return wybor;
    }

    public String getKomunikat() { //tworzenie komunikatu do wysłania
        boolean errorFlag = false;

        IDEN += id + "@"; //doklejenie do pola iden ID klienta

        switch (wybor) {
            case 0: {
                closed = true;
                errorFlag = true;
                break;
            }
            case 1: {
                //dodawanie
                errorFlag = false;
                OPER += "dodawanie@";
                getLiczby();
                break;
            }
            case 2: {
                //odejmowanie
                errorFlag = false;
                OPER += "odejmowanie@";
                getLiczby();
                break;
            }
            case 3: {
                //mnożenie
                errorFlag = false;
                OPER += "mnozenie@";
                getLiczby();
                break;
            }
            case 4: {
                //dzielenie
                errorFlag = false;
                OPER += "dzielenie@";
                dzielenie = true;
                getLiczby();
                break;
            }
            default:
                System.out.println("Zły wybór. Wpisz ponownie od 1 do 4");
                errorFlag = true;
        }

        if (errorFlag) {
            if (closed) {
                komunikat = OPER+ "close@" + STAT +IDEN+ "@" + TIME+ Czas.getGodzina()+"@";
            } else {
                komunikat = OPER + "error@" + STAT + IDEN +"@" +TIME +Czas.getGodzina()+"@";
            }

        } else {
            komunikat = OPER + STAT + IDEN +NUMS[0] + NUMS[1]+NUMS[2]+ TIME +Czas.getGodzina()+"@";
        }

        setDefaultTextOfStatement();
        return komunikat;
    }

    private static void getLiczby() {
        System.out.println("Podaj trzy liczby");

        for (int i = 0; i < 3; ) {
            tablicaLiczby[i] = userEntry.nextInt();    //wprowadzanie liczby do zmiennej tymczasowej

            //warunek sprawdzajacy czy dana operacja jest dzieleniem lub, w przypadku dzielenia, sprawdza czy aktualnie ustawiana jest pierwsza liczba (w przypadku dzielenia moze byc to 0)
            if (tablicaLiczby[i] == 0 || !dzielenie) {
                //wpisanie liczby do
                NUMS[i] += Integer.toString(tablicaLiczby[i])+"@";
                i++;
            } else {
                if (dzielenie) { //wykrycie czy dana operacja jest dzieleniem
                    //wykrycie czy zostalo wpisane 0 dla drugiej lub trzeciej liczby, jesli nie, liczba zostanie dopisana, jesli tak, krok dopisania zostanie pominiety
                    if (tablicaLiczby[0] != 0) {
                        NUMS[i] += Integer.toString(tablicaLiczby[i])+"@";
                        i++;
                    } else {
                        System.out.println("To nie może być zero! Wpisz jeszcze raz"); //2 i 3 liczba nie mogą być zerami
                    }
                }
            }
        }
    }
}



