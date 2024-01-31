package intro_app.client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class StepByStepClient {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // Establish address information - OUR network port
        int myPort = 10909;

        DatagramSocket mySocket = null;
        try {
            // Create OUR window to the network - our socket for receiving messages
            mySocket = new DatagramSocket(myPort);
            /*
                Logic overview:
                    Repeatedly:
                        Phase 1 (Sending):
                            Ask for message destination information
                            Ask for the message to be sent
                            Create a byte array from the message
                            Create a packet and fill it with:
                                The byte array of the message data
                                The length of the data
                                The destination IP for the message
                                The destination port for the message
                            Send the packet via the socket
                        Phase 2 (Receiving):
                            Create an empty byte array of the largest feasible size
                            Create a packet and fill it with the blank array (so that it has somewhere to put the
                            incoming data)
                            Receive whatever the next incoming message is into the blank packet
                            Extract the payload (received message) from the packet
                            Convert the payload from a byte array to a String using the String constructor (this will let
                             us only use the exact size of data the packet says it was carrying)
                            Display the message to the user
             */
            while(true) {
                System.out.println("Do you want to send a message? (Yes to continue, any other key to exit): ");
                String choice = input.nextLine();
                if(!choice.equalsIgnoreCase("yes")){
                    break;
                }
                System.out.println("Please enter the hostname: ");
                String hostname = input.nextLine();
                // Create address information for SERVER
                InetAddress serverIP = InetAddress.getByName(hostname);

                System.out.println("Please enter the port: ");
                int serverPort = input.nextInt();
                input.nextLine();

                System.out.println("Please enter the message to send: ");
                String message = input.nextLine();
                // CONVERT message into a byte array
                byte[] data = message.getBytes();

                // Build datagram packet for transmission
                DatagramPacket packet = new DatagramPacket(data, data.length, serverIP, serverPort);

                // SEND the message to the network
                mySocket.send(packet);

                // Array to store incoming message
                byte[] payload = new byte[1024 * 64];
                // Packet to store incoming message
                DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
                // Try to receive the message
                System.out.println("Waiting for packet....");
                mySocket.receive(incomingPacket);
                System.out.println("Packet received!");

                int len = incomingPacket.getLength();
                String incomingMessage = new String(payload, 0, len);
                System.out.println("Message reads: " + incomingMessage);
            }
        } catch (BindException e) {
            System.out.println("Port already in use. Please supply a different port next time!");
        }catch (SocketException e){
            System.out.println("An error occurred while setting up local socket.");
        } catch (UnknownHostException e) {
            System.out.println("Server IP hostname does not exist. Please update architecture information.");
        } catch (IOException e) {
            System.out.println("Problem occurred when sending the message. Please try again later.");
        }finally {
            if(mySocket != null){
                mySocket.close();
            }
        }

        System.out.println("Thank you for using our messaging system!");

    }
}
