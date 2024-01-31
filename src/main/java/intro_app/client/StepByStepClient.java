package intro_app.client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class StepByStepClient {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // Establish address information - OUR network port
        int myPort = 10909;
        // Create OUR window to the network - our socket for receiving messages
        try {
            DatagramSocket mySocket = new DatagramSocket(myPort);

            // Create address information for SERVER
            InetAddress serverIP = InetAddress.getByName("localhost");
            int serverPort = 20990;

            System.out.println("Please enter the message to send: ");
            String message = input.nextLine();
            // CONVERT message into a byte array
            byte [] data = message.getBytes();

            // Build datagram packet for transmission
            DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverPort);

            // SEND the message to the network
            mySocket.send(packet);
        } catch (BindException e) {
            System.out.println("Port already in use. Please supply a different port next time!");
        }catch (SocketException e){
            System.out.println("An error occurred while setting up local socket.");
        } catch (UnknownHostException e) {
            System.out.println("Server IP hostname does not exist. Please update architecture information.");
        } catch (IOException e) {
            System.out.println("Problem occurred when sending the message. Please try again later.");
        }
    }
}
