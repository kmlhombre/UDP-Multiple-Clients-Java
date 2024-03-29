import java.util.Scanner;

public class Operacja {
    private int wybor;
    private static String komunikat;
    private static Scanner userEntry;
    private static boolean dzielenie;
    private static boolean closed;
    //id
    private static String id;

    private static String OPERACJA = "oper#";
    private static String STATUS = "stat#NULL@";
    private static String IDEN = "iden#";

    public Operacja(String ID) {
        dzielenie = false;
        closed = false;
        komunikat = "";
        id = ID;
    }

    private static void setDefaultTextOfStatement() { //zresetowanie pola operacja i iden
        OPERACJA = "oper#";
        IDEN = "iden#";

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

        IDEN += id + "#" + Czas.getGodzina() + "#"; //doklejenie do pola iden godziny

        switch (wybor) {
            case 0: {
                closed = true;
                errorFlag = true;
                break;
            }
            case 1: {
                //dodawanie
                errorFlag = false;
                OPERACJA += "dodawanie@";
                getLiczby();
                break;
            }
            case 2: {
                //odejmowanie
                errorFlag = false;
                OPERACJA += "odejmowanie@";
                getLiczby();
                break;
            }
            case 3: {
                //mnożenie
                errorFlag = false;
                OPERACJA += "mnozenie@";
                getLiczby();
                break;
            }
            case 4: {
                //dzielenie
                errorFlag = false;
                OPERACJA += "dzielenie@";
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
                komunikat = OPERACJA + "CLOSE@";
            } else {
                komunikat = OPERACJA + "ERROR@" + STATUS + "null@" + IDEN;
            }

        } else {
            komunikat = OPERACJA + STATUS + IDEN;
        }

        setDefaultTextOfStatement();
        return komunikat;
    }

    private static void getLiczby() {
        System.out.println("Podaj trzy liczby");
        float liczba;
        for (int i = 0; i < 3; ) {
            liczba = userEntry.nextFloat();    //wprowadzanie liczby do zmiennej tymczasowej

            //warunek sprawdzajacy czy dana operacja jest dzieleniem lub, w przypadku dzielenia, sprawdza czy aktualnie ustawiana jest pierwsza liczba (w przypadku dzielenia moze byc to 0)
            if (i == 0 || !dzielenie) {
                //wpisanie liczby do operacji iden
                IDEN += Float.toString(liczba);
                IDEN += (i != 2) ? "#" : "@";
                i++;
            } else {

                if (dzielenie) { //wykrycie czy dana operacja jest dzieleniem
                    //wykrycie czy zostalo wpisane 0 dla drugiej lub trzeciej liczby, jesli nie, liczba zostanie dopisana, jesli tak, krok dopisania zostanie pominiety
                    if (liczba != 0) {
                        IDEN += Float.toString(liczba);
                        IDEN += (i != 2) ? "#" : "@";
                        i++;
                    } else {
                        System.out.println("To nie może być zero! Wpisz jeszcze raz"); //2 i 3 liczba nie mogą być zerami
                    }
                }
            }
        }
    }
}



