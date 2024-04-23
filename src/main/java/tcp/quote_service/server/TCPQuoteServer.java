package tcp.quote_service.server;

import tcp.quote_service.business.Quote;
import tcp.quote_service.business.QuoteManager;
import tcp.quote_service.protocol.QuoteService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPQuoteServer {
    private static QuoteManager quoteManager;

    public static void main(String[] args) {
        // SET UP HOST AND PORT INFO
        // Done in MathsService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(QuoteService.PORT)) {
            quoteManager = new QuoteManager();
            // REPEATEDLY:
            while (true) {
                // Accept an incoming connection request
                Socket dataSocket = listeningSocket.accept();
                //handleClientSession(dataSocket);
                // Make an instance of our runnable (client handler)
                QuoteClientHandler clientHandler = new QuoteClientHandler(dataSocket, quoteManager);
                Thread wrapper = new Thread(clientHandler);
                wrapper.start();
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + QuoteService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }
    }
}
