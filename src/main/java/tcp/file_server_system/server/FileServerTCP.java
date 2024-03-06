package tcp.file_server_system.server;

import tcp.file_server_system.FileServerProtocol;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.TimeZone;

public class FileServerTCP {
    public static String existsCommand(String[] components) {
        String outgoing = null;
        if (components.length == 2) {
            // Do all logic for what should be done where the user wants to check if a file exists
            String filename = components[1];
            File f = new File(filename);
            if (f.exists()) {
                outgoing = tcp.file_server_system.FileServerProtocol.FOUND_RESPONSE;
            } else {
                outgoing = tcp.file_server_system.FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = tcp.file_server_system.FileServerProtocol.INVALID;
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
                outgoing = tcp.file_server_system.FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = tcp.file_server_system.FileServerProtocol.INVALID;
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
                outgoing = tcp.file_server_system.FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = tcp.file_server_system.FileServerProtocol.INVALID;
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
                        outgoing = tcp.file_server_system.FileServerProtocol.BOUNDS;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Line number supplied is not a number: " + components[2]);
                    outgoing = tcp.file_server_system.FileServerProtocol.INVALID;
                } catch (IllegalArgumentException e) {
                    System.out.println("Line number supplied is an invalid value: " + components[2]);
                    System.out.println(e.getMessage());
                    outgoing = tcp.file_server_system.FileServerProtocol.BOUNDS;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                outgoing = tcp.file_server_system.FileServerProtocol.NOT_FOUND_RESPONSE;
            }
        } else {
            outgoing = tcp.file_server_system.FileServerProtocol.INVALID;
        }
        return outgoing;
    }


    public static void main(String[] args) {
        // Create a server socket to listen for and accept connections
        // - will do three-way handshake component!
        try (ServerSocket listeningSocket = new ServerSocket(FileServerProtocol.PORT)) {
            // Repeatedly accept new clients
            while(true) {
                // Accept the next incoming connection request
                try (Socket dataSocket = listeningSocket.accept()) {
                    // Set up our communication lines
                    // Create our scanner to read in messages
                    // Create our printwriter to send out messages
                    try (
                            Scanner input = new Scanner(dataSocket.getInputStream());
                            PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {

                        // Take in request
                        String request = input.nextLine();

                        // PROGRAM LOGIC!!!

                        System.out.println("Message reads: " + request);
                        String[] components = request.split(tcp.file_server_system.FileServerProtocol.DELIMITER);

                        // NOW I know what the client has sent - incomingMessage
                        // Create a variable to hold the outgoing message
                        String response = null;

                        switch (components[0]) {
                            case tcp.file_server_system.FileServerProtocol.EXISTS:
                                response = existsCommand(components);
                                break;
                            case tcp.file_server_system.FileServerProtocol.ACCESSED:
                                response = accessedCommand(components);
                                break;
                            case tcp.file_server_system.FileServerProtocol.LINE:
                                response = lineCommand(components);
                                break;
                            case tcp.file_server_system.FileServerProtocol.LENGTH:
                                response = checkLength(components);
                                break;
                            default:
                                // Set the outgoing message for all unrecognised commands
                                response = tcp.file_server_system.FileServerProtocol.INVALID;
                        }

                        // Send response
                        output.println(response);
                        // Flush it through to force it out of buffer and on to network
                        output.flush();
                    }
                    // Close the connections -
                    // close the communication lines
                    // Close the dedicated connection
                }
            }
        } catch (BindException e) {
            System.out.println("An exception occurred when binding to the server port " + FileServerProtocol.PORT);
        } catch (IOException e) {
            System.out.println("An IO exception occurred" + e.getMessage());
        }
    }

}
