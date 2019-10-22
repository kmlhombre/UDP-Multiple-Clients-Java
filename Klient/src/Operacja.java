import java.util.InputMismatchException;
import java.util.Scanner;

public class Operacja {
    private static int wybor;
    private static String komunikat;
    private static Scanner userEntry;
    private static boolean dzielenie;

    private static String OPERACJA = "oper#";
    private static String STATUS = "stat#NULL@";
    private static String IDEN = "iden#";

    public Operacja() {
        dzielenie = false;
        komunikat = "";
    }
    private void setDefaults(){
        OPERACJA = "oper#";
        IDEN = "iden#";
    }
    private static void setDefaultTextOfStatement(){
        OPERACJA = "oper#";
        IDEN = "iden#";
    }

    public static void pokazMenu() {
        System.out.println("Jaka operacja? ");
        System.out.println("--------------- ");
        System.out.println("1. Dodawanie ");
        System.out.println("2. Odejmowanie? ");
        System.out.println("3. Mnozenie ");
        System.out.println("4. Dzielenie ");
        System.out.println("-----------------");

    }

    public static int getWybor() {
        return wybor;
    }

    public static String getKomunikat(int wybor) {

        userEntry = new Scanner(System.in);
        wybor = userEntry.nextInt();
        IDEN += Czas.getGodzina() + "##"; //doklejenie do iden godziny //USUNĄĆ JEDNEGO HASHA..?

    switch (wybor) {
        case 1: {
            //dodawanie
            OPERACJA += "dodawanie@";
            getLiczby();
            break;
        }
        case 2: {
            //odejmowanie
            OPERACJA += "odejmowanie@";
            getLiczby();
            break;
        }
        case 3: {
            //mnożenie
            OPERACJA += "mnozenie@";
            getLiczby();
            break;
        }
        case 4: {
            //dzielenie
            OPERACJA += "dzielenie@";
            dzielenie = true;
            getLiczby();
            break;
        }
        default:
            System.out.println("Zły wybór. Wpisz ponownie od 1 do 4");
    }



        komunikat = OPERACJA + STATUS + IDEN;
        setDefaultTextOfStatement();
        return komunikat;
    }
    private static void getLiczby(){
        System.out.println("Podaj trzy liczby");

        for (int i = 0; i < 3; ) {
            //wprowadzanie liczby do zmiennej tymczasowej
            wybor = userEntry.nextInt();
            //warunek sprawdzajacy czy dana operacja jest dzieleniem lub, w przypadku dzielenia, sprawdza czy aktualnie ustawiana jest pierwsza liczba (w przypadku dzielenia moze byc to 0)
            if (i == 0 || !dzielenie) {
                //wpisanie liczby do operacji iden
                IDEN += Integer.toString(wybor);
                IDEN += (i != 2) ? "#" : "@";
                i++;
            } else {
                //wykrycie czy dana operacja jest dzieleniem
                if (dzielenie) {
                    //wykrycie czy zostalo wpisane 0 dla drugiej lub trzeciej liczby, jesli nie, liczba zostanie dopisana, jesli tak, krok dopisania zostanie pominiety
                    if (wybor != 0) {
                        IDEN += Integer.toString(wybor);
                        IDEN += (i != 2) ? "#" : "@";
                        i++;
                    } else {
                        System.out.println("To nie może być zero! Wpisz jesczcze raz");
                    }
                }
            }
        }
    }
}



