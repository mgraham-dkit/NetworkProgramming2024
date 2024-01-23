package app;

import java.io.IOException;
import java.net.*;

public class SampleClient {
    public static void main(String[] args) {
        try {
            // SET-UP STAGE
            // OUR address information - we listen for messages here
            int myPort = 5656;
            // Create a socket on which to listen for messages to that port
            DatagramSocket mySocket = new DatagramSocket(myPort);
            // Set timeout on the socket so it doesn't wait for a message for longer than 5000 milliseconds
            mySocket.setSoTimeout(5000);

            // Destination address information - IP and port
            InetAddress destinationIP = InetAddress.getByName("localhost");
            int destinationPort = 7777;

            // LOGIC STAGE
            // Message to be sent
            String message = "Hello world!";

            // TRANSMISSION STAGE:
            // Condition the message for transmission
            byte[] payload = message.getBytes();
            // Build the packet to be sent
            DatagramPacket packet = new DatagramPacket(payload, payload.length, destinationIP, destinationPort);
            // Send message to server
            mySocket.send(packet);

            // Create a byte array to hold the payload data
            byte [] receivedMessage = new byte[50];
            // Create a packet to hold the received message
            DatagramPacket incomingMessage = new DatagramPacket(receivedMessage, receivedMessage.length);
            // Receive the message from the network
            // This will BLOCK until it receives a message, i.e. the code will not progress beyond this line!
            mySocket.receive(incomingMessage);
            // Get the data out of the packet
            receivedMessage = incomingMessage.getData();

            // LOGIC STAGE
            // Display the data from the packet
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
