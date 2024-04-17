package threading.maths_service.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPMathsServer {
    private static int clientLargest;
    private static int largest;
    
    public static void main(String[] args) {
        // SET UP HOST AND PORT INFO
        // Done in MathsService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(MathsService.PORT)) {
            largest = Integer.MIN_VALUE;
            // REPEATEDLY:
            while (true) {
                // Accept an incoming connection request
                Socket dataSocket = listeningSocket.accept();
                MathsClientHandler clientHandler = new MathsClientHandler(dataSocket, largest);
                Thread worker = new Thread(clientHandler);
                worker.start();
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + MathsService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }
    }


}
