package file_server_system.server;

import file_server_system.FileServerProtocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TimeZone;

public class FileServer {

    public static String existsCommand(String[] components) {
        String outgoing = null;
        if (components.length == 2) {
            // Do all logic for what should be done where the user wants to check if a file exists
            String filename = components[1];
            File f = new File(filename);
            if (f.exists()) {
                outgoing = FileServerProtocol.FOUND_RESPONSE;
            } else {
                outgoing = FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = FileServerProtocol.INVALID;
        }
        return outgoing;
    }

    public static String accessedCommand(String[] components) {
        String outgoing = null;
        if (components.length == 2) {
            String filename = components[1];
            File f = new File(filename);
            if (f.exists()) {
                long lastModified = f.lastModified();
                LocalDateTime accessDate =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(lastModified),
                                TimeZone.getDefault().toZoneId());
                outgoing =
                        new String(accessDate.getDayOfWeek() + "" +
                                accessDate.atZone(TimeZone.getDefault().toZoneId()));
            } else {
                outgoing = FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = FileServerProtocol.INVALID;
        }
        return outgoing;
    }

    public static String checkLength(String[] components) {
        String outgoing = null;
        if (components.length == 2) {
            String filename = components[1];
            File f = new File(filename);

            if (f.exists()) {
                long size = f.length();
                outgoing = size + "";

            } else {
                outgoing = FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = FileServerProtocol.INVALID;
        }
        return outgoing;
    }


    public static String lineCommand(String[] components) {
        String outgoing = null;
        if (components.length == 3) {
            String filename = components[1];
            File f = new File(filename);

            if (f.exists()) {
                try {
                    // Get the line number at which to stop
                    int lineNum = Integer.parseInt(components[2]);
                    if (lineNum < 0) {
                        throw new IllegalArgumentException("Line count cannot be less than 0");
                    }

                    // Get appropriate line from file
                    Scanner file = new Scanner(f);
                    String line = null;
                    int count = 0;
                    while (file.hasNext() && count != lineNum) {
                        line = file.nextLine();
                        count++;
                    }

                    // Decide whether to send back line or error
                    if (count == lineNum) {
                        outgoing = line;
                    } else {
                        outgoing = FileServerProtocol.BOUNDS;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Line number supplied is not a number: " + components[2]);
                    outgoing = FileServerProtocol.INVALID;
                } catch (IllegalArgumentException e) {
                    System.out.println("Line number supplied is an invalid value: " + components[2]);
                    System.out.println(e.getMessage());
                    outgoing = FileServerProtocol.BOUNDS;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                outgoing = FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = FileServerProtocol.INVALID;
        }
        return outgoing;
    }


    public static void main(String[] args) {
        // Establish server (MY) listening port
        int myPort = FileServerProtocol.PORT;

        // Create a window for server to access network
        try (DatagramSocket mySocket = new DatagramSocket(myPort)) {

            while (true) {
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
                String[] components = incomingMessage.split(FileServerProtocol.DELIMITER);
                
                // NOW I know what the client has sent - incomingMessage
                // Create a variable to hold the outgoing message
                String outgoing = null;

                switch (components[0]) {
                    case FileServerProtocol.EXISTS:
                        outgoing = existsCommand(components);
                        break;
                    case FileServerProtocol.ACCESSED:
                        outgoing = accessedCommand(components);
                        break;
                    case FileServerProtocol.LINE:
                        outgoing = lineCommand(components);
                        break;
                    case FileServerProtocol.LENGTH:
                        outgoing = checkLength(components);
                        break;
                    default:
                        // Set the outgoing message for all unrecognised commands
                        outgoing = FileServerProtocol.INVALID;
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
        } catch (BindException e) {
            System.out.println("Port already in use. Please supply a different port next time!");
        } catch (SocketException e) {
            System.out.println("An error occurred while setting up local socket.");
        } catch (IOException e) {
            System.out.println("Problem occurred when waiting to receive/receiving a message. Please try again later.");
        }

        System.out.println("Server shutting down...");
    }
}
