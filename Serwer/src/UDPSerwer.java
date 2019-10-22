
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
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

        private static Boolean[] ID = new Boolean[16];
        private static int counterUsers = 0;

        public static void main(String[] args)
        {
            for(int i=0; i<16; i++) {
                ID[i] = false;
            }

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

        private static int getIdForUser() {
            int tempId = 0;
            for(int i=0; i<16; i++) {
                if(!ID[i]) {
                    tempId = i;
                    ID[i] = true;
                    counterUsers++;
                    break;
                }
            }
            return tempId;
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

                    if(messageReceived.equals("oper#id@")) {
                        messageSendTo = "oper#id#" + getIdForUser() + "@";
                        sendToPacket = new DatagramPacket(messageSendTo.getBytes(), messageSendTo.length(), clientAddress, clientPort);
                        datagramSocket.send(sendToPacket);
                    }
                    else {
                        Operacja operacja = new Operacja(messageReceived);

                        System.out.print(clientAddress);
                        System.out.print(" : ");
                        System.out.println(messageReceived);

                        messageSendTo = operacja.createMessage();


                        sendToPacket = new DatagramPacket(messageSendTo.getBytes(), messageSendTo.length(), clientAddress, clientPort); //stworzenie pakietu do wysłania
                        datagramSocket.send(sendToPacket); //wysłanie odpowiedzi do klienta
                    }
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



