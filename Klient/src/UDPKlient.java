
import java.io.*;
import java.net.*;
import java.util.*;

    public class UDPKlient
    {

        private static InetAddress host;
        private static final int PORT = 8000;
        private static DatagramSocket datagramSocket;
        private static DatagramPacket receivedPacket, sendToPacket;
        private static byte[] buffer;
        private static int BUFFER_SIZE = 128;

        public static void main(String[] args) {
            try { //ustawienie adresu hosta
                host = InetAddress.getLocalHost();
            }
            catch(UnknownHostException uhEx) {
                System.out.println("ID HOSTA nie znaleziono ");
                System.exit(1);
            }
            accessServer();
        }
        private static void accessServer() {
            try {
                datagramSocket = new DatagramSocket();
                Scanner userEntry=new Scanner(System.in);
                String message="", response="";
                do {
                    System.out.println("Wpisz wiadomość:");
                    message = userEntry.nextLine();

                    if(!message.equals("close")) {//jeżeli klient nie zakończy połączenia
                        sendToPacket = new DatagramPacket(message.getBytes(),message.length(),host,PORT); //stwórz nowy pakiet do wysłania
                        datagramSocket.send(sendToPacket);// wyślij pakiet do serwera
                        buffer = new byte[BUFFER_SIZE];

                        receivedPacket = new DatagramPacket(buffer,buffer.length); //odpowiedź od serwera
                        datagramSocket.receive(receivedPacket);
                        response = new String(receivedPacket.getData(),0, receivedPacket.getLength());

                        System.out.println(" \n Odpowiedź serwera: " + response);
                    }
                }while(!message.equals("close")); //jeżeli klient wpisze close zamknięcie gniazda
            }
            catch(IOException ioEx) {
                ioEx.printStackTrace();
            }

            finally {
                System.out.println("\n closing connection.... ");
                datagramSocket.close();
            }
        }
    }




