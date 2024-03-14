package tcp.maths_service.client;

import tcp.file_server_system.FileServerProtocol;
import tcp.maths_service.server.MathsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClearTCPMathsClient {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);
        // Requests a connection
        try (Socket dataSocket = new Socket(MathsService.HOST,MathsService.PORT)) {

            // Sets up communication lines
            // Create a Scanner to receive messages
            // Create a Printwriter to send messages
            try (Scanner input = new Scanner(dataSocket.getInputStream());
                 PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
                boolean validSession = true;
                // Repeated:
                while(validSession) {
                    // Ask user for information to be sent
                    System.out.println("Please enter a message to be sent (Send EXIT to end):");
                    String message = userInput.nextLine();
                    // Send message to server
                    output.println(message);
                    // Flush message through to server
                    output.flush();

                    // Receive message from server
                    String response = input.nextLine();
                    // Display result to user
                    System.out.println("Received from server: " + response);
                    if(response.equals(MathsService.SIGNOFF)){
                        validSession = false;
                    }
                }

            }

        } catch (UnknownHostException e) {
            System.out.println("Host cannot be found at this moment. Try again later");
        } catch (IOException e) {
            System.out.println("An IO Exception occurred: " + e.getMessage());
        }
        // Close connection to server
    }
}
