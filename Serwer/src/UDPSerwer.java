
import java.io.*;
import java.net.*;
import java.util.ArrayList;
//do zrobienia nadawanie id klientom, jeśli klient zakończy transmisję id zostaje przydzielone znowu do obiegu
//wszystkie konfiguracje z komunikatem
// na jaką komendę zatrzymujemy działanie serwera/klienta

    public class UDPSerwer
    {
        private static final int PORT = 8001;
        private static DatagramSocket datagramSocket;
        private static DatagramPacket receivedPacket, sendToPacket;
        private static byte[] buffer ;
        private static int BUFFER_SIZE = 128;

        private ArrayList<Boolean> ID = new ArrayList<>();
        private static int counterUsers = 0;

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

        private int getIdForUser() {
            int tempId = 0;
            if(ID.size() == counterUsers) {
                ID.add(true);
                tempId = counterUsers;
                counterUsers++;
            }
            else {
                for(int i=0; i<ID.size(); i++) {
                    if(!ID.get(i)) {
                        tempId = i;
                        ID.set(i, true);
                        break;
                    }
                }
            }
            return tempId; //przed otrzymaniem i wyslaniem wiadomosci trzeba cos zrobic xd
        }

        private static void handleClient() {
            try {
                String messageReceived, messageSendTo;
                InetAddress clientAddress = null;
                int clientPort;
                do
                {
                    buffer = new byte[BUFFER_SIZE];
                    receivedPacket = new DatagramPacket(buffer,buffer.length);
                    datagramSocket.receive(receivedPacket); //odebranie wiadomości od klienta
                    clientAddress = receivedPacket.getAddress(); //adres klienta
                    clientPort = receivedPacket.getPort(); //port klienta


                    messageReceived = new String(receivedPacket.getData(),0,receivedPacket.getLength());
                    Operacja operacja = new Operacja(messageReceived);

                    System.out.print(clientAddress);
                    System.out.print(" : ");
                    System.out.println(messageReceived);

                    messageSendTo = operacja.createMessage();


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



