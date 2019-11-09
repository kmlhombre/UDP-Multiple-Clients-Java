import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Operacja {
    private int wybor;
    private static String komunikat;
    private static Scanner userEntry;
    private static boolean dzielenie;
    private static int iloscLiczb;
    //id
    private static String id;

    private static String OPER = "oper#";
    private static String STAT = "stat#null@";
    private static String IDEN = "iden#";
    private static String TIME= "time#";
    private static String[] NUMS =new String[3];
    private static long[] tablicaLiczby = new long[3];
    private static ArrayList<Long> sum_n = new ArrayList<Long>();

    private static Boolean sum;

    public Operacja(String ID) {
        dzielenie = false;
        komunikat = "";
        id = ID;
        NUMS[0]="num1#";
        NUMS[1]="num2#";
        NUMS[2]="num3#";
        tablicaLiczby[0]=1; // inicjalizacja jedynkami ( żeby nie aktywować petli dla dzielenia)
        tablicaLiczby[1]=1;
        tablicaLiczby[2]=1;
        sum = false;
    }

    public static void setDefaultTextOfStatement() { //zresetowanie pola operacja i iden
        OPER = "oper#";
        IDEN = "iden#";
        TIME= "time#";
        NUMS[0]="num1#";
        NUMS[1]="num2#";
        NUMS[2]="num3#";
        sum = false;
        sum_n.clear();
    }

    void pokazMenu() throws InputMismatchException { //wyświetlenie menu
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
    public int getWybor() throws NumberFormatException { //zwraca numer wybranego działania wybrany przez użytkownika

        /*blokada przed błędnym wpisaniem wyboru*/
        while((wybor>4) ){
            System.out.println("Zly wybor. Wpisz ponownie od 1 do 4");
            pokazMenu();
        }
        /**/
        return wybor;

    }

    public String getKomunikat() { //tworzenie komunikatu do wysłania

        IDEN += id + "@"; //doklejenie do pola iden ID klienta
        TIME+=Czas.getGodzina() +"@";

        if (wybor == 0) {
            OPER += "close@";
        } else if (wybor == 1) {//dodawanie
            OPER += "dodawanie@";
            sum= true;
            getLiczby();
        } else if (wybor == 2) {//odejmowanie
            OPER += "odejmowanie@";
            getLiczby();
        } else if (wybor == 3) {//mnożenie
            OPER += "mnozenie@";
            getLiczby();
        } else if (wybor == 4) {//dzielenie
            OPER += "dzielenie@";
            dzielenie = true;
            getLiczby();
        }
            if (OPER.equals("oper#close@")) {
                komunikat = OPER + STAT + IDEN + TIME;
            } else {
                if(sum && iloscLiczb!= 3) {

                    komunikat = OPER + STAT + IDEN;

                    for(int i=0; i<sum_n.size(); i++) {
                        komunikat += "num" + (i+1) + "#" + sum_n.get(i) + "@";
                    }
                }
                else {
                    komunikat = OPER + STAT + IDEN + NUMS[0] + NUMS[1] + NUMS[2] ;
                }
                komunikat += TIME;
            }

            setDefaultTextOfStatement();
            return komunikat;
    }

    private static void getLiczby() {
        System.out.println("Podaj ile liczb wpisac ");
        iloscLiczb = userEntry.nextInt();

        if (sum && iloscLiczb !=3) {
            System.out.println("Wpisz liczby ");
            for(int i=0; i< iloscLiczb; i++) {
                long liczba = userEntry.nextLong();
                sum_n.add(liczba);
            }
        } else {
            System.out.println("Podaj trzy liczby");
            for (int i = 0; i < 3; ) {
                tablicaLiczby[i] = userEntry.nextLong();    //wprowadzanie liczby do zmiennej tymczasowej

                /*warunek sprawdzajacy czy dana operacja jest dzieleniem lub, w przypadku dzielenia,
                 *sprawdza czy aktualnie ustawiana jest pierwsza liczba (w przypadku dzielenia moze być to 0)*/
                if (!dzielenie) {
                    //wpisanie liczby do
                    NUMS[i] += tablicaLiczby[i] + "@";
                    i++;
                }

                /*jeżeli operacja to dzielenie, sprawdź czy druga lub czy trzecia liczba to zero
                 * jeśli liczba to nie zero, liczba zostanie dopisana.
                 * jeśli tak, krok dopisania zostanie pominięty, klient musi wpisać liczbę ponownie*/
                else {
                    if (tablicaLiczby[1] != 0 && tablicaLiczby[2] != 0) {
                        NUMS[i] += tablicaLiczby[i] + "@";
                        i++;
                    } else {
                        System.out.println("To nie moze byc zero! Wpisz jeszcze raz"); //2 i 3 liczba nie mogą być zerami
                    }
                }
            }
        }
        }
    }




