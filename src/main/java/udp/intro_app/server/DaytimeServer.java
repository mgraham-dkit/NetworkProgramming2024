package udp.intro_app.server;

import java.io.IOException;
import java.net.*;
import java.time.LocalDate;

public class DaytimeServer {
    public static void main(String[] args) {
        // Establish server (MY) listening port
        int myPort = 20990;
        DatagramSocket mySocket = null;
        
        // Create a window for server to access network
        try {
            
            mySocket = new DatagramSocket(myPort);

            while(true) {
                // Array to store incoming message
                byte [] payload = new byte[1024*64];
                // Packet to store incoming message
                DatagramPacket incomingPacket = new DatagramPacket(payload, payload.length);
                // Try to receive the message
                System.out.println("Waiting for packet....");
                mySocket.receive(incomingPacket);
                System.out.println("Packet received!");
                
                int len = incomingPacket.getLength();
                String incomingMessage = new String(payload, 0, len);
                System.out.println("Message reads: " + incomingMessage);
                String [] components = incomingMessage.split("%%");
                
                // NOW I know what the client has sent - incomingMessage
                // Create a variable to hold the outgoing message
                String outgoing = null;
                if(incomingMessage.equalsIgnoreCase("daytime")){
                    outgoing=LocalDate.now().toString();
                }else if(components[0].equalsIgnoreCase("echo")){
                    outgoing = components[1];
                }else{
                    outgoing= "unknown command";
                }
                

                // Get the address information from the received packet
                InetAddress clientIP = incomingPacket.getAddress();
                int clientPort = incomingPacket.getPort();

                // Build byte array out of received message (without padding)
                byte[] payloadToBeSent = outgoing.getBytes();
                // Build packet to hold the information
                DatagramPacket sendingPacket = new DatagramPacket(payloadToBeSent, payloadToBeSent.length, clientIP,
                        clientPort);

                mySocket.send(sendingPacket);
            }
        }catch(BindException e){
            System.out.println("Port already in use. Please supply a different port next time!");
        }catch (SocketException e){
            System.out.println("An error occurred while setting up local socket.");
        } catch (IOException e) {
            System.out.println("Problem occurred when waiting to receive/receiving a message. Please try again later.");
        }finally {
            if(mySocket != null){
                mySocket.close();
            }
        }

        System.out.println("Server shutting down...");
    }
}
