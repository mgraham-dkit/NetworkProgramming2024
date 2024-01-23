package intro_app.server;

import java.io.IOException;
import java.net.*;

public class SampleServer {
    public static void main(String[] args) {
        try {
            // SET-UP STAGE
            // OUR address information - we listen for messages here
            int myPort = 7777;
            // Create a socket on which to listen for messages to that port
            DatagramSocket mySocket = new DatagramSocket(myPort);
            // Set timeout on the socket so it doesn't wait for a message for longer than 5000 milliseconds
            mySocket.setSoTimeout(50000);

            // LISTENING STAGE:
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
            System.out.println("Message received from client: " + new String(receivedMessage));

            // TRANSMISSION STAGE:
            // Get address information
            InetAddress destinationIP = incomingMessage.getAddress();
            int destinationPort = incomingMessage.getPort();
            // Build the packet to be sent
            DatagramPacket packet = new DatagramPacket(receivedMessage, receivedMessage.length, destinationIP, destinationPort);
            // Send message to server
            mySocket.send(packet);
            System.out.println("Response sent to client");

            // Close socket!
            mySocket.close();
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
