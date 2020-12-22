import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPSerwer {
    /*zmienne globalne wykorzystywane w transmisji*/
    private static final int PORT = 8005;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket receivedPacket, sendToPacket;
    private static byte[] buffer;
    private static int BUFFER_SIZE = 256;

    /*tablica dostepnych id - na potrzeby zadania zostalo zalozone ze max liczba klientow to 32*/
    private static Boolean[] ID = new Boolean[32];

    /*funkcja główna programu inicjalizujaca tablice z ID oraz otwierajaca port do transmisji*/
    public static void main(String[] args) {
        for (int i = 0; i < 32; i++) {
            ID[i] = false;
        }

        System.out.println("Otwieranie portu\n");
        try {
            datagramSocket = new DatagramSocket(PORT);
        } catch (SocketException sockEx) {
            System.out.println("Błąd podczas otwierania portu");
            System.exit(1);
        }
        handleClient();
    }

    /*funkcja zwracajaca pierwsze dostepne ID dla klienta*/
    public static int getIdForUser() {
        int tempId = 0;
        for (int i = 0; i < 32; i++) {
            if (!ID[i]) {
                tempId = i;
                ID[i] = true;
                break;
            }
        }
        return tempId;
    }

    /*funkcja ustawiajaca ID ostatniego rozlaczonego klienta na dostepne*/
    public static void setIdEmpty(int id) {
        ID[id] = false;
    }

    private static void handleClient() {
        try {

            String messageReceived, messageSendTo;
            InetAddress clientAddress = null; //ustawienie ip klienta
            int clientPort;

            do {
                buffer = new byte[BUFFER_SIZE];
                receivedPacket = new DatagramPacket(buffer, buffer.length);

                datagramSocket.receive(receivedPacket); //odebranie wiadomości od klienta
                /* ---------------------*/

                clientAddress = receivedPacket.getAddress(); //adres klienta
                clientPort = receivedPacket.getPort(); //port klienta
                messageReceived = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                Operacja operacja = new Operacja(messageReceived);
                messageSendTo = operacja.createMessage();

                System.out.print("[R] ");
                System.out.print(clientAddress);
                System.out.print(" : ");
                System.out.println(messageReceived);

                System.out.print("[S] ");
                System.out.print(clientAddress);
                System.out.print(" : ");
                System.out.println(messageSendTo);

                if (Pattern.compile("oper#close@").matcher(messageReceived).find()) {
                    int temp;
                    Pattern p = Pattern.compile("\\d+");
                    Matcher m = p.matcher(messageReceived);
                    if(m.find()) {
                        temp = Integer.parseInt(m.group());
                        setIdEmpty(temp);
                    }
                }

                sendToPacket = new DatagramPacket(messageSendTo.getBytes(), messageSendTo.length(), clientAddress, clientPort); //stworzenie pakietu do wysłania
                datagramSocket.send(sendToPacket); //wysłanie odpowiedzi do klienta
            } while (true);

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } finally {
            System.out.println("Zamykanie połączenia  ");
            datagramSocket.close();
        }
    }


}
