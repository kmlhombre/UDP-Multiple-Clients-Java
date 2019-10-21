import java.util.Scanner;

public class Operacja {

    private static String  OPERACJA = "oper#";
    private static String STATUS = "stat#NULL@";
    private static String IDEN  = "iden#";
    private static String dzialanie;
    private static String komunikat;

    private int liczba1, liczba2, liczba3;
    public Operacja() {
        komunikat="";
    }
    public static void pokazMenu(){
        System.out.println("Jaka operacja? ");
        System.out.println("--------------- ");
        System.out.println("1. Dodawanie ");
        System.out.println("2. Odejmowanie? ");
        System.out.println("3. Mnozenie ");
        System.out.println("4. Dzielenie ");
        System.out.println("-----------------");

    }
    public static String getKomunikat(int choose){

        boolean dzielenie = false;

        switch(choose){
            case 1: {
                //dodawanie
                dzialanie+="dodawanie@";
                break;
            }
            case 2: {
                //odejmowanie
               dzialanie+="odejmowanie@";
                break;
            }
            case 3: {
                //mnożenie
                dzialanie+="mnozenie@";
                break;
            }
            case 4: {
                //dzielenie
                dzialanie+="dzielenie@";
                dzielenie = true;
                //co jesli pierwsza liczba to zero? napisac
                break;
            }
            default: System.out.println("Takiej opcji nie ma. Wpisz jeszcze raz");
        }
        //USUNĄĆ JEDNEGO HASHA..?
        IDEN += Czas.getGodzina()+ "##"; //doklejenie do iden godziny

        int liczba;
        Scanner inputScanner = new Scanner(System.in);
        liczba = inputScanner.nextInt();

        for (int i = 0; i < 3;) {
            //wprowadzanie liczby do zmiennej tymczasowej
            liczba = inputScanner.nextInt();
            //warunek sprawdzajacy czy dana operacja jest dzieleniem lub, w przypadku dzielenia, sprawdza czy aktualnie ustawiana jest pierwsza liczba (w przypadku dzielenia moze byc to 0)
            if (i == 0 || !dzielenie) {
                //wpisanie liczby do operacji iden
                IDEN += Integer.toString(liczba);
                IDEN += (i != 2) ? "#" : "@";
                i++;
            }
            else {
                //wykrycie czy dana operacja jest dzieleniem
                if (dzielenie) {
                    //wykrycie czy zostalo wpisane 0 dla drugiej lub trzeciej liczby, jesli nie, liczba zostanie dopisana, jesli tak, krok dopisania zostanie pominiety
                    if ( liczba != 0) {
                        IDEN += Integer.toString( liczba);
                        IDEN += (i != 2) ? "#" : "@";
                        i++;
                    }
                    else {
                        System.out.println("To nie może być zero! Wpisz jesczcze raz");
                    }
                }
            }
        }
        komunikat = OPERACJA + STATUS + IDEN;
        return komunikat;
    }



}
