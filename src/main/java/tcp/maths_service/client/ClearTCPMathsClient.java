package tcp.maths_service.client;

import tcp.maths_service.server.MathsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
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
                    String message = generateRequest(userInput);
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

    public static void displayMenu(){
        System.out.println("0) Exit");
        System.out.println("1) Calculate square of a number");
        System.out.println("2) Calculate cube of a number");
        System.out.println("3) Show largest number I have calculated during this session");
        System.out.println("4) Show largest number ever calculated in server's lifetime");
    }

    public static String generateRequest(Scanner userInput){
        boolean valid = false;
        String request = null;

        while(!valid) {
            displayMenu();
            String choice = userInput.nextLine();
            int value = 0;
            switch (choice) {
                case "0":
                    System.out.println("Good choice?");
                    request = MathsService.EXIT;
                    break;
                case "1":
                    value = getValidInt(userInput, "Please enter the number to be squared: ");
                    request = MathsService.SQUARE + MathsService.DELIMITER + value;
                    break;
                case "2":
                    value = getValidInt(userInput, "Please enter the number to be cubed: ");
                    request = MathsService.CUBE + MathsService.DELIMITER + value;
                    break;
                case "3":
                    request = MathsService.MYLARGEST;
                    break;
                case "4":
                    request = MathsService.LARGEST;
                    break;
                default:
                    System.out.println("Please select one of the stated options!");
                    System.out.println("------------------------------------");
                    continue;
            }
            valid = true;
        }
        return request;
    }

    public static int getValidInt(Scanner userInput, String prompt) {
        boolean valid = false;
        int value = 0;
        while(!valid) {
            System.out.println(prompt);
            try {
                value = userInput.nextInt();
                valid = true;
            }catch(InputMismatchException e){
                System.out.println("Numeric data required!");
                userInput.nextLine();
            }
        }
        userInput.nextLine();
        return value;
    }
}
