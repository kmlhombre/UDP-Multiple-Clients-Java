import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class UDPKlient {

    private static InetAddress IPAdress;
/*zmienne globalne wykorzystywane w transmisji*/
    private static final int PORT = 8005;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket sendToPacket;
    private static byte[] buffer;
    private static int BUFFER_SIZE = 256;
    private static String ID_USER = "default";
    /*pola zdefiniowane do komunikatu*/
    private static String IDEN = "iden#";
    private static String STAT="stat#null@";
    private static String OPER="oper#";
    private static String TIME="time#";
    /*pola zdefiniowane do komunikatu*/

    static void setIPAdress(String adresIP) {
        try { //ustawienie adresu hosta
         IPAdress = InetAddress.getByName(adresIP);//ustawienie ip hosta

        } catch (UnknownHostException uhEx) {
            System.out.println("Podanego ID nie znaleziono");
            System.exit(1);
        }
        accessServer();
    }
    private static void showResult(String serverResponse){
        String finalMessage="Wynik: ";

        /*regex na wyciągnięcie z komunikatu 'result#otrzymany_wynik' */
        Pattern p = Pattern.compile("result#-?\\d+");
        Matcher m = p.matcher(serverResponse);
        String resultHashWynik ="";
        if(m.find()) resultHashWynik += m.group();

        /*regex na wyciągnięcie z komunikatu result#otrzymany_wynik samego wyniku */
        p = Pattern.compile("-?\\d+");
        m = p.matcher(resultHashWynik);
        if(m.find()) finalMessage += m.group();
        /*wyswietlenie komunikatu */
        System.out.println(finalMessage);

    }
    static void accessServer() {

        try {
            datagramSocket = new DatagramSocket();
            buffer = new byte[BUFFER_SIZE];
            int choose = 0;
            String messageToSend = "", serverResponse = "";
            DatagramPacket receivedPacket;

            if (ID_USER.equals("default")) {
                //prośba o ID
                /* user łączy się pierwszy raz*/
                OPER+="getid@";
                IDEN+="null@";
                TIME+= Czas.getGodzina()+ "@";

                messageToSend = OPER + STAT+ IDEN + TIME; //wiadomość do wysłania

                /* tworzenie do wysłania i wysyłanie datagramu */
                sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT);
                datagramSocket.send(sendToPacket);
                /* tworzenie do wysłania i wysyłanie datagramu */
                System.out.println(messageToSend);

                /* otrzymanie datagramu z id  */
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivedPacket);
                serverResponse = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                /* otrzymanie datagramu z id  */

                System.out.println((serverResponse));
                //regex aby otrzymac id z komunikatu
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(serverResponse);
                if (matcher.find()) ID_USER = matcher.group();

            }

            do {

                Operacja operacja = new Operacja(ID_USER);
                operacja.pokazMenu(); //wyświetlenie menu
                choose = operacja.getWybor(); //wybranie opcji z menu
                messageToSend = operacja.getKomunikat(); //przypisanie do messageToSend komunikatu

                sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT); //stwórz nowy datagram do wysłania

                datagramSocket.send(sendToPacket);// wyślij datagram do serwera
                receivedPacket = new DatagramPacket(buffer, buffer.length); //odpowiedź od serwera
                datagramSocket.receive(receivedPacket);
                serverResponse = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                /* wyświetlenie wyniku*/
               if (choose==1 || choose ==2 || choose ==3 || choose==4) showResult(serverResponse);
               /* */
            } while (choose != 0); //jeżeli wybor==0 komunikat close, zamknięcie gniazda

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            System.out.println("Rozlaczanie klienta... ");
            datagramSocket.close();

        }
    }
}
