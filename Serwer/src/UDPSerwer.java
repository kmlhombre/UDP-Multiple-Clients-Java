
import java.io.*;
import java.net.*;
//do zrobienia nadawanie id klientom, jeśli klient zakończy transmisję id zostaje przydzielone znowu do obiegu
//wszystkie konfiguracje z komunikatem
// na jaką komendę zatrzymujemy działanie serwera/klienta

    public class UDPSerwer
    {
        private static final int PORT= 8000;
        private static DatagramSocket datagramSocket;
        private static DatagramPacket receivedPacket, sendToPacket;
        private static byte[] buffer;
        private static int BUFFER_SIZE=128;

        public static void main(String[] args)
        {
            System.out.println("Otwieranie portu\n");
            try {
                datagramSocket = new DatagramSocket(PORT);
            }
            catch(SocketException sockEx) {
                System.out.println("Błąd podczas otwierania portu");
                System.exit(1);
            }
            handleClient();
        }
        private static void handleClient() {
            try {
                String messageReceived, messageSendTo;
                int numMessages = 0; //ilość odebranych wiadomości przez serwer
                InetAddress clientAddress = null;
                int clientPort;
                do
                {
                    buffer = new byte[BUFFER_SIZE];
                    receivedPacket = new DatagramPacket(buffer,buffer.length);
                    datagramSocket.receive(receivedPacket); //odebranie wiadomości od klienta
                    clientAddress = receivedPacket.getAddress(); //adres klienta
                    clientPort = receivedPacket.getPort(); //port klienta
                    messageReceived =new String(receivedPacket.getData(),0,receivedPacket.getLength());

                    System.out.print(clientAddress);
                    System.out.print(" : ");
                    System.out.println(messageReceived);
                    numMessages++;
                    messageSendTo= "Wiadomosc numer: " + numMessages + ": " + messageReceived;
                    sendToPacket=new DatagramPacket(messageSendTo.getBytes(),messageSendTo.length(), clientAddress,clientPort); //stworzenie pakietu do wysłania
                    datagramSocket.send(sendToPacket); //wysłanie odpowiedzi do klienta

                }while(true);
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
            }
            finally {
                System.out.println("\n Zamykanie połączenia  ");
                datagramSocket.close();
            }
        }
    }



