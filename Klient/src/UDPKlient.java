
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//do zrobienia id sesji klienta

    public class UDPKlient
    {

        private static InetAddress IPAdress;
        private static final int PORT = 8001;
        private static DatagramSocket datagramSocket;
        private static DatagramPacket receivedPacket, sendToPacket;
        private static byte[] buffer;
        private static int BUFFER_SIZE = 128;
        //id
        private static String ID_USER = "default";

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

                //prośba o ID
                messageToSend = "oper#id@";
                sendToPacket = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), IPAdress, PORT);
                datagramSocket.send(sendToPacket);
                buffer = new byte[BUFFER_SIZE];

                //otrzymanie pakietu z id
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(receivedPacket);
                serverResponse = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                //regex aby otrzymac id
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(serverResponse);
                if(matcher.find()) {
                    ID_USER = matcher.group();
                }

                do {
                    Operacja operacja = new Operacja(ID_USER);
                    operacja.pokazMenu();
                    choose = operacja.getWybor(); //wybranie opcji z menu
                    messageToSend = operacja.getKomunikat(choose); //pobranie komunikatu od klienta

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
                    else {
                        messageToSend = "oper#close@iden#" + ID_USER + "@";
                        sendToPacket = new DatagramPacket(messageToSend.getBytes(),messageToSend.length(),IPAdress,PORT); //stwórz nowy pakiet do wysłania
                        datagramSocket.send(sendToPacket);// wyślij pakiet do serwera
                        buffer = new byte[BUFFER_SIZE];
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




