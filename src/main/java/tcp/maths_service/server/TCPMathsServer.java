package tcp.maths_service.server;

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
                handleClientSession(dataSocket);
            }
        } catch (BindException e) {
            System.out.println("BindException occurred when attempting to bind to port " + MathsService.PORT);
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException occurred on server socket");
            System.out.println(e.getMessage());
        }
    }

    private static void handleClientSession(Socket dataSocket) {
        // Set up our lines of communication - input and output
        try (Scanner input = new Scanner(dataSocket.getInputStream());
             PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
            boolean validSession = true;
            clientLargest = Integer.MIN_VALUE;
            // REPEATEDLY
            while(validSession){
                // Take in a request
                String message = input.nextLine();
                System.out.println("Message received from " + dataSocket.getInetAddress()+":"+ dataSocket.getPort() + ": " + message);
                // Parse the request
                String [] components = message.split(MathsService.DELIMITER);
                // Do the requested action and generate an appropriate response
                String response = null;
                switch(components[0]){
                    case(MathsService.CUBE):
                        response = calcCube(components, message);
                        break;
                    case(MathsService.SQUARE):
                        response = calcSquare(components, message);
                        break;
                    case(MathsService.MYLARGEST):
                        response = getClientLargest(components);
                        break;
                    case(MathsService.LARGEST):
                        response = getLargest(components);
                        break;
                    case(MathsService.EXIT):
                        if(components.length == 1) {
                            validSession = false;
                            response = MathsService.SIGNOFF;
                        }else{
                            response = MathsService.INVALID;
                        }
                        break;
                    default:
                        response = MathsService.INVALID;
                }
                // Send the response
                output.println(response);
                output.flush();
            }
        }catch (IOException e) {
            System.out.println("IOException occurred on data socket when communicating with " + dataSocket.getInetAddress() + ":" + dataSocket.getPort());
            System.out.println(e.getMessage());
        }
        return largest;
    }

    private static String getLargest(String[] components) {
        String response;
        if(components.length == 1) {
            response = "" + largest;
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }

    private static String getClientLargest(String[] components) {
        String response;
        if(components.length == 1) {
            response = "" + clientLargest;
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }

    private static String calcSquare(String[] components, String message) {
        String response;
        if(components.length == 2){
            try{
                int value = Integer.parseInt(components[1]);
                int result = value * value;
                response = "" + result;
                if(result > clientLargest){
                    clientLargest = result;
                }
                if(result > largest){
                    largest = result;
                }
            }catch(NumberFormatException e){
                response = MathsService.NOT_NUMBER;
                System.out.println("No number supplied: " + message);
            }
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }

    private static String calcCube(String[] components, String message) {
        String response;
        if(components.length == 2){
            try{
                int value = Integer.parseInt(components[1]);
                int result = value * value * value;
                response = "" + result;
                if(result > clientLargest){
                    clientLargest = result;
                }
                if(result > largest){
                    largest = result;
                }
            }catch(NumberFormatException e){
                response = MathsService.NOT_NUMBER;
                System.out.println("No number supplied: " + message);
            }
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }
}
