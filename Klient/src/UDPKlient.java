import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UDPKlient {

    //private static InetAddress IPAdress;
    private static InetAddress IPAdress;

    private static final int PORT = 8001;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket sendToPacket;
    private static byte[] buffer;
    private static int BUFFER_SIZE = 128;
    private static String ID_USER = "default";

    static void setIPAdress(String adresIP) {
        try { //ustawienie adresu hosta
           //IPAdress = InetAddress.getLocalHost(); //ustawienie ip hosta
           IPAdress = InetAddress.getByName(adresIP);//ustawienie ip hosta

        } catch (UnknownHostException uhEx) {
            System.out.println("Podanego ID nie znaleziono");
            System.exit(1);
        }
        accessServer();
    }
    private static void showFinalMessage(String serverResponse){
        String finalMessage="";

        Pattern p = Pattern.compile("\\d\\d:\\d\\d");
        Matcher m = p.matcher(serverResponse);
        if(m.find()){
            finalMessage+="[" + m.group() + "] ";
        }
        finalMessage+="Wynik: ";

        p = Pattern.compile("\\d+\\.\\d+");
        m = p.matcher(serverResponse);
        if(m.find()){
            finalMessage+=m.group();
        }

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
                messageToSend = "oper#id@iden#" + Czas.getGodzina() + "@";
                sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT);
                datagramSocket.send(sendToPacket);

                //otrzymanie pakietu z id
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivedPacket);
                serverResponse = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                //regex aby otrzymac id
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(serverResponse);
                if (matcher.find()) {
                    ID_USER = matcher.group();
                }
            }

            do {

                Operacja operacja = new Operacja(ID_USER);
                operacja.pokazMenu();
                choose = operacja.getWybor(); //wybranie opcji z menu

                if (choose != 0) {
                    messageToSend = operacja.getKomunikat(); //pobranie komunikatu od klienta
                    sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT); //stwórz nowy pakiet do wysłania

                } else {
                    messageToSend = operacja.getKomunikat() + "iden#" + ID_USER + "@";
                    sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT); //stwórz nowy pakiet do wysłania
                }
                datagramSocket.send(sendToPacket);// wyślij pakiet do serwera
                receivedPacket = new DatagramPacket(buffer, buffer.length); //odpowiedź od serwera
                datagramSocket.receive(receivedPacket);
                serverResponse = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                showFinalMessage(serverResponse);

                //System.out.println("Odpowiedź serwera: " + serverResponse);

            } while (choose != 0); //jeżeli klient wpisze close zamknięcie gniazda

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {

            System.out.println("Rozłączanie klienta... ");
            datagramSocket.close();
        }
    }
}
