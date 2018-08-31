package test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Sudhabindu on 18-Jun-17.
 */
public class FileClient {

    static final String DEFAULT_PORT = "1729";
    static final String HANDSHAKE = "FILESHARE";

    public static void main(String args[]){

        String computer;
        int port;
        Socket connection = null;
        String command;

        BufferedReader incoming;
        PrintWriter outgoing;
        Scanner userInput;
        String messageIn;

        userInput = new Scanner(System.in);

        System.out.println("Enter the computer: ");
        String line = userInput.nextLine();
        if(line.length() == 0) {
            computer = "localhost";
        }
        else{
            computer = line;
        }

        System.out.println("Enter the port number: ");
        line = userInput.nextLine();
        if(line.length() == 0){
            port = Integer.parseInt(DEFAULT_PORT);
        }
        else{
            port = Integer.parseInt(line);
        }

        System.out.println("Enter the command: \"INDEX\" or GET<filename>");
        command = userInput.nextLine();
        if (command.length() == 0){
            command = "INDEX";
        }

        if (!command.toLowerCase().startsWith("index")){
            command = "get " + command;
        }
        if (!command.equalsIgnoreCase("INDEX") && !command.toLowerCase().startsWith("get")){
            System.out.println("Invalid command");
            System.exit(1);
        }

        if (port <= 0 || port > 65535){
            System.out.println("Illegal port number");
            System.exit(1);
        }

        try {
            connection = new Socket(computer, port);
            incoming = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outgoing = new PrintWriter(connection.getOutputStream());
            outgoing.println(HANDSHAKE);
            outgoing.flush();
            messageIn = incoming.readLine();
            if (messageIn.equalsIgnoreCase(HANDSHAKE)){
                outgoing.println(command);
                outgoing.flush();

                if (command.equalsIgnoreCase("index")){
                    System.out.println("File list from server: ");
                    while (true){
                        line = incoming.readLine();
                        if (line == null){
                            break;
                        }
                        System.out.println("   " + line);
                    }
                }
                else if(command.toLowerCase().startsWith("get")){
                    messageIn = incoming.readLine();
                    if(!messageIn.equals("OK")){
                        System.out.println("File not found on server");
                        System.out.println("Message from server " + messageIn);
                        return;
                    }
                    File fileOut = new File(command.substring(4));
                    if (fileOut.exists()){
                        System.out.println("The file already exists. Overrite?");
                        int response = userInput.nextInt();
                        if (response != 1) {
                            System.out.println("Exiting");
                            return;
                        }

                    }
                    PrintWriter fileStream = new PrintWriter(new FileWriter(fileOut));
                    while (true){
                        line = incoming.readLine();
                        if  (line == null){
                            break;
                        }
                        fileStream.println(line);
                    }
                    if (fileStream.checkError()){
                        System.out.println("Some error occured");
                        return;
                    }
                    fileStream.close();
                }

            }
            else{
                System.out.println("Incompatible server");
                System.exit(1);
            }


        }
        catch (IOException e){
            System.out.println("Error in opening connection");
            System.out.println("Error: " + e);
        }

        finally {
            try {
                connection.close();
            }
            catch (IOException e){
                System.out.println("Some error occured");
                System.out.println("Error: " + e);
            }
        }

    }

}
