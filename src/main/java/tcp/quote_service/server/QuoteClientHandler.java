package tcp.quote_service.server;

import tcp.quote_service.business.Quote;
import tcp.quote_service.business.QuoteManager;
import tcp.quote_service.protocol.QuoteService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class QuoteClientHandler implements Runnable{
    private Socket dataSocket;
    private QuoteManager quoteManager;

    public QuoteClientHandler(Socket dataSocket, QuoteManager quoteManager){
        this.dataSocket = dataSocket;
        this.quoteManager = quoteManager;
    }

    @Override
    public void run() {
        // Open communication lines
        try (Scanner clientInput = new Scanner(dataSocket.getInputStream());
             PrintWriter clientOutput = new PrintWriter(dataSocket.getOutputStream())) {
            boolean clientSession = true;
            while(clientSession) {
                // Receive a request
                String request = clientInput.nextLine();
                System.out.println(dataSocket.getInetAddress() + ":" + dataSocket.getPort() + " : " + request);

                String response = null;
                // Parse the request
                String[] components = request.split(QuoteService.DELIMITER);
                switch (components[0]) {
                    case QuoteService.GET:
                        response = get(request);
                        break;
                    case "EXIT":
                        response = "GOODBYE";
                        clientSession = false;
                        break;
                    default:
                        response = QuoteService.INVALID;
                }
                // Send a response
                System.out.println("Responding : " + response);
                clientOutput.println(response);
                clientOutput.flush();
            }
        } catch (IOException e) {
            System.out.println("An IO Exception has occurred!");
        }

    }

    private String get(String request){
        String[] components = request.split(QuoteService.DELIMITER);
        String response;
        if (components.length == 1) {
            Quote generated = quoteManager.getRandomQuote();
            response =
                    generated.getQuotationText() + QuoteService.DELIMITER + generated.getAuthor();
        } else {
            response = QuoteService.INVALID;
        }
        return response;
    }
}
