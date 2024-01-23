package app;

import java.io.IOException;
import java.net.*;

public class SampleClient {
    public static void main(String[] args) {
        // Need address information
        // PORT
        // IP Address/hostname
        try {
            // OUR address information
            int myPort = 5656;
            DatagramSocket mySocket = new DatagramSocket(myPort);
            mySocket.setSoTimeout(5000);

            // Destination address information
            InetAddress destinationIP = InetAddress.getByName("localhost");
            int destinationPort = 7777;

            // Message to be sent
            String message = "Hello world!";
            // Condition the message for transmission
            byte[] payload = message.getBytes();
            // Build the packet to be sent
            DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destinationPort);
            // Send message to server
            mySocket.send(packet);


            byte [] receivedMessage = new byte[50];
            DatagramPacket incomingMessage = new DatagramPacket(receivedMessage, receivedMessage.length);
            mySocket.receive(incomingMessage);
            receivedMessage = incomingMessage.getData();
            System.out.println("Message received: " + receivedMessage.toString());
        } catch (UnknownHostException e) {
            System.out.println("IP address is not recognised");
            System.out.println(e.getMessage());
        } catch (SocketException e) {
            System.out.println("Problem occurred on the socket");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Problem occurred when working with the socket");
            System.out.println(e.getMessage());
        }
    }
}
