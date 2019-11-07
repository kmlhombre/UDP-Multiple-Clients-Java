import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UDPSerwer {
    private static final int PORT = 8001;
    private static DatagramSocket datagramSocket;
    private static DatagramPacket receivedPacket, sendToPacket;
    private static byte[] buffer;
    private static int BUFFER_SIZE = 256;

    private static Boolean[] ID = new Boolean[16];

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
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

    public static int getIdForUser() {
        int tempId = 0;
        for (int i = 0; i < 16; i++) {
            if (!ID[i]) {
                tempId = i;
                ID[i] = true;
                break;
            }
        }
        return tempId;
    }

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
