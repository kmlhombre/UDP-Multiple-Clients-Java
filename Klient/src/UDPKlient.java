
import java.io.*;
import java.net.*;
import java.util.*;
//do zrobienia id sesji klienta
// + co jesli wysle zly komunikat (stat error)
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
                Scanner userEntry = new Scanner(System.in);
                int choose = 0;
                String message="", response="";

                do {
                    Operacja.pokazMenu();
                    choose = userEntry.nextInt(); //wybranie opcji z menu
                    message = Operacja.getKomunikat(choose); //pobranie komunikatu od klienta

                    //ogarnac komunikat przyslany

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
                System.out.println("\n Rozłączanie klienta... ");
                datagramSocket.close();
            }
        }
    }




