package tcp.quote_service.client;

import tcp.quote_service.protocol.QuoteService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClearTCPQuoteClient {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        // Requests a connection
        try (Socket dataSocket = new Socket(QuoteService.HOST, QuoteService.PORT)) {

            // Sets up communication lines
            // Create a Scanner to receive messages
            // Create a Printwriter to send messages
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                // Repeated:
                // Ask user for information to be sent
                System.out.println("Please enter a message to be sent:");
                String message = userInput.nextLine();
                // Send message to server
                output.println(message);
                // Flush message through to server
                output.flush();

                // Receive message from server
                String response = input.nextLine();

                // Checking if it's responding to a GET request (so we can parse the response appropriately)
                if(message.equals(QuoteService.GET)){
                    handleGetResponse(response);
                }else {
                    // Display result to user
                    System.out.println("Received from server: " + response);
                }
            }

        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            System.out.println("An IO Exception occurred: " + e.getMessage());
        }
        // Close connection to server
    }

    private static void handleGetResponse(String response) {
        String [] responseComponents = response.split(QuoteService.DELIMITER);
        if(responseComponents.length == 2){
            System.out.println("Quote received:");
            System.out.println("\"" + responseComponents[0] + "\"");
            System.out.println("\t- " + responseComponents[1]);
        }else{
            System.out.println("Unrecognised response detected. Please try again later.");
        }
    }
}
