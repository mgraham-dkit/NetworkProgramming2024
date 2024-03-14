package tcp.maths_service.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TCPMathsServer {
    public static void main(String[] args) {
        // SET UP HOST AND PORT INFO
        // Done in MathsService utility class
        // Make a listening socket
        try (ServerSocket listeningSocket = new ServerSocket(MathsService.PORT)) {
            int largest = Integer.MIN_VALUE;
            // REPEATEDLY:
            while (true) {
                // Accept an incoming connection request
                Socket dataSocket = listeningSocket.accept();
                // Set up our lines of communication - input and output
                try (Scanner input = new Scanner(dataSocket.getInputStream());
                     PrintWriter output = new PrintWriter(dataSocket.getOutputStream());) {
                    boolean validSession = true;
                    int clientLargest = Integer.MIN_VALUE;
                    // REPEATEDLY
                    while(validSession){
                        // Take in a request
                        String message = input.nextLine();
                        // Parse the request
                        String [] components = message.split(MathsService.DELIMITER);
                        // Do the requested action and generate an appropriate response
                        String response = "";
                        switch(components[0]){
                            case(MathsService.CUBE):
                                if(components.length == 2){
                                    try{
                                        int value = Integer.parseInt(components[1]);
                                        int result = value * value * value;
                                        response = "" + result;
                                        if(value > clientLargest){
                                            clientLargest = value;
                                        }
                                        if(value > largest){
                                            largest = value;
                                        }
                                    }catch(NumberFormatException e){
                                        response = MathsService.NOT_NUMBER;
                                        System.out.println("No number supplied: " + message);
                                    }
                                }else{
                                    response = MathsService.INVALID;
                                }
                                break;
                            case(MathsService.SQUARE):
                                if(components.length == 2){
                                    try{
                                        int value = Integer.parseInt(components[1]);
                                        int result = value * value;
                                        response = "" + result;
                                        if(value > clientLargest){
                                            clientLargest = value;
                                        }
                                        if(value > largest){
                                            largest = value;
                                        }
                                    }catch(NumberFormatException e){
                                        response = MathsService.NOT_NUMBER;
                                        System.out.println("No number supplied: " + message);
                                    }
                                }else{
                                    response = MathsService.INVALID;
                                }
                                break;
                            case(MathsService.MYLARGEST):
                                response = "" + clientLargest;
                                break;
                            case(MathsService.LARGEST):
                                response = "" + largest;
                                break;
                            case(MathsService.EXIT):
                                validSession = false;
                                response = MathsService.SIGNOFF;
                                break;
                            default:
                                response = MathsService.INVALID;
                        }
                        // Send the response
                        output.println(response);
                        output.flush();
                    }
                }
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
