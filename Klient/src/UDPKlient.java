
import java.io.*;
import java.net.*;
import java.util.*;
//do zrobienia id sesji klienta
// + co jesli wysle zly komunikat (stat error)
    public class UDPKlient
    {

        private static InetAddress IPAdress;
        private static final int PORT = 8000;
        private static DatagramSocket datagramSocket;
        private static DatagramPacket receivedPacket, sendToPacket;
        private static byte[] buffer;
        private static int BUFFER_SIZE = 128;

        public static void main(String[] args) {
            try { //ustawienie adresu hosta
                IPAdress = InetAddress.getLocalHost();
            }
            catch(UnknownHostException uhEx) {
                System.out.println("ID HOSTA nie znaleziono");
                System.exit(1);
            }
            accessServer();
        }
        private static void accessServer() {
            try {
                datagramSocket = new DatagramSocket();
              //  Scanner userEntry = new Scanner(System.in);
                int choose = 0;
                String messageToSend="", serverResponse="";

                do {
                    Operacja.pokazMenu();
                    choose = Operacja.getWybor(); //wybranie opcji z menu
                    messageToSend = Operacja.getKomunikat(choose); //pobranie komunikatu od klienta

                    //ogarnac komunikat przyslany

                    if(!messageToSend.equals("close")) {//jeżeli klient nie zakończy połączenia
                        sendToPacket = new DatagramPacket(messageToSend.getBytes(),messageToSend.length(),IPAdress,PORT); //stwórz nowy pakiet do wysłania
                        datagramSocket.send(sendToPacket);// wyślij pakiet do serwera
                        buffer = new byte[BUFFER_SIZE];

                        receivedPacket = new DatagramPacket(buffer,buffer.length); //odpowiedź od serwera
                        datagramSocket.receive(receivedPacket);
                        serverResponse = new String(receivedPacket.getData(),0, receivedPacket.getLength());

                        System.out.println(" \n Odpowiedź serwera: " +  serverResponse);
                    }
                }while(!messageToSend.equals("close")); //jeżeli klient wpisze close zamknięcie gniazda

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




