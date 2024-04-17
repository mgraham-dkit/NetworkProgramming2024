package threading.maths_service.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class MathsClientHandler implements Runnable{

    private Socket dataSocket;
    private int largest;
    private int clientLargest;

    public MathsClientHandler(Socket dataSocket, int largest) {
        this.dataSocket = dataSocket;
        clientLargest = Integer.MIN_VALUE;
        this.largest = largest;
    }

    public void run() {
        // Set up our lines of communication - input and output
        try (Scanner input = new Scanner(dataSocket.getInputStream());
             PrintWriter output = new PrintWriter(dataSocket.getOutputStream())) {
            boolean validSession = true;
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
    }

    private String getLargest(String[] components) {
        String response;
        if(components.length == 1) {
            response = "" + largest;
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }

    private String getClientLargest(String[] components) {
        String response;
        if(components.length == 1) {
            response = "" + clientLargest;
        }else{
            response = MathsService.INVALID;
        }
        return response;
    }

    private String calcSquare(String[] components, String message) {
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

    private String calcCube(String[] components, String message) {
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
